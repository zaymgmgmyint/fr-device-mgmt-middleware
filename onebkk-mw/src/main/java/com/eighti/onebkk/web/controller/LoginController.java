package com.eighti.onebkk.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.eighti.onebkk.dto.web.request.LoginRequest;
import com.eighti.onebkk.service.AdminService;

import jakarta.validation.Valid;

@Controller
public class LoginController {

	private final static Logger LOG = LoggerFactory.getLogger(LoginController.class);

	private final AdminService adminService;

	public LoginController(AdminService adminService) {
		this.adminService = adminService;
	}

	// User login method
	@PostMapping("/login")
	public String userLogin(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest, BindingResult result,
			Model model) {
		LOG.info("userLogin() >>> Login request: " + loginRequest.toString());

		if (result.hasErrors()) {
			model.addAttribute("loginRequest", loginRequest);
			result.getAllErrors().forEach(error -> LOG.error("Validation error: " + error.toString()));

			return "index";
		}

		// TODO check login authentication
		boolean isAuthenticated = adminService.checkAdminAuthentication(loginRequest.getLoginName(),
				loginRequest.getPassword().trim());

		if (!isAuthenticated) {
			loginRequest.setAuthenticated(0);
			model.addAttribute("loginRequest", loginRequest);

			return "index";
		}

		return "redirect:/home";
	}

	@GetMapping("/logout")
	public String logout() {
		LOG.info("logout() >>> Logout!");
		return "redirect:/";
	}
}
