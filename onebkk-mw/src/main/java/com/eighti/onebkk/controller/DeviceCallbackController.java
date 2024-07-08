package com.eighti.onebkk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eighti.onebkk.request.HeartBeatCallbackRequest;
import com.eighti.onebkk.request.IdentifyCallbackRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/lan")
@Tag(name = "Device CallBack APIs")
public class DeviceCallbackController {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceMgmtInterfaceController.class);

	@PostMapping("/identifyCallback")
	public String identifyCallBack(@RequestBody IdentifyCallbackRequest requestData, HttpServletRequest httpRequest) {
		LOG.info("identifyCallBack() >>> Request Data: " + requestData.toString());
		return "Success";
	}

	@PostMapping("/heartBeatCallback")
	public String heartBeatCallback(@RequestBody HeartBeatCallbackRequest requestData, HttpServletRequest httpRequest) {
		LOG.info("heartBeatCallback() >>> Request Data: " + requestData.toString());
		return "Success";
	}
}
