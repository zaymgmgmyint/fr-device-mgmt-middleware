package com.eighti.onebkk.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@Data
public class DeviceCallbackResponse {

	private Integer result;
	private Boolean success;

	public DeviceCallbackResponse(int result, boolean success) {
		this.result = result;
		this.success = success;
	}
}
