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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.eighti.onebkk.dto.QRCallbackDto;
import com.eighti.onebkk.dto.api.request.HeartBeatCallbackRequest;
import com.eighti.onebkk.dto.api.request.IdentifyCallbackRequest;
import com.eighti.onebkk.dto.api.request.QRCallbackRequest;
import com.eighti.onebkk.dto.api.response.EncryptedDataResponse;
import com.eighti.onebkk.dto.api.response.TcctAuthResponse;
import com.eighti.onebkk.entity.Device;
import com.eighti.onebkk.entity.HeartBeatLog;
import com.eighti.onebkk.entity.IdentifyLog;
import com.eighti.onebkk.entity.QRCallbackLog;
import com.eighti.onebkk.entity.User;
import com.eighti.onebkk.repository.DeviceRepository;
import com.eighti.onebkk.repository.HeartBeatLogRepository;
import com.eighti.onebkk.repository.IdentifyLogRepository;
import com.eighti.onebkk.repository.QrCallbackLogRepository;
import com.eighti.onebkk.repository.UserRepository;
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
	private final UserRepository userRepository;
	private final QrCallbackLogRepository qRCallbackLogRepository;

	public DeviceCallbackService(HeartBeatLogRepository heartbeatLogRepository,
			IdentifyLogRepository identifyLogRepository, DeviceRepository deviceRepository,
			TCCTAPIComponent tcctApiComponent, RestTemplate restTemplate, UserRepository userRepository,
			QrCallbackLogRepository qRCallbackLogRepository) {
		this.heartbeatLogRepository = heartbeatLogRepository;
		this.identifyLogRepository = identifyLogRepository;
		this.deviceRepository = deviceRepository;
		this.tcctApiComponent = tcctApiComponent;
		this.restTemplate = restTemplate;
		this.userRepository = userRepository;
		this.qRCallbackLogRepository = qRCallbackLogRepository;
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

	public QRCallbackDto processQRRequest(QRCallbackRequest requestData) throws Exception {
		LOG.info("processQRRequest() >>> Process QR request: {}", requestData.getDeviceKey());

		QRCallbackDto qrCallbackDto = null;
		String customerUid = null;
		String qrId = null;
		String encryptedDataBase64 = null;
		QRCallbackLog qrCallbackLog = new QRCallbackLog();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jwtPayloadToken = null, accountId = null;
			String decryptedData = "";

			// Parse the JSON string into a JsonNode
			JsonNode qrDataNode = objectMapper.readTree(requestData.getQRdata());

			// Extract the QR request data
			qrId = qrDataNode.get("id").asText();
			LOG.info("processQRRequest() >>> QR id: {}", qrId);

			// Get Account ID and Permission(Authentication) API
			LOG.info("processQRRequest() >>> Auth API URL: {}", tcctApiComponent.getAuthUrl());

			HttpHeaders authHeaders = new HttpHeaders();
			authHeaders.set("x-api-key-id", tcctApiComponent.getAuthKeyId());
			authHeaders.set("x-api-key-secret", tcctApiComponent.getAuthKeySecret());

			// Create an HttpEntity with the headers
			HttpEntity<String> authRequestEntity = new HttpEntity<>(null, authHeaders);

			ResponseEntity<TcctAuthResponse> authResponse = restTemplate.postForEntity(tcctApiComponent.getAuthUrl(),
					authRequestEntity, TcctAuthResponse.class);

			// Check the Authentication response status and data
			LOG.info("processQRRequest() >>> Auth API Status: {}", authResponse.getStatusCode());
			if (authResponse.getBody() != null) {
				LOG.info("processQRRequest() >>> Auth Response Body: {}", authResponse.getBody());

				String jwtToken = authResponse.getBody().getData().getToken().getValue();
				// LOG.info("processQRRequest() >>> JWT Token: {}", jwtToken);

				jwtPayloadToken = JwtDecoder.extractPayloadTokenFromJwt(jwtToken);
				LOG.info("processQRRequest() >>> JWT Payload Token: {}", jwtPayloadToken);
				accountId = JwtDecoder.extractSubFromJwt(jwtToken);
				LOG.info("processQRRequest() >>> Account Id: {}", accountId);
			} else {
				LOG.info("processQRRequest() >>> Authentication API response is NULL");
			}

			// Get Customer Information API
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "*/*");
			headers.set("x-account-id", accountId);
			headers.set("x-permissions", jwtPayloadToken);

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

				encryptedDataBase64 = dataResponse.getData().getEncryptedData();
				LOG.info("processQRRequest() >>> Encrypted Data: {}", encryptedDataBase64);

				// Decode the encrypted data from Base64
				byte[] encryptedData = Base64.getDecoder().decode(encryptedDataBase64);

				// Sanitize and get the private key
				String sanitizedKey = RSADecryption.sanitizeKey(tcctApiComponent.getRsaPrivateKey());
				PrivateKey privateKey = RSADecryption.getPrivateKeyFromPEM(sanitizedKey);

				// Decrypt the data
				decryptedData = RSADecryption.decrypt(privateKey, encryptedData);
				LOG.info("processQRRequest() >>> Decrypted Data: {}", decryptedData);

				// Parse the JSON string into a JsonNode
				JsonNode customerDataNode = objectMapper.readTree(decryptedData);
				customerUid = customerDataNode.get("uid").asText();
				LOG.info("processQRRequest() >>> custome uid: {}", customerUid);
			} else {
				LOG.info("processQRRequest() >>> Customer API response is NULL");
			}

			// Check the customer id is exist in system
			Optional<User> userOptional = userRepository
					.findUserByAccountIdWithLimit(CommonUtil.validString(customerUid) ? customerUid.trim() : "-");

			// Check customer info API and matching customer is id exist or not result
			qrCallbackDto = new QRCallbackDto();
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				LOG.info("processQRRequest() >>> User account id is exist in system.");
				qrCallbackDto.setSuccess(true);
				qrCallbackDto.setCardNo(user.getFrCardNumber());
			}

			// into audit log table
			qrCallbackLog.setQrData(requestData.getQRdata());
			qrCallbackLog.setQrId(qrId);
			qrCallbackLog.setEncryptedData(encryptedDataBase64);
			qrCallbackLog.setDecryptedData(customerUid);
			qrCallbackLog.setResponse(customerResponse.getBody() != null ? customerResponse.getBody() : null);
			qrCallbackLog.setSuccess(true);

		} catch (HttpClientErrorException ex) {
			// Catch 4xx errors (like 404)
			if (ex.getStatusCode().is4xxClientError()) {
				LOG.error("Client error occurred: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
			}
			qrCallbackLog.setSuccess(false);
			qrCallbackLog.setErrorCode(ex.getStatusCode().toString());
			qrCallbackLog.setErrrMessage(ex.getMessage());
			throw ex;
		} catch (HttpServerErrorException ex) {
			// Catch 5xx errors (like 500)
			if (ex.getStatusCode().is5xxServerError()) {
				LOG.error("Server error occurred: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
			}
			qrCallbackLog.setSuccess(false);
			qrCallbackLog.setErrorCode(ex.getStatusCode().toString());
			qrCallbackLog.setErrrMessage(ex.getMessage());
			throw ex; // Re-throw if needed
		} catch (Exception e) {
			LOG.error("An unexpected error occurred: {}", e.getMessage());
			// TODO save customer info API and matching customer is id exist or not result
			qrCallbackLog.setSuccess(false);
			qrCallbackLog.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			qrCallbackLog.setErrrMessage(e.getMessage());

			// into audit log table
			throw e;
		}

		qrCallbackLog.setLogDateTime(LocalDateTime.now());
		qRCallbackLogRepository.save(qrCallbackLog);
		return qrCallbackDto;
	}

}
