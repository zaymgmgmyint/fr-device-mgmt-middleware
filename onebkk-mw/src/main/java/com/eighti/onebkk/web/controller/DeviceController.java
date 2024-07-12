package com.eighti.onebkk.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.eighti.onebkk.service.DeviceService;

@Controller
public class DeviceController {

	private final static Logger LOG = LoggerFactory.getLogger(LoginController.class);

	private final DeviceService deviceService;

	public DeviceController(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	@GetMapping("/home")
	public String home(Model model) {

		try {
			// Get the device list
			model.addAttribute("devices", deviceService.getDevices());
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("home() >>> ERROR: " + e.getMessage(), e);
		}

		return "home";
	}

}
