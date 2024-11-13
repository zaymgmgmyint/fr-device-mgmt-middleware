package com.eighti.onebkk.service;

import java.security.PrivateKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eighti.onebkk.dto.api.request.HeartBeatCallbackRequest;
import com.eighti.onebkk.dto.api.request.IdentifyCallbackRequest;
import com.eighti.onebkk.dto.api.request.QRCallbackRequest;
import com.eighti.onebkk.dto.api.response.EncryptedDataResponse;
import com.eighti.onebkk.dto.api.response.TcctAuthResponse;
import com.eighti.onebkk.entity.Device;
import com.eighti.onebkk.entity.HeartBeatLog;
import com.eighti.onebkk.entity.IdentifyLog;
import com.eighti.onebkk.repository.DeviceRepository;
import com.eighti.onebkk.repository.HeartBeatLogRepository;
import com.eighti.onebkk.repository.IdentifyLogRepository;
import com.eighti.onebkk.utils.CommonUtil;
import com.eighti.onebkk.utils.JwtDecoder;
import com.eighti.onebkk.utils.RSADecryption;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class DeviceCallbackService {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceCallbackService.class);

	private final HeartBeatLogRepository heartbeatLogRepository;
	private final IdentifyLogRepository identifyLogRepository;
	private final DeviceRepository deviceRepository;
	private final TCCTAPIComponent tcctApiComponent;
	private final RestTemplate restTemplate;

	public DeviceCallbackService(HeartBeatLogRepository heartbeatLogRepository,
			IdentifyLogRepository identifyLogRepository, DeviceRepository deviceRepository,
			TCCTAPIComponent tcctApiComponent, RestTemplate restTemplate) {
		this.heartbeatLogRepository = heartbeatLogRepository;
		this.identifyLogRepository = identifyLogRepository;
		this.deviceRepository = deviceRepository;
		this.tcctApiComponent = tcctApiComponent;
		this.restTemplate = restTemplate;
	}

	// TODO save identify callback log
	@Async("identifyExecutor")
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
	@Async("heartbeatExecutor")
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

		Optional<Device> device = deviceRepository.findDeviceByDeviceKey(callbackData.getDeviceKey());
		if (device.isPresent()) {
			Device deviceEntity = device.get();
			deviceEntity.setLastHeartbeatTime(CommonUtil.validString(callbackData.getTime())
					? CommonUtil.convertMilliSecondToLocalDateTime(callbackData.getTime())
					: LocalDateTime.now());

			deviceRepository.save(deviceEntity);
		}
	}

	public void processQRRequest(QRCallbackRequest requestData) throws Exception {
		LOG.info("processQRRequest() >>> Process QR request: {}", requestData.getDeviceKey());

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jwtPayloadToken = null, accountId = null;
			String decryptedData = "";

			// Parse the JSON string into a JsonNode
			JsonNode qrDataNode = objectMapper.readTree(requestData.getQRdata());

			// Extract the QR request data
			String qrId = qrDataNode.get("id").asText();
			LOG.info("processQRRequest() >>> QR id: {}", qrId);

			// Get Account ID and Permission(Auth) API
			LOG.info("processQRRequest() >>> Auth API URL: {}", tcctApiComponent.getAuthUrl());

			HttpHeaders authHeaders = new HttpHeaders();
			authHeaders.set("x-api-key-id", tcctApiComponent.getAuthKeyId());
			authHeaders.set("x-api-key-secret", tcctApiComponent.getAuthKeySecret());

			// Create an HttpEntity with the headers
			HttpEntity<String> authRequestEntity = new HttpEntity<>(null, authHeaders);

			ResponseEntity<TcctAuthResponse> authResponse = restTemplate.postForEntity(tcctApiComponent.getAuthUrl(),
					authRequestEntity, TcctAuthResponse.class);

			// Check the auth response status and data
			LOG.info("processQRRequest() >>> Auth API Status: {}", authResponse.getStatusCode());
			if (authResponse.getBody() != null) {
				LOG.info("processQRRequest() >>> Auth Response Body: {}", authResponse.getBody());

				String jwtToken = authResponse.getBody().getData().getToken().getValue();
				// LOG.info("processQRRequest() >>> JWT Token: {}", jwtToken);

				jwtPayloadToken = JwtDecoder.extractPayloadTokenFromJwt(jwtToken);
				LOG.info("processQRRequest() >>> JWT Payload Token: {}", jwtPayloadToken);
				accountId = JwtDecoder.extractSubFromJwt(jwtToken);
				LOG.info("processQRRequest() >>> Account Id: {}", accountId);
			}

			// Get Customer Information API
			HttpHeaders headers = new HttpHeaders();
//			authHeaders.set("x-account-id", accountId);
//			authHeaders.set("x-permissions", jwtPayloadToken);
			headers.set("Accept", "*/*");
			headers.set("x-account-id", "3a534b2c-cf7b-4d81-af5b-7d375b5fcbc1");
			headers.set("x-permissions",
					"eyJzdWIiOiIzYTUzNGIyYy1jZjdiLTRkODEtYWY1Yi03ZDM3NWI1ZmNiYzEiLCJpc3MiOiJPbmVCYW5na29rIiwiaWF0IjoxNzMwODc5NjU4LCJleHAiOjE3MzM0NzE2NTgsInBlcm1pc3Npb24iOlt7ImlkIjoiNDE2YmY5OWItMDg0NC00YTVlLThjZjAtNjRiMWZkZDY3YmJlIiwicGVybWl0dGVlX3R5cGUiOiJhY2NvdW50IiwidmFsdWUiOnsibmFtZSI6Im9iLWJtczpmcyIsInNlcnZpY2UiOiJvYi1ibXMiLCJhY3Rpb25zIjpbIioiXSwicmVzb3VyY2VfdHlwZSI6ImZzIiwicmVzb3VyY2UiOnsiaWQiOiJzZWxmIn19LCJhY2NvdW50X2dyb3VwX2lkIjpudWxsLCJyb2xlX2lkIjpudWxsfSx7ImlkIjoiODExYjZiOTgtOTVkMS00NTA2LWJjZDUtYTdkYzRmMjU3NzZlIiwicGVybWl0dGVlX3R5cGUiOiJhY2NvdW50IiwidmFsdWUiOnsibmFtZSI6Im9iLWlhbTp0b2tlbiIsInNlcnZpY2UiOiJvYi1pYW0iLCJhY3Rpb25zIjpbInJlYWQiXSwicmVzb3VyY2VfdHlwZSI6InRva2VuIiwicmVzb3VyY2UiOnsiYWNjb3VudF9pZCI6InNlbGYifX0sImFjY291bnRfZ3JvdXBfaWQiOm51bGwsInJvbGVfaWQiOm51bGx9XX0"); // Ensure
																																																																																																																																																																																																						// this
																																																																																																																																																																																																						// is
																																																																																																																																																																																																						// correct

			String url = tcctApiComponent.getCustomerUrl().concat(qrId);
			LOG.info("processQRRequest() >>> Customer URL: {}", url);

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<String> customerResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

			// Handle the customer info response
			LOG.info("processQRRequest() >>> Customer API Status: {}", customerResponse.getStatusCode());
			if (customerResponse.getBody() != null) {
				LOG.info("processQRRequest() >>> Customer Response Body: {}", customerResponse.getBody());

				EncryptedDataResponse dataResponse = objectMapper.readValue(customerResponse.getBody(),
						EncryptedDataResponse.class);

				String encryptedDataBase64 = dataResponse.getData().getEncryptedData();
				LOG.info("processQRRequest() >>> Encrypted Data: {}", encryptedDataBase64);

				// Decode the encrypted data from Base64
				byte[] encryptedData = Base64.getDecoder().decode(encryptedDataBase64);

				// Sanitize and get the private key
				String sanitizedKey = RSADecryption.sanitizeKey(tcctApiComponent.getRsaPrivateKey());
				PrivateKey privateKey = RSADecryption.getPrivateKeyFromPEM(sanitizedKey);

				// Decrypt the data
				decryptedData = RSADecryption.decrypt(privateKey, encryptedData);
				LOG.info("processQRRequest() >>> Decrypted Data: {}", decryptedData);
			}

			// TODO Matching the user card no and customer id

		} catch (Exception e) {
			LOG.error("processQRRequest() >>> ERROR: {}", e.getMessage(), e);
			throw e;
		}
	}

}
