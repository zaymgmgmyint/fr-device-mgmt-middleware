package com.eighti.onebkk.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.eighti.onebkk.dto.PersonnelDto;
import com.eighti.onebkk.entity.Device;
import com.eighti.onebkk.entity.SyncTime;
import com.eighti.onebkk.entity.User;
import com.eighti.onebkk.entity.UserDevice;
import com.eighti.onebkk.repository.DeviceRepository;
import com.eighti.onebkk.repository.SyncTimeRepository;
import com.eighti.onebkk.repository.UserDeviceRepository;
import com.eighti.onebkk.repository.UserRepository;
import com.eighti.onebkk.utils.CommonUtil;
import com.eighti.onebkk.utils.DateConstant;
import com.eighti.onebkk.utils.ImageResizer;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Value("${excel.file.path}")
	private String EXCEL_FILE_PATH;

	private static final DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern(DateConstant.DATE_FORMAT_yyyy_mm_dd_hhmmss);

	private final UserRepository userRepository;
	private final SyncTimeRepository syncTimeRepository;
	private final DeviceRepository deviceRepository;
	private final UserDeviceRepository userDeviceRepository;
	private final DeviceInterfaceService deviceInterfaceService;

	public UserService(UserRepository userRepository, SyncTimeRepository syncTimeRepository,
			DeviceRepository deviceRepository, UserDeviceRepository userDeviceRepository,
			DeviceInterfaceService deviceInterfaceService) {

		this.userRepository = userRepository;
		this.syncTimeRepository = syncTimeRepository;
		this.deviceRepository = deviceRepository;
		this.userDeviceRepository = userDeviceRepository;
		this.deviceInterfaceService = deviceInterfaceService;
	}

	public void syncUsersToFRDevices() throws Exception {
		StopWatch watcher = new StopWatch("Execution Time");
		watcher.start("Data Synchronizing");

		// Get the last sync time
		LocalDateTime lastSyncTime = getLastSyncTime();
		LOG.info("syncUsersToFRDevices() >>> Last Sync Time: " + lastSyncTime);

		List<User> userEntityList = new ArrayList<User>();

		try (InputStream inputStream = new FileInputStream(EXCEL_FILE_PATH);
				Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0);
			// TODO [EXCEL PROCESS START]
			for (Row row : sheet) {

				if (row.getRowNum() == 0) { // Skip header row
					continue;
				}

				User user = new User();
				String userId = row.getCell(0).getStringCellValue();

				// Check user is existing or not existing
				if (CommonUtil.validString(userId) && isUserExist(userId)) {
					LOG.info("syncUsersToFRDevices() >>> User is already existing : " + userId);
					Optional<User> userOptional = userRepository.findTopOneByUserId(userId);
					user = userOptional.get();
				}

				// Get the users from excel template
				user.setUserId(row.getCell(0).getStringCellValue());
				user.setUserName(row.getCell(1).getStringCellValue());
				user.setUserPhoneNumber(row.getCell(2).getStringCellValue());
				user.setUserTag(row.getCell(3).getStringCellValue());

				user.setFrCardNumber(row.getCell(4).getStringCellValue());
				user.setFrImageId(row.getCell(5).getStringCellValue());

				user.setUserCreatedDate(LocalDateTime.parse(row.getCell(6).getStringCellValue(), formatter));
				user.setUserLastModifiedDate(LocalDateTime.parse(row.getCell(7).getStringCellValue(), formatter));

				user.setSyncedDatetime(LocalDateTime.now());

				// Check created and last modified date time
				if (lastSyncTime.isBefore(user.getUserCreatedDate())
						|| lastSyncTime.isBefore(user.getUserLastModifiedDate())) {
					LOG.info("syncUsersToFRDevices() >>> New/Update user data: " + user.getUserId());
					userEntityList.add(user);
				}

				// Batch size for saving users
				int batchSize = 20;
				if (userEntityList.size() >= batchSize) {
					saveUsersInBatch(userEntityList);
					userEntityList.clear();
				}

				// Save any remaining users
				if (!userEntityList.isEmpty()) {
					saveUsersInBatch(userEntityList);
				}
			}
			// TODO [EXCEL PROCESS END]

			// TODO [USER PROCESS START]
			// Get users from database and validate the synchronizing time
			List<User> userList = userRepository.findAll();
			userList.forEach(uDto -> {

				Optional<User> user = userRepository.findTopOneByUserId(uDto.getUserId());
				Long userId = user.get().getId();
				List<Device> deviceList = getAllDevices();

				// User device list
				deviceList.forEach(device -> {
					// check user and device is exist or not
					Optional<UserDevice> userDevice = userDeviceRepository.findUserDeviceByUserIdAndDeviceId(userId,
							device.getId());

					UserDevice userDeviceEntity = new UserDevice();
					if (userDevice.isPresent()) {
						// Update the FR device
						userDeviceEntity = userDevice.get();
					} else {
						// Register the FR device
						userDeviceEntity.setUser(user.get());
						userDeviceEntity.setDevice(device);
						userDeviceEntity.setDataSynced(0);

						userDeviceEntity = userDeviceRepository.save(userDeviceEntity);
					}

					// TODO [FR DEVICE PROCESS START]
					// Register the users to the User Device and FR Device
					PersonnelDto personnelDto = prepareUserAndDeviceData(userDeviceEntity);
					PersonnelDto personnelResponse = null;
					if (userDeviceEntity.getDataSynced() != null && userDeviceEntity.getDataSynced() != 1) {
						// Register the user to the FR device
						personnelResponse = deviceInterfaceService.registerFaceScannerTerminal(personnelDto);

					} else {
						// Update the user to the FR device
						personnelResponse = deviceInterfaceService.updateFaceScannerTerminal(personnelDto);
					}

					// update sync status and date_time base on API response
					if (personnelResponse != null && CommonUtil.validString(personnelResponse.getResponseCode())) {
						LOG.info("syncUsersToFRDevices() >>> Personnel response: "
								+ personnelResponse.getResponseMessage());
						userDeviceEntity.setDataSynced(personnelResponse.getDataSynced());
						userDeviceEntity.setSyncedDatetime(LocalDateTime.now());
						userDeviceEntity.setResponseCode(personnelResponse.getResponseCode());
						userDeviceEntity.setResponseMessage(personnelResponse.getResponseMessage());
						userDeviceRepository.save(userDeviceEntity);
					} else {
						LOG.info("syncUsersToFRDevices() >>> Personnel response is NULL");
					}
					// TODO [FR DEVICE PROCESS END]
				});

			});
			// TODO [USER PROCESS END]

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("syncUsersToFRDevices() >>> Exception occurred while reading the file and saving the users: "
					+ e.getMessage(), e);
			throw e;
		}

		watcher.stop();
		LOG.info(watcher.prettyPrint(TimeUnit.MINUTES));
		long elapsedTimeMillis = watcher.getTotalTimeMillis(); // Get elapsed time in milliseconds
		String formattedTime = formatTime(elapsedTimeMillis); // Convert to human-readable format
		LOG.info(">>>=====>>> Execution time : " + formattedTime);
	}

	private PersonnelDto prepareUserAndDeviceData(UserDevice userDeviceTemp) {
		PersonnelDto personaDto = new PersonnelDto();

		User user = userDeviceTemp.getUser();
		Device device = userDeviceTemp.getDevice();

		// Personal information
		personaDto.setUserId(user.getUserId());
		personaDto.setName(user.getUserName());
		personaDto.setTag(user.getUserTag());
		personaDto.setPhoneNo(user.getUserPhoneNumber());
		personaDto.setType(1);

		personaDto.setCardNoId(user.getFrCardNumber());

		if (CommonUtil.validString(user.getFrImageId())) {
			String base64ImageString = "";
			// If image is exceeding 2MB size, then resize the image
			int sizeLimitInBytes = 2 * 1024 * 1024; // 2MB limit

			try {
				boolean exceedsLimit = CommonUtil.exceedsSizeLimit(user.getFrImageId(), sizeLimitInBytes);
				if (exceedsLimit) {
					LOG.info(">>> Base64 string exceeds size limit.");
					base64ImageString = ImageResizer.resizeImageExceedsLimit(base64ImageString);
					user.setFrImageId(base64ImageString);
				} else {
					LOG.info("Base64 string does not exceed size limit.");
				}
			} catch (Exception e) {
				LOG.error("Error while checking image size and resolution : " + e.getMessage());
			}

			personaDto.setFaceId(user.getFrImageId());
		}

		// Device information
		personaDto.setDeviceIp(device.getDeviceIp());
		personaDto.setDeviceName(device.getDeviceName());
		personaDto.setDevicePassword(device.getDevicePassword());

		return personaDto;
	}

	@Transactional
	private void saveUsersInBatch(List<User> users) {
		userRepository.saveAll(users);
	}

	private static String formatTime(long milliseconds) {
		long seconds = milliseconds / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;

		return String.format("%02d:%02d:%02d.%03d", hours, minutes % 60, seconds % 60, milliseconds % 1000);
	}

	private LocalDateTime getLastSyncTime() throws Exception {
		Optional<SyncTime> syncTimeOptional = syncTimeRepository.findTopOneByOrderByLastSyncTimeDesc();

		if (syncTimeOptional.isPresent()) {
			// Get the last sync time
			LocalDateTime lastSyncTime = syncTimeOptional.get().getLastSyncTime();

			// Update the last sync time
			SyncTime entity = syncTimeOptional.get();
			entity.setLastSyncTime(LocalDateTime.now());
			syncTimeRepository.save(entity);

			return lastSyncTime;
		}

		return null;
	}

	private boolean isUserExist(String userId) {
		Optional<User> userOptional = userRepository.findTopOneByUserId(userId);
		return userOptional.isPresent() ? true : false;
	}

	private List<Device> getAllDevices() {

		List<Device> devices = deviceRepository.findAll();
		return CommonUtil.validList(devices) ? devices : new ArrayList<Device>();

	}

}
