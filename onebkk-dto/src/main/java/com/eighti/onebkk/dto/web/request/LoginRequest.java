package com.eighti.onebkk.dto.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

	@NotBlank(message = "Login name cannot be null")
	private String loginName;

	@NotBlank(message = "Login password cannot be null")
	private String password;

	private int authenticated;
}
