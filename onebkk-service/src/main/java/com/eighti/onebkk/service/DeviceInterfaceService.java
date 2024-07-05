package com.eighti.onebkk.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eighti.onebkk.dto.PersonnelDto;
import com.eighti.onebkk.dto.deviceinterface.PersonnelResponse;
import com.eighti.onebkk.utils.CommonUtil;
import com.eighti.onebkk.utils.device.common.DeviceInterfaceAPIConstant;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DeviceInterfaceService {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceInterfaceService.class);

	public PersonnelDto registerFaceScannerTerminal(PersonnelDto personnelDto) {
		LOG.info("registerFaceScannerTerminal() >>> Face scanner device: " + personnelDto.getDeviceName()
				+ ", User id: " + personnelDto.getUserId());
		try {

			// Create a HttpClient instance
			HttpClient httpClient = HttpClient.newHttpClient();

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
//			Map<String, String> headersMap = new HashMap<>();
//			headersMap.put("Content-Type", "application/x-www-form-urlencoded");

			// Create the POST request
			HttpRequest request = HttpRequest.newBuilder()
					.uri(new URI(personnelDto.getDeviceIp().concat(DeviceInterfaceAPIConstant.PERSON_CREATE)))
					.headers("Content-Type", "application/x-www-form-urlencoded")
					.POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();

			LOG.info("registerFaceScannerTerminal() >>> Invoking face scanner terminal (register) ...");
			// Send the request and get the response
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			LOG.info("registerFaceScannerTerminal() >>> FR device register response: " + response.toString());

			// Deserialize response body into PersonnelResponse
			ObjectMapper objectMapper = new ObjectMapper();
			PersonnelResponse personnelResponse = objectMapper.readValue(response.body(), PersonnelResponse.class);

			// Print the response
			if (response != null) {
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

	public PersonnelDto updateFaceScannerTerminal(PersonnelDto personnelDto) {
		LOG.info("updateFaceScannerTerminal() >>> Face scanner device: " + personnelDto.getDeviceName() + ", User id: "
				+ personnelDto.getUserId());
		try {

			// Create a HttpClient instance
			HttpClient httpClient = HttpClient.newHttpClient();

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
//			Map<String, String> headersMap = new HashMap<>();
//			headersMap.put("Content-Type", "application/x-www-form-urlencoded");

			// Create the POST request
			HttpRequest request = HttpRequest.newBuilder()
					.uri(new URI(personnelDto.getDeviceIp().concat(DeviceInterfaceAPIConstant.PERSON_UPDATE)))
					.headers("Content-Type", "application/x-www-form-urlencoded")
					.POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();

			LOG.info("updateFaceScannerTerminal() >>> Invoking face scanner terminal (update) ...");
			// Send the request and get the response
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			LOG.info("registerFaceScannerTerminal() >>> FR device update response: " + response.toString());

			// Deserialize response body into PersonnelResponse
			ObjectMapper objectMapper = new ObjectMapper();
			PersonnelResponse personnelResponse = objectMapper.readValue(response.body(), PersonnelResponse.class);

			// Print the response
			if (response != null) {
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
