package com.eighti.onebkk.utils;

import lombok.Getter;

@Getter
public enum CommonStatus {

	ACTIVE(1, "Active"), INACTIVE(2, "Inactive");

	private int code;
	private String desc;

	private CommonStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
