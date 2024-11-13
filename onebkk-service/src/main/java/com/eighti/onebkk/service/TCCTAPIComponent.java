package com.eighti.onebkk.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
public class TCCTAPIComponent {

	@Value("${auth.api.url}")
	public String authUrl;

	@Value("${auth.api.key.id}")
	public String authKeyId;

	@Value("${auth.api.key.secret}")
	public String authKeySecret;
	
	@Value("${customer.api.url}")
	public String customerUrl;
	
	@Value("${rsa.private.key}")
	public String rsaPrivateKey;

}
