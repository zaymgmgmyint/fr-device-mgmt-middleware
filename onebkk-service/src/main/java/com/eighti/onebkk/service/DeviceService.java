package com.eighti.onebkk.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.eighti.onebkk.dto.DeviceDto;
import com.eighti.onebkk.dto.api.response.DeviceResponse;
import com.eighti.onebkk.dto.api.response.IdentifyLogResponse;
import com.eighti.onebkk.entity.Device;
import com.eighti.onebkk.entity.IdentifyLog;
import com.eighti.onebkk.repository.DeviceRepository;
import com.eighti.onebkk.repository.IdentifyLogRepository;
import com.eighti.onebkk.utils.CommonStatus;
import com.eighti.onebkk.utils.CommonUtil;
import com.eighti.onebkk.utils.DateConstant;
import com.eighti.onebkk.utils.device.common.DeviceInterfaceAPIConstant;

import jakarta.transaction.Transactional;

@Service
public class DeviceService {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceService.class);

	private static final DateTimeFormatter formatter1 = DateTimeFormatter
			.ofPattern(DateConstant.DATE_FORMAT_yyyy_mm_dd_hhmmss_a);

	private static final DateTimeFormatter formatter2 = DateTimeFormatter
			.ofPattern(DateConstant.DATE_FORMAT_yyyy_mm_dd_HHmmss);

	private final DeviceRepository deviceRepository;
	private final RestTemplate restTemplate;
	private IdentifyLogRepository identifyLogRepository;

	public DeviceService(DeviceRepository deviceRepository, RestTemplate restTemplate,
			IdentifyLogRepository identifyLogRepository) {
		this.deviceRepository = deviceRepository;
		this.restTemplate = restTemplate;
		this.identifyLogRepository = identifyLogRepository;
	}

	public List<DeviceDto> getDevices() {
		LOG.info("getDevices() >>> Getting device list...");
		List<DeviceDto> dataList = new ArrayList<DeviceDto>();

		List<Device> devices = deviceRepository.findAll(Sort.by("id").ascending());
		devices.forEach(device -> {
			DeviceDto dto = new DeviceDto();
			dto.setId(device.getId());
			dto.setDeviceName(device.getDeviceName());
			dto.setDeviceKey(device.getDeviceKey());
			dto.setDeviceIp(device.getDeviceIp());
			dto.setDeviceStatus(device.getDeviceStatus());
			dto.setLastHeartbeatTime(
					device.getLastHeartbeatTime() != null ? device.getLastHeartbeatTime().format(formatter1) : "");

			dataList.add(dto);
		});

		return dataList;
	}

	public void updateDeviceStatusOnlineOrOffline() {
		LOG.info("updateDeviceStatusOnlineOrOffline() >>> Executing start");
		List<Device> devices = deviceRepository.findAll();

		devices.stream().filter(device -> device.getStatus() == CommonStatus.ACTIVE.getCode()).forEach(device -> {
			checkFRDeviceIsOnlineOrOffline(device);
		});
	}

	// TODO Get identify log from devices
	public void fetchIdentifyLogFromDevices() {
		LOG.info("fetchIdentifyLogFromDevices() >>> START");

		List<Device> devices = deviceRepository.findAll();

		devices.stream().filter(device -> device.getStatus() == CommonStatus.ACTIVE.getCode()).forEach(device -> {
			fetchIdentifyLogFromDevice(device);
		});

		LOG.info("fetchIdentifyLogFromDevices() >>> END");
	}

	// Prepare the query parameters
	private void fetchIdentifyLogFromDevice(Device device) {
		try {

			// Set the startTime to the lastEndTime or 1 minute ago if it's the first run
			LocalDateTime startTime = (device.getLastFetchTime() != null) ? device.getLastFetchTime().minusSeconds(5)
					: LocalDateTime.now().minusMinutes(1);

			// Set the endTime to the current time
			LocalDateTime endTime = LocalDateTime.now();

			// Convert times to the desired format
			String formattedStartTime = startTime.format(formatter2);
			String formattedEndTime = endTime.format(formatter2);

			// Update the device last fetch time
			device.setLastFetchTime(endTime);
			deviceRepository.save(device);
			LOG.info("fetchIdentifyLogFromDevice() >>> Update device last fetch time: " + formattedStartTime);

			// Prepare the identification record URL with query parameters
			String url = device.getDeviceIp().concat(DeviceInterfaceAPIConstant.IDNETIFY_LOG_URL);
			String pass = device.getDevicePassword();
			String personId = "-1";
			String length = "1000";
			String model = "-1";
			String index = "0";

			String fullUrl = String.format("%s?pass=%s&personId=%s&startTime=%s&endTime=%s&length=%s&model=%s&index=%s",
					url, pass, personId, formattedStartTime, formattedEndTime, length, model, index);
			LOG.info("fetchIdentifyLogFromDevice() >>> Identify record query url: " + fullUrl);

			// Make the API call
			fetchIdentifyLogs(fullUrl, device.getDeviceKey());

		} catch (Exception e) {
			LOG.error("fetchIdentifyLogFromDevice() >>> Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Async("identifyLogExecutor")
	@Transactional
	private void fetchIdentifyLogs(String fullUrl, String deviceKey) {
		try {
			LOG.info("fetchIdentifyLogs() >>> Fetching identify record...");

			// Setup the HTTP headers
			HttpHeaders httpHeaders = new HttpHeaders();

			// Create HTTP entity object
			HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);

			// Make the GET request
			ResponseEntity<IdentifyLogResponse> response = restTemplate.exchange(fullUrl, HttpMethod.GET, entity,
					IdentifyLogResponse.class);

			if (response != null) {

				LOG.info("fetchIdentifyLogs() >>> Response status code: " + response.getStatusCode());
				// Get the response body
				IdentifyLogResponse responseBody = response.getBody();

				LOG.info("fetchIdentifyLogs() >>> Response body: " + responseBody.toString());

				if (responseBody.getData() != null && responseBody.getData().getRecords() != null) {
					responseBody.getData().getRecords().forEach(log -> {

						IdentifyLog identifyLog = new IdentifyLog();
						identifyLog.setAliveType(log.getAliveType() != null ? log.getAliveType().toString() : "");
						// identifyLog.setBase64(callbackData.getBase64());
						identifyLog.setDeviceKey(deviceKey);
						identifyLog.setIdcardNum(log.getIdcardNum());
						identifyLog
								.setIdentifyType(log.getIdentifyType() != null ? log.getIdentifyType().toString() : "");
						// identifyLog.setIp(log.getIp());
						identifyLog.setModel(log.getModel() != null ? log.getModel().toString() : "");
						identifyLog
								.setPassTimeType(log.getPassTimeType() != null ? log.getPassTimeType().toString() : "");
						identifyLog.setPath(log.getPath());
						identifyLog.setPermissionTimeType(
								log.getPermissionTimeType() != null ? log.getPermissionTimeType().toString() : "");
						identifyLog.setPersonId(log.getPersonId());
						identifyLog.setRecType(log.getRecType());
						identifyLog.setTime(log.getTime() != null ? log.getTime().toString() : "");
						identifyLog.setDstOffset(log.getDstOffset());
						identifyLog.setPassbackTriggerType(
								log.getPassbackTriggerType() != null ? Integer.valueOf(log.getPassbackTriggerType())
										: null);
						identifyLog.setMaskState(log.getMaskState());
						identifyLog.setWorkCode(log.getWorkCode());
						identifyLog.setAttendance(log.getAttendance());
						identifyLog.setType(log.getType() != null ? log.getType().toString() : "");
						identifyLog.setLogDateTime(LocalDateTime.now());

						identifyLog.setLogTime(CommonUtil.validLong(log.getTime())
								? CommonUtil.convertMilliSecondToLocalDate(log.getTime().toString())
								: LocalDate.now());

						identifyLog.setLogDateTime(CommonUtil.validLong(log.getTime())
								? CommonUtil.convertMilliSecondToLocalDateTime(log.getTime().toString())
								: LocalDateTime.now());

						identifyLogRepository.save(identifyLog);

					});
				}

			} else {
				LOG.info("fetchIdentifyLogs() >>> Response data is NULL");
			}

		} catch (RestClientException e) {
			LOG.error("fetchIdentifyLogs() >>> RestClientException: " + e.getMessage(), e);
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error("fetchIdentifyLogs() >>> Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Async("statusExecutor")
	@Transactional
	private void checkFRDeviceIsOnlineOrOffline(Device device) {
		try {
			// Create request headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			// Create the GET request URL
			String url = device.getDeviceIp().concat(DeviceInterfaceAPIConstant.GET_DEVICE_KEY);

			// Handle response object
			LOG.info("checkFRDeviceIsOnlineOrOffline() >>> Invoking face scanner terminal (getDeviceKey) ...");
			ResponseEntity<DeviceResponse> response = restTemplate.getForEntity(url, DeviceResponse.class);

			// Print the response
			if (response != null) {
				LOG.info("checkFRDeviceIsOnlineOrOffline() >>> Response status code: " + response.getStatusCode());
				LOG.info("checkFRDeviceIsOnlineOrOffline() >>> Response body: " + response.getBody().toString());
				DeviceResponse deviceResposne = response.getBody();
				// Update the device status based on isDeviceOline
				if (deviceResposne.getSuccess() && !CommonUtil.isEmpty(deviceResposne.getData())) {
					device.setDeviceStatus(1);
				} else {
					device.setDeviceStatus(0);
				}

			} else {
				LOG.info("checkFRDeviceIsOnlineOrOffline() >>> Response data is NULL");
				device.setDeviceStatus(0);
			}
		} catch (RestClientException e) {
			LOG.error("checkFRDeviceIsOnlineOrOffline() >>> RestClientException: " + e.getMessage(), e);
			device.setDeviceStatus(0);
		} catch (Exception e) {
			LOG.error("checkFRDeviceIsOnlineOrOffline() >>> Exception: " + e.getMessage(), e);
			device.setDeviceStatus(0);
		}

		deviceRepository.save(device);
	}

	private static String formatTime(long milliseconds) {
		long seconds = milliseconds / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;

		return String.format("%02d:%02d:%02d.%03d", hours, minutes % 60, seconds % 60, milliseconds % 1000);
	}

}
