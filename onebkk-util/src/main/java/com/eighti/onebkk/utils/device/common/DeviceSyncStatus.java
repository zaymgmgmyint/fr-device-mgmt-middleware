package com.eighti.onebkk.utils.device.common;

import lombok.Getter;

@Getter
public enum DeviceSyncStatus {

	SYNC(1, "Sync"), UNSYNC(2, "Unsync");

	private int code;
	private String desc;

	private DeviceSyncStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
