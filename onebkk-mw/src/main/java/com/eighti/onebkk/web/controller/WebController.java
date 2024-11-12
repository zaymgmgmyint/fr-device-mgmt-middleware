package com.eighti.onebkk.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

	@GetMapping("/")
	public String showLoginForm(Model model) {
		String message = "Innoflex Middleware is up and running...";
		return message;
	}
}
