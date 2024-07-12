package com.eighti.onebkk.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.eighti.onebkk.dto.IdentifyLogDto;
import com.eighti.onebkk.dto.api.request.IdentifyAuditLogRequest;
import com.eighti.onebkk.entity.Device;
import com.eighti.onebkk.entity.IdentifyLog;
import com.eighti.onebkk.entity.User;
import com.eighti.onebkk.repository.DeviceRepository;
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

	public AuditLogService(IdentifyLogRepository identifyLogRepository, UserRepository userRepository,
			DeviceRepository deviceRepository) {
		this.identifyLogRepository = identifyLogRepository;
		this.userRepository = userRepository;
		this.deviceRepository = deviceRepository;
	}

	// Get the identify log data
	public List<IdentifyLogDto> searchIdentifyLogList(IdentifyAuditLogRequest requestData) throws Exception {

		List<IdentifyLogDto> dtoList = new ArrayList<IdentifyLogDto>();

		// Convert millisecond to local data time
		LocalDate fromDate = CommonUtil.validString(requestData.getFromDate())
				? CommonUtil.changeStringToDate(requestData.getFromDate())
				: LocalDate.now();

		LocalDate toDate = CommonUtil.validString(requestData.getToDate())
				? CommonUtil.changeStringToDate(requestData.getToDate())
				: LocalDate.now();

		// get the identify log data based on the search criteria
		List<IdentifyLog> identifyLogs = CommonUtil.validList(requestData.getDeviceKeys()) ? identifyLogRepository
				.findByLogDateTimeBetweenAndDeviceKeyIn(fromDate, toDate, requestData.getDeviceKeys())
				: identifyLogRepository.findByLogDateTimeBetween(fromDate, toDate);

		// Loop through the identify log list
		identifyLogs.forEach(log -> {
			LOG.info("searchIdentifyLogList() >>> Identify log: " + log.getId() + ", " + log.getPersonId() + ", "
					+ log.getDeviceKey());

			IdentifyLogDto identifyLogDto = new IdentifyLogDto();

			identifyLogDto.setLogId(log.getId());
			identifyLogDto.setSuccess(DeviceCodeConstant.IdentifyTypeEnum.NO_1.getCode().equals(log.getIdentifyType()));

			// Get the user and device information
			Optional<User> userOptional = userRepository.findTopOneByUserId(log.getPersonId());
			Optional<Device> deviceOptional = deviceRepository.findDeviceByDeviceKey(log.getDeviceKey());

			// Prepare the identify log DTO list
			// User information
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				identifyLogDto.setPersonId(user.getUserId());
				identifyLogDto.setPersonName(user.getUserName());
				identifyLogDto.setPersonPhoneNumber(user.getUserPhoneNumber());
			} else {
				identifyLogDto.setPersonId(log.getPersonId());
			}

			// Device information
			if (deviceOptional.isPresent()) {
				Device device = deviceOptional.get();
				identifyLogDto.setDeviceName(device.getDeviceName());
				identifyLogDto.setDeviceKey(device.getDeviceKey());
				identifyLogDto.setDeviceIp(device.getDeviceIp());
			}

			// Identity callback information
			identifyLogDto.setAliveType(log.getAliveType());
			identifyLogDto.setAliveTypeDesc(DeviceCodeConstant.AliveTypeEnum.getDescriptionByCode(log.getAliveType()));

			identifyLogDto.setComparisonType(log.getType());
			identifyLogDto
					.setComparisonTypeDesc(DeviceCodeConstant.ComparisonTypeEnum.getDescriptionByCode(log.getType()));

			identifyLogDto.setIdCardNum(log.getIdcardNum());

			identifyLogDto.setIdentifyType(log.getIdentifyType());
			identifyLogDto.setIdentifyTypeDesc(
					DeviceCodeConstant.IdentifyTypeEnum.getDescriptionByCode(log.getIdentifyType()));

			identifyLogDto.setIdentifyPattern(log.getModel());
			identifyLogDto.setIdentifyPatternDesc(
					DeviceCodeConstant.IdentifyPatternEnum.getDescriptionByCode(log.getModel()));

			identifyLogDto.setIdentifyDate(log.getLogTime().toString());
			identifyLogDto.setIdentifyDateTime(log.getLogDateTime().format(formatter));

			dtoList.add(identifyLogDto);

		});

		return dtoList;

	}

}
