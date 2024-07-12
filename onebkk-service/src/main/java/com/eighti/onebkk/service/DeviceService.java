package com.eighti.onebkk.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.eighti.onebkk.dto.DeviceDto;
import com.eighti.onebkk.dto.api.response.DeviceResponse;
import com.eighti.onebkk.entity.Device;
import com.eighti.onebkk.repository.DeviceRepository;
import com.eighti.onebkk.utils.CommonUtil;
import com.eighti.onebkk.utils.DateConstant;
import com.eighti.onebkk.utils.device.common.DeviceInterfaceAPIConstant;

import jakarta.transaction.Transactional;

@Service
public class DeviceService {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceService.class);

	private static final DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern(DateConstant.DATE_FORMAT_yyyy_mm_dd_hhmmss_a);

	private final DeviceRepository deviceRepository;
	private final RestTemplate restTemplate;

	public DeviceService(DeviceRepository deviceRepository, RestTemplate restTemplate) {
		this.deviceRepository = deviceRepository;
		this.restTemplate = restTemplate;
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
			dto.setLastHeartbeatTime(device.getLastHeartbeatTime().format(formatter));

			dataList.add(dto);
		});

		return dataList;
	}

	public void updateDeviceStatusOnlineOrOffline() {
		LOG.info("updateDeviceStatusOnlineOrOffline() >>> Executing start");
		List<Device> devices = deviceRepository.findAll();

		devices.forEach(device -> {
			checkFRDeviceIsOnlineOrOffline(device);
		});

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
				// TODO Update the device status based on isDeviceOline
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

}
