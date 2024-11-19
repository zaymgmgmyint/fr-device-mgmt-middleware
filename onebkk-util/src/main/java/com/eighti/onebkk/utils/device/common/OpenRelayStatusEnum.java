package com.eighti.onebkk.utils.device.common;

import com.eighti.onebkk.utils.CommonUtil;

public enum OpenRelayStatusEnum {

	NOT_OPEN(0, "Not Open"), OPEN(1, "Open");

	private int code;
	private String desc;

	private OpenRelayStatusEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(Integer code) {
		if (!CommonUtil.validNumber(code)) {
			return "";
		}
		for (OpenRelayStatusEnum s : values()) {
			if (s.getCode() == code) {
				return s.getDesc();
			}
		}
		return "";
	}

}
