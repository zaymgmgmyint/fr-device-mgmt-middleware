package com.eighti.onebkk.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import com.eighti.onebkk.dto.UserImportDto;
import com.eighti.onebkk.entity.User;
import com.eighti.onebkk.repository.UserRepository;
import com.eighti.onebkk.utils.DateConstant;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Value("${excel.file.path}")
	private String EXCEL_FILE_PATH;

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void syncUsersToFRDevices() throws Exception {
		StopWatch watcher = new StopWatch("Execution Time");
		watcher.start("Data Synchronizing");

		List<UserImportDto> userImportList = new ArrayList<UserImportDto>();
		List<User> userEntityList = new ArrayList<User>();

		try (InputStream inputStream = new FileInputStream(EXCEL_FILE_PATH);
				Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {

				if (row.getRowNum() == 0) { // Skip header row
					continue;
				}

				UserImportDto userImportDto = new UserImportDto();
				// TODO Get the users from excel template
				userImportDto.setUserId(row.getCell(0).getStringCellValue());
				userImportDto.setUserName(row.getCell(1).getStringCellValue());
				userImportDto.setUserPhoneNumber(row.getCell(2).getStringCellValue());
				userImportDto.setUserTag(row.getCell(3).getStringCellValue());

				userImportDto.setFrCardNumber(row.getCell(4).getStringCellValue());
				userImportDto.setFrImageId(row.getCell(5).getStringCellValue());

				userImportDto.setCreatedDate(row.getCell(6).getStringCellValue());
				userImportDto.setLastModifiedDate(row.getCell(7).getStringCellValue());

				userImportList.add(userImportDto);
			}

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateConstant.DATE_FORMAT_yyyy_mm_dd_hhmmss);

			// TODO save the users to system database
			userImportList.forEach(iu -> {
				User user = new User();
				user.setUserId(iu.getUserId());
				user.setUserName(iu.getUserName());
				user.setUserPhoneNumber(iu.getUserPhoneNumber());
				user.setUserTag(iu.getUserTag());

				user.setFrCardNumber(iu.getFrCardNumber());
				user.setFrImageId(iu.getFrImageId());

				user.setUserCreatedDate(LocalDateTime.parse(iu.getCreatedDate(), formatter));
				user.setUserLastModifiedDate(LocalDateTime.parse(iu.getLastModifiedDate(), formatter));
				
				user.setPermissionType(3); // Allowed Face or Card permission

				userEntityList.add(user);

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

			});

			// TODO Get users from database and validate the synchronizing time

			// TODO Register the users to the FR devices

			// TODO Record the users device registration status

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("saveUsersFromExcel() >>> Exception occurred while reading the file and saving the users: "
					+ e.getMessage(), e);
		}

		watcher.stop();
		LOG.info(watcher.prettyPrint(TimeUnit.MINUTES));
		long elapsedTimeMillis = watcher.getTotalTimeMillis(); // Get elapsed time in milliseconds
		String formattedTime = formatTime(elapsedTimeMillis); // Convert to human-readable format
		LOG.info(">>>=====>>> Execution time : " + formattedTime);
	}

	@Transactional
	protected void saveUsersInBatch(List<User> users) {
		userRepository.saveAll(users);
	}

	private static String formatTime(long milliseconds) {
		long seconds = milliseconds / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;

		return String.format("%02d:%02d:%02d.%03d", hours, minutes % 60, seconds % 60, milliseconds % 1000);
	}

}
