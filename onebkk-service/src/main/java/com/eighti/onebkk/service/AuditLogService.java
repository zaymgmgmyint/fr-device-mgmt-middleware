package com.eighti.onebkk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.eighti.onebkk.dto.IdentifyLogDto;
import com.eighti.onebkk.dto.PaginatedResponse;
import com.eighti.onebkk.dto.api.request.IdentifyAuditLogRequest;
import com.eighti.onebkk.entity.Device;
import com.eighti.onebkk.entity.IdentifyLog;
import com.eighti.onebkk.entity.User;
import com.eighti.onebkk.repository.DeviceRepository;
import com.eighti.onebkk.repository.IdentifyLogCustomRepository;
import com.eighti.onebkk.repository.IdentifyLogRepository;
import com.eighti.onebkk.repository.UserRepository;
import com.eighti.onebkk.utils.CommonUtil;
import com.eighti.onebkk.utils.DateConstant;
import com.eighti.onebkk.utils.device.common.DeviceCodeConstant;

@Service
public class AuditLogService {

	private static final Logger LOG = LoggerFactory.getLogger(AuditLogService.class);

	private static final DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern(DateConstant.DATE_FORMAT_yyyy_mm_dd_hhmmss_a);

	private final IdentifyLogRepository identifyLogRepository;
	private final UserRepository userRepository;
	private final DeviceRepository deviceRepository;
	private final IdentifyLogCustomRepository identifyLogCustomRepository;

	public AuditLogService(IdentifyLogRepository identifyLogRepository, UserRepository userRepository,
			DeviceRepository deviceRepository, IdentifyLogCustomRepository identifyLogCustomRepository) {
		this.identifyLogRepository = identifyLogRepository;
		this.userRepository = userRepository;
		this.deviceRepository = deviceRepository;
		this.identifyLogCustomRepository = identifyLogCustomRepository;
	}

	// Get the identify log data
	public PaginatedResponse<IdentifyLogDto> searchIdentifyLogList(IdentifyAuditLogRequest requestData)
			throws Exception {
		LOG.info("searchIdentifyLogList() >>> Request data: " + requestData.toString());
		;

		List<IdentifyLogDto> dtoList = new ArrayList<IdentifyLogDto>();

		// Prepare the auth type filter list
		List<String> authTypes = DeviceCodeConstant.AuthTypeEnum.getIdentifyPatterns(requestData.getAuthType());

		// Prepare the auth success filter list
		List<String> authSuccessTypes = DeviceCodeConstant.IdentifyTypeEnum
				.getIdentifyTypes(String.valueOf(requestData.getAuthSuccessType()));

		// Convert millisecond to local data time
		LocalDateTime fromDate = CommonUtil.validString(requestData.getFromDate())
				? CommonUtil.changeStringToDate(requestData.getFromDate())
				: null;

		LocalDateTime toDate = CommonUtil.validString(requestData.getToDate())
				? CommonUtil.changeStringToDate(requestData.getToDate())
				: null;

		List<String> deviceKeys = CommonUtil.validList(requestData.getDeviceKeys()) ? requestData.getDeviceKeys()
				: new ArrayList<String>();

		Integer pageNo = CommonUtil.validInteger(requestData.getPage()) ? requestData.getPage() : 1;

		// Retrieve the identify log data based on the search criteria

		PaginatedResponse<IdentifyLog> identifyLogEntity = identifyLogCustomRepository.searchIdentifyLogs(fromDate,
				toDate, requestData.getRole(), requestData.getCompany(), deviceKeys, authTypes, authSuccessTypes,
				pageNo);

		List<IdentifyLog> identifyLogs = (identifyLogEntity != null && identifyLogEntity.getData() != null)
				? identifyLogEntity.getData()
				: new ArrayList<IdentifyLog>();

//		List<IdentifyLog> identifyLogs = CommonUtil.validList(requestData.getDeviceKeys())
//				? identifyLogRepository.findByLogDateTimeBetweenAndDeviceKeyIn(fromDate, toDate,
//						requestData.getDeviceKeys(), authTypes, authSuccessTypes)
//				: identifyLogRepository.findByLogDateTimeBetween(fromDate, toDate, authTypes, authSuccessTypes);

		// Loop through the identify log list
		identifyLogs.forEach(log -> {
			LOG.info("searchIdentifyLogList() >>> Identify log: " + log.getId() + ", " + log.getPersonId() + ", "
					+ log.getDeviceKey());

			IdentifyLogDto identifyLogDto = new IdentifyLogDto();

			identifyLogDto.setLogId(log.getId());
			identifyLogDto.setAuthSuccessType(
					DeviceCodeConstant.IdentifyTypeEnum.NO_1.getCode().equals(log.getIdentifyType()) ? 1 : 2);

			// Get the user and device information
			Optional<User> userOptional = userRepository.findTopOneByUserId(log.getPersonId());
			Optional<Device> deviceOptional = deviceRepository.findDeviceByDeviceKey(log.getDeviceKey());

			// Prepare the identify log DTO list
			// User information
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				identifyLogDto.setUserId(user.getUserId());
				identifyLogDto.setFirstName(user.getFirstName());
				identifyLogDto.setLastName(user.getLastName());
				identifyLogDto.setUserRole(user.getUserTag());
				identifyLogDto.setUserCompany(user.getCompany());
				identifyLogDto.setUserBaseLocation(user.getBaseLocation());
			} else if (DeviceCodeConstant.IdentifyTypeEnum.NO_1.getCode().equals(log.getIdentifyType())) {
				identifyLogDto.setUserId(log.getPersonId());
				identifyLogDto.setFirstName("UnRegistered");
			} else {
				identifyLogDto.setUserId(log.getPersonId());
				identifyLogDto.setFirstName("");
				identifyLogDto.setLastName("");
				identifyLogDto.setUserRole("");
				identifyLogDto.setUserCompany("");
				identifyLogDto.setUserBaseLocation("");
			}

			// Device information
			if (deviceOptional.isPresent()) {
				Device device = deviceOptional.get();
				identifyLogDto.setDeviceName(device.getDeviceName());
				identifyLogDto.setDeviceKey(device.getDeviceKey());
				identifyLogDto.setDeviceIp(device.getDeviceIp());
			} else {
				identifyLogDto.setDeviceName("UnRegistered");
				identifyLogDto.setDeviceKey(log.getDeviceKey());
				identifyLogDto.setDeviceIp(log.getIp());
			}

			// Identity callback information

			identifyLogDto.setAuthType(DeviceCodeConstant.ComparisonTypeEnum.getMsgByCode(log.getType()));
			identifyLogDto.setAuthTypeDesc(DeviceCodeConstant.ComparisonTypeEnum.getDescriptionByCode(log.getType()));

			identifyLogDto.setIdCardNum(log.getIdcardNum());

			identifyLogDto.setIdentifyTypeDesc(
					DeviceCodeConstant.IdentifyTypeEnum.getDescriptionByCode(log.getIdentifyType()));

			identifyLogDto.setIdentifyPatternDesc(
					DeviceCodeConstant.IdentifyPatternEnum.getDescriptionByCode(log.getModel()));

			identifyLogDto.setIdentifyDateTime(log.getLogDateTime().format(formatter));
			identifyLogDto.setIdentifyTimestamp(log.getTime());

			dtoList.add(identifyLogDto);

		});

		PaginatedResponse<IdentifyLogDto> dtoResponse = new PaginatedResponse<IdentifyLogDto>(
				identifyLogEntity.getTotalRecords(), dtoList);
		return dtoResponse;

	}

}
