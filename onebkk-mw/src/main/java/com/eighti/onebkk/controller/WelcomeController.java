package com.eighti.onebkk.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

	@GetMapping("/")
	public String welcome() {
		LocalDate localDate = LocalDate.now();
		return """
				API is available and running upstream: %s
				""".formatted(localDate.toString());
	}
}
