package com.eighti.onebkk.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.eighti.onebkk.dto.request.HeartBeatCallbackRequest;
import com.eighti.onebkk.dto.request.IdentifyCallbackRequest;
import com.eighti.onebkk.entity.HeartBeatLog;
import com.eighti.onebkk.entity.IdentifyLog;
import com.eighti.onebkk.repository.HeartBeatLogRepository;
import com.eighti.onebkk.repository.IdentifyLogRepository;
import com.eighti.onebkk.utils.CommonUtil;

import jakarta.transaction.Transactional;

@Service
public class DeviceCallbackService {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceCallbackService.class);

	private final HeartBeatLogRepository heartbeatLogRepository;
	private final IdentifyLogRepository identifyLogRepository;

	public DeviceCallbackService(HeartBeatLogRepository heartbeatLogRepository,
			IdentifyLogRepository identifyLogRepository) {
		this.heartbeatLogRepository = heartbeatLogRepository;
		this.identifyLogRepository = identifyLogRepository;
	}

	// TODO save identify callback log
	@Async("callBackExecutor")
	@Transactional
	public void saveIdentifyCallbackLog(IdentifyCallbackRequest callbackData) throws Exception {
		LOG.info("saveIdentifyCallbackLog() >>> Saving identify log: " + callbackData.getDeviceKey());

		IdentifyLog identifyLog = new IdentifyLog();
		identifyLog.setAliveType(callbackData.getAliveType());
		identifyLog.setBase64(callbackData.getBase64());
		identifyLog.setDeviceKey(callbackData.getDeviceKey());
		identifyLog.setIdcardNum(callbackData.getIdcardNum());
		identifyLog.setIdentifyType(callbackData.getIdentifyType());
		identifyLog.setIp(callbackData.getIp());
		identifyLog.setModel(callbackData.getModel());
		identifyLog.setPassTimeType(callbackData.getPassTimeType());
		identifyLog.setPath(callbackData.getPath());
		identifyLog.setPermissionTimeType(callbackData.getPermissionTimeType());
		identifyLog.setPersonId(callbackData.getPersonId());
		identifyLog.setRecType(callbackData.getRecType());
		identifyLog.setTime(callbackData.getTime());
		identifyLog.setDstOffset(callbackData.getDstOffset());
		identifyLog.setPassbackTriggerType(callbackData.getPassbackTriggerType());
		identifyLog.setMaskState(callbackData.getMaskState());
		identifyLog.setWorkCode(callbackData.getWorkCode());
		identifyLog.setAttendance(callbackData.getAttendance());
		identifyLog.setType(callbackData.getType());
		identifyLog.setLogDateTime(LocalDateTime.now());

		identifyLog.setLogTime(CommonUtil.validString(callbackData.getTime())
				? CommonUtil.convertMilliSecondToLocalDate(callbackData.getTime())
				: LocalDate.now());

		identifyLog.setLogDateTime(CommonUtil.validString(callbackData.getTime())
				? CommonUtil.convertMilliSecondToLocalDateTime(callbackData.getTime())
				: LocalDateTime.now());

		identifyLogRepository.save(identifyLog);

	}

	// save heart beat callback log
	@Async("callBackExecutor")
	@Transactional
	public void saveHeartBeatCallbackLog(HeartBeatCallbackRequest callbackData) throws Exception {

		LOG.info("saveHeartBeatLog() >>> Saving heart beat log: " + callbackData.getDeviceKey());

		HeartBeatLog heartBeatLog = new HeartBeatLog();
		heartBeatLog.setDeviceKey(callbackData.getDeviceKey());
		heartBeatLog.setTime(callbackData.getTime());
		heartBeatLog.setIp(callbackData.getIp());
		heartBeatLog.setPersonCount(callbackData.getPersonCount());
		heartBeatLog.setFaceCount(callbackData.getFaceCount());
		heartBeatLog.setFingerCount(callbackData.getFingerCount());
		heartBeatLog.setVersion(callbackData.getVersion());
		heartBeatLog.setFreeDiskSpace(callbackData.getFreeDiskSpace());
		heartBeatLog.setCpuUsageRate(callbackData.getCpuUsageRate());
		heartBeatLog.setCpuTemperature(callbackData.getCpuTemperature());
		heartBeatLog.setMemoryUsageRate(callbackData.getMemoryUsageRate());
		heartBeatLog.setDeviceName(callbackData.getDeviceName());
		heartBeatLog.setSdkVersion(callbackData.getSDKVersion());
		heartBeatLog.setCompanyName(callbackData.getCompanyName());
		heartBeatLog.setLogDateTime(LocalDateTime.now());

		heartbeatLogRepository.save(heartBeatLog);
	}

}
