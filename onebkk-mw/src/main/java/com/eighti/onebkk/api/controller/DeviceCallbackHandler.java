package com.eighti.onebkk.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighti.onebkk.dto.api.request.HeartBeatCallbackRequest;
import com.eighti.onebkk.dto.api.request.IdentifyCallbackRequest;
import com.eighti.onebkk.dto.api.request.QRCallbackRequest;
import com.eighti.onebkk.dto.api.response.QRCallbackHandlerResponse;
import com.eighti.onebkk.response.DeviceCallbackResponse;
import com.eighti.onebkk.service.DeviceCallbackService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/lan")
@Hidden
@Tag(name = "Device CallBack Handler APIs")
public class DeviceCallbackHandler {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceManagementInterfaceApi.class);

	private final DeviceCallbackService callbackService;

	public DeviceCallbackHandler(DeviceCallbackService callbackService) {
		this.callbackService = callbackService;
	}

	// Identify callback handler
	@PostMapping("/identifyCallback")
	public DeviceCallbackResponse identifyCallBack(@RequestBody IdentifyCallbackRequest requestData,
			HttpServletRequest httpRequest) {
		LOG.info("identifyCallBack() >>> Request Data: " + requestData.toString());
		DeviceCallbackResponse resposne = new DeviceCallbackResponse(2, false);
		try {
			callbackService.saveIdentifyCallbackLog(requestData);
			resposne = new DeviceCallbackResponse(1, true);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("identifyCallBack() >>> ERROR: " + e.getMessage(), e);
		}

		return resposne;
	}

	// Device heart beat callback handler
	@PostMapping("/heartBeatCallback")
	public DeviceCallbackResponse heartBeatCallback(@RequestBody HeartBeatCallbackRequest requestData,
			HttpServletRequest httpRequest) {
		LOG.info("heartBeatCallback() >>> Request Data: " + requestData.toString());
		DeviceCallbackResponse response = new DeviceCallbackResponse(2, false);
		try {
			callbackService.saveHeartBeatCallbackLog(requestData);
			response = new DeviceCallbackResponse(1, true);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("heartBeatCallback() >>> ERROR: " + e.getMessage(), e);
		}
		return response;
	}

	// TODO Device QR callback handler
	@PostMapping("/qrCallback")
	public QRCallbackHandlerResponse qrCallback(@RequestBody QRCallbackRequest requestData,
			HttpServletRequest httpRequest) {
		LOG.info("qrCallback() >>> Request Data: " + requestData.toString());
		QRCallbackHandlerResponse response = new QRCallbackHandlerResponse("Valid Access!", 1, "123456");

		try {
			callbackService.processQRRequest(requestData);
		} catch (Exception e) {
			LOG.error("qrCallback() >>> ERROR: {}", e.getMessage(), e);
		}

		return response;
	}
}
