package com.eighti.onebkk.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighti.onebkk.dto.api.response.DeviceCallbackResponse;
import com.eighti.onebkk.dto.api.response.IdentifyCallbackResponse;
import com.eighti.onebkk.response.Response;
import com.eighti.onebkk.service.DeviceInterfaceService;
import com.eighti.onebkk.utils.FieldErrorCode;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/device/mgmt")
@Tag(name = "Device Management Interface APIs")
public class DeviceManagementInterfaceApi {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceManagementInterfaceApi.class);

	private final DeviceInterfaceService deviceInterfaceService;

	public DeviceManagementInterfaceApi(DeviceInterfaceService deviceInterfaceService) {
		this.deviceInterfaceService = deviceInterfaceService;
	}

	// TODO Set device password

	// TODO Delete personnel list

	// TODO Configure the device QR callback
	@GetMapping("/setDeviceQRCallback")
	public Response<String> setDeviceQRCallback() {
		Response<String> response = new Response<String>();
		try {
			response.setData("Successfully set the device QR callback");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("setIdentifyCallback() >>> Excpetion occured while set identify callback: ", e.getMessage(), e);
			response = new Response<>(FieldErrorCode.GENERAL);
		}

		return response;
	}

	// TODO Configure identify callback
	@GetMapping("/setIdentifyCallBack")
	public Response<List<IdentifyCallbackResponse>> setIdentifyCallback() {
		Response<List<IdentifyCallbackResponse>> response = new Response<List<IdentifyCallbackResponse>>();

		try {
			List<IdentifyCallbackResponse> identifyCallbackResponses = deviceInterfaceService.setIdentifyCallBack();
			response.setData(identifyCallbackResponses);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("setIdentifyCallback() >>> Excpetion occured while set identify callback: ", e.getMessage(), e);
			response = new Response<>(FieldErrorCode.GENERAL);
		}

		return response;
	}

	// TODO Configure heart beat callback
	@GetMapping("/setDeviceHeartBeat")
	public Response<List<IdentifyCallbackResponse>> setDeviceHeartBeat() {
		Response<List<IdentifyCallbackResponse>> response = new Response<List<IdentifyCallbackResponse>>();

		try {
			List<IdentifyCallbackResponse> identifyCallbackResponses = deviceInterfaceService.setDeviceHeartBeat();
			response.setData(identifyCallbackResponses);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("setIdentifyCallback() >>> Excpetion occured while set hearbeat callback: ", e.getMessage(), e);
			response = new Response<>(FieldErrorCode.GENERAL);
		}

		return response;
	}

	// Get the callback address
	@GetMapping("/device/getCallback")
	public Response<List<DeviceCallbackResponse>> getCallbackAddress() {
		Response<List<DeviceCallbackResponse>> response = new Response<List<DeviceCallbackResponse>>();

		try {
			List<DeviceCallbackResponse> heartBeatCallbacks = deviceInterfaceService.getCallbackAddress();
			response.setData(heartBeatCallbacks);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("getCallbackAddress() >>> Excpetion occured while getting callback address: ", e.getMessage(), e);
			response = new Response<>(FieldErrorCode.GENERAL);
		}

		return response;
	}

}
