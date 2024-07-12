package com.eighti.onebkk.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.eighti.onebkk.dto.web.request.LoginRequest;

@Controller
public class WebController {

	@GetMapping("/")
	public String showLoginForm(Model model) {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setAuthenticated(1);
		model.addAttribute("loginRequest", loginRequest);
		return "index";
	}

	@GetMapping("/error")
	public String showError() {
		return "error";
	}
}
