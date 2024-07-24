package com.eighti.onebkk.utils.device.common;

import lombok.Getter;

@Getter
public enum DeviceResponseStatus {

	SUCCESS(1, "Success"), FAIL(0, "Fail");

	private int code;
	private String desc;

	private DeviceResponseStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
