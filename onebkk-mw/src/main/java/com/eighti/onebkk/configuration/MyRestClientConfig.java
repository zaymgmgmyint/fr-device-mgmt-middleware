package com.eighti.onebkk.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyRestClientConfig {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
