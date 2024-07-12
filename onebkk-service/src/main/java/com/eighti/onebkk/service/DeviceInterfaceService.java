package com.eighti.onebkk.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.eighti.onebkk.dto.PersonnelDto;
import com.eighti.onebkk.dto.api.response.DeviceCallbackResponse;
import com.eighti.onebkk.dto.api.response.IdentifyCallbackResponse;
import com.eighti.onebkk.dto.deviceinterface.PersonnelResponse;
import com.eighti.onebkk.entity.Device;
import com.eighti.onebkk.entity.InterfaceSetting;
import com.eighti.onebkk.repository.DeviceRepository;
import com.eighti.onebkk.repository.InterfaceSettingRepository;
import com.eighti.onebkk.utils.CommonUtil;
import com.eighti.onebkk.utils.device.common.DeviceInterfaceAPIConstant;
import com.eighti.onebkk.utils.device.common.DeviceInterfaceConstant;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DeviceInterfaceService {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceInterfaceService.class);

	@Value("${heartbeat.interval.second}")
	private String heartBeatIntervalSecond;

	private final RestTemplate restTemplate;

	private final DeviceRepository deviceRepository;

	private final InterfaceSettingRepository interfaceSettingRepository;

	public DeviceInterfaceService(RestTemplate restTemplate, DeviceRepository deviceRepository,
			InterfaceSettingRepository interfaceSettingRepository) {
		this.restTemplate = restTemplate;
		this.deviceRepository = deviceRepository;
		this.interfaceSettingRepository = interfaceSettingRepository;
	}

	/**
	 * Register the personnel data to the device interface
	 * 
	 * @param personnelDto the personnel DTO object
	 * @return the personnel object with response data (status, message)
	 * @throws Exception
	 */
	public PersonnelDto registerFaceScannerTerminal(PersonnelDto personnelDto) {
		LOG.info("registerFaceScannerTerminal() >>> Face scanner device: " + personnelDto.getDeviceName()
				+ ", User id: " + personnelDto.getUserId());
		try {

			// Prepare request body with JSON data
			Map<String, Object> requestBody = new HashMap<>();

			requestBody.put("pass", personnelDto.getDevicePassword());
			requestBody.put("person", createPersonJsonObject(personnelDto));

			if ((personnelDto.getType() == 1 || personnelDto.getType() == 3)
					&& CommonUtil.validString(personnelDto.getFaceId())) {
				requestBody.put("face1", createFaceJsonObject(personnelDto.getUserId(), personnelDto.getFaceId()));
			}

			String jsonBody = new ObjectMapper().writeValueAsString(requestBody);

			// Create request headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);

			// Create the POST request
			String url = personnelDto.getDeviceIp().concat(DeviceInterfaceAPIConstant.PERSON_CREATE);

			// Update response type to PersonnelResponse and handle response object
			LOG.info("registerFaceScannerTerminal() >>> Invoking face scanner terminal (register) ...");
			ResponseEntity<PersonnelResponse> response = restTemplate.postForEntity(url, entity,
					PersonnelResponse.class);

			// Process the response
			PersonnelResponse personnelResponse = response.getBody();
			if (response.getStatusCode() == HttpStatus.OK) {
				// Success scenario, access data from personnelResponse
				LOG.info("registerFaceScannerTerminal() >>> FR device register response: "
						+ personnelResponse.toString());
				LOG.info("Personnel register successfully: " + personnelResponse.getMsg());
			} else {
				// Error scenario, handle error based on status code and response body
				LOG.info("Error register personnel: " + response.getStatusCode());
			}

			// Print the response
			if (personnelResponse != null) {
				personnelDto.setResponseCode(personnelResponse.getCode());
				personnelDto.setResponseMessage(personnelResponse.getMsg());
				personnelDto.setDataSynced(personnelResponse.getSuccess() ? 1 : 0);

				LOG.info("Response status code: " + personnelResponse.getCode());
				LOG.info("Response message: " + personnelResponse.getMsg());
				LOG.info("Response status: " + personnelResponse.getSuccess());
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("registerFaceScannerTerminal() >>> ERROR: Face scanner device: " + personnelDto.getDeviceIp());
			LOG.error("registerFaceScannerTerminal() >>> ERROR: " + e.getMessage(), e);
		}

		return personnelDto;
	}

	/**
	 * Update the personnel data to the device interface
	 * 
	 * @param personnelDto the personnel DTO object
	 * @return the personnel object with response data (status, message)
	 */
	public PersonnelDto updateFaceScannerTerminal(PersonnelDto personnelDto) {
		LOG.info("updateFaceScannerTerminal() >>> Face scanner device: " + personnelDto.getDeviceName() + ", User id: "
				+ personnelDto.getUserId());
		try {

			// Prepare request body with JSON data
			Map<String, Object> requestBody = new HashMap<>();

			requestBody.put("pass", personnelDto.getDevicePassword());
			requestBody.put("person", createPersonJsonObject(personnelDto));

			if ((personnelDto.getType() == 1 || personnelDto.getType() == 3)
					&& CommonUtil.validString(personnelDto.getFaceId())) {
				requestBody.put("face1", createFaceJsonObject(personnelDto.getUserId(), personnelDto.getFaceId()));
			}

			String jsonBody = new ObjectMapper().writeValueAsString(requestBody);

			// Create request headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);

			// Create the POST request
			String url = personnelDto.getDeviceIp().concat(DeviceInterfaceAPIConstant.PERSON_UPDATE);

			// Update response type to PersonnelResponse and handle response object
			LOG.info("updateFaceScannerTerminal() >>> Invoking face scanner terminal (update) ...");
			ResponseEntity<PersonnelResponse> response = restTemplate.postForEntity(url, entity,
					PersonnelResponse.class);

			// Process the response
			PersonnelResponse personnelResponse = response.getBody();
			if (response.getStatusCode() == HttpStatus.OK) {
				// Success scenario, access data from personnelResponse
				LOG.info("updateFaceScannerTerminal() >>> FR device update response: " + personnelResponse.toString());
				System.out.println("Personnel updated successfully: " + personnelResponse.getMsg());
			} else {
				// Error scenario, handle error based on status code and response body
				System.out.println("Error updating personnel: " + response.getStatusCode());
			}

			// Print the response
			if (personnelResponse != null) {
				personnelDto.setResponseCode(personnelResponse.getCode());
				personnelDto.setResponseMessage(personnelResponse.getMsg());
				personnelDto.setDataSynced(personnelResponse.getSuccess() ? 1 : 0);

				LOG.info("Response status code: " + personnelResponse.getCode());
				LOG.info("Response message: " + personnelResponse.getMsg());
				LOG.info("Response status: " + personnelResponse.getSuccess());
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("updateFaceScannerTerminal() >>> ERROR: Face scanner device: " + personnelDto.getDeviceIp());
			LOG.error("updateFaceScannerTerminal() >>> ERROR: " + e.getMessage(), e);
		}

		return personnelDto;
	}

	/**
	 * Set identify callback URL to devices
	 * 
	 * @return all devices with set identify callback status
	 */
	public List<IdentifyCallbackResponse> setIdentifyCallBack() {
		List<IdentifyCallbackResponse> responseList = new ArrayList<IdentifyCallbackResponse>();
		IdentifyCallbackResponse identifyResponse = null;

		List<Device> deviceList = deviceRepository.findByStatus(1);

		Map<String, String> requestBody = new HashMap<String, String>();
		Optional<InterfaceSetting> interfaceSettingOptional = interfaceSettingRepository
				.getValueByCode(DeviceInterfaceConstant.IDENTIFY_CALLBACK);
		requestBody.put("callbackUrl",
				interfaceSettingOptional.isPresent() ? interfaceSettingOptional.get().getValue() : "");
		requestBody.put("base64Enable", "1");

		for (Device device : deviceList) {
			try {
				identifyResponse = new IdentifyCallbackResponse();

				requestBody.put("pass", device.getDevicePassword());

				// Prepare the request body
				String jsonBody = new ObjectMapper().writeValueAsString(requestBody);

				// Create request headers
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

				HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);

				// Create the POST request URL
				String url = device.getDeviceIp().concat(DeviceInterfaceAPIConstant.SET_IDENTIFY_CALLBACK);

				// Update response type to PersonnelResponse and handle response object
				LOG.info("setIdentifyCallBack() >>> Invoking face scanner terminal (setIdentifyCallBack) ...");
				ResponseEntity<IdentifyCallbackResponse> response = restTemplate.postForEntity(url, entity,
						IdentifyCallbackResponse.class);

				// Print the response
				if (response != null) {
					LOG.info("Response status code: " + response.getStatusCode());
					LOG.info("Response body: " + response.getBody().toString());

					identifyResponse = response.getBody();
					identifyResponse.setDeviceName(device.getDeviceName());
					identifyResponse.setSetIdenfityCallback(true);
				} else {
					identifyResponse.setDeviceName(device.getDeviceName());
					identifyResponse.setSetIdenfityCallback(false);
				}

			} catch (Exception e) {
				identifyResponse.setDeviceName(device.getDeviceName());
				identifyResponse.setSetIdenfityCallback(false);
				e.printStackTrace();
				LOG.error("setIdentifyCallBack() >>> ERROR: " + e.getMessage(), e);
			}

			responseList.add(identifyResponse);
		}

		return responseList;
	}

	public List<IdentifyCallbackResponse> setDeviceHeartBeat() {
		List<IdentifyCallbackResponse> responseList = new ArrayList<IdentifyCallbackResponse>();
		IdentifyCallbackResponse identifyResponse = null;

		List<Device> deviceList = deviceRepository.findByStatus(1);

		Map<String, String> requestBody = new HashMap<String, String>();

		Optional<InterfaceSetting> interfaceSettingOptional = interfaceSettingRepository
				.getValueByCode(DeviceInterfaceConstant.HEARTBEAT_CALLBACK);

		requestBody.put("url", interfaceSettingOptional.isPresent() ? interfaceSettingOptional.get().getValue() : "");
		requestBody.put("interval", heartBeatIntervalSecond);

		for (Device device : deviceList) {
			try {
				identifyResponse = new IdentifyCallbackResponse();

				requestBody.put("pass", device.getDevicePassword());

				// Prepare the request body
				String jsonBody = new ObjectMapper().writeValueAsString(requestBody);

				// Create request headers
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

				HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);

				// Create the POST request URL
				String url = device.getDeviceIp().concat(DeviceInterfaceAPIConstant.SET_HEATBEAT_CALLBACK);

				// Update response type to PersonnelResponse and handle response object
				LOG.info("setDeviceHeartBeat() >>> Invoking face scanner terminal (setDeviceHeartBeat) ...");
				ResponseEntity<IdentifyCallbackResponse> response = restTemplate.postForEntity(url, entity,
						IdentifyCallbackResponse.class);

				// Print the response
				if (response != null) {
					LOG.info("Response status code: " + response.getStatusCode());
					LOG.info("Response body: " + response.getBody().toString());

					identifyResponse = response.getBody();
					identifyResponse.setDeviceName(device.getDeviceName());
					identifyResponse.setSetDeviceHeartBeat(true);
				} else {
					identifyResponse.setDeviceName(device.getDeviceName());
					identifyResponse.setSetDeviceHeartBeat(false);
				}

			} catch (Exception e) {
				identifyResponse.setDeviceName(device.getDeviceName());
				identifyResponse.setSetDeviceHeartBeat(false);
				e.printStackTrace();
				LOG.error("setDeviceHeartBeat() >>> ERROR: " + e.getMessage(), e);

			}

			responseList.add(identifyResponse);
		}

		return responseList;
	}

	public List<DeviceCallbackResponse> getCallbackAddress() {
		List<DeviceCallbackResponse> heartBeatCallbackDataList = new ArrayList<DeviceCallbackResponse>();
		List<Device> deviceList = deviceRepository.findByStatus(1);

		Map<String, String> params = new HashMap<String, String>();

		DeviceCallbackResponse deviceCallbackResponse = null;
		for (Device device : deviceList) {
			try {
				deviceCallbackResponse = new DeviceCallbackResponse();
				params.put("pass", device.getDevicePassword());

				// Create request headers
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

				// Create the GET request URL
				String url = device.getDeviceIp().concat(DeviceInterfaceAPIConstant.GET_CALLBACK_ADDRESS);

				// Update response type to PersonnelResponse and handle response object
				LOG.info("getCallbackAddress() >>> Invoking face scanner terminal (getCallbackAddress) ...");
				ResponseEntity<DeviceCallbackResponse> response = restTemplate.getForEntity(url,
						DeviceCallbackResponse.class, params);

				// Print the response
				if (response != null) {
					LOG.info("getCallbackAddress() >>> Response status code: " + response.getStatusCode());
					LOG.info("getCallbackAddress() >>> Response body: " + response.getBody().toString());

					deviceCallbackResponse = response.getBody();
					heartBeatCallbackDataList.add(deviceCallbackResponse);
				} else {
					LOG.info("getCallbackAddress() >>> Response data is NULL");
				}

			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("getCallbackAddress() >>> ERROR: " + e.getMessage(), e);
			}

		}

		return heartBeatCallbackDataList;
	}
	
	private static Map<String, String> createPersonJsonObject(PersonnelDto p) {
		Map<String, String> person = new HashMap<>();

		person.put("id", p.getUserId());
		person.put("name", p.getName());
		person.put("idcardNum", p.getCardNoId());
		person.put("facePermission", "2");
		person.put("idCardPermission", "2");
		person.put("faceAndCardPermission", String.valueOf(p.getType() == 3 ? 2 : 1));
		person.put("faceAndQrCodePermission", "1");
		person.put("iDNumberPermission", "1");
		person.put("qrCode", "");
		person.put("tag", p.getTag());
		person.put("phone", p.getPhoneNo());
		person.put("password", "");
		person.put("passwordPermission", "1");
		person.put("fingerPermission", "1");
		person.put("qrCodePermission", "1");
		person.put("cardAndPasswordPermission", "1");
		return person;
	}

	private static Map<String, Object> createFaceJsonObject(String userId, String faceId) {
		Map<String, Object> face = new HashMap<>();
		face.put("faceId", userId);
		face.put("base64", faceId);
		return face;
	}
}
