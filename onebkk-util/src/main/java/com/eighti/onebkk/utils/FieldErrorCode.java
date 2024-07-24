package com.eighti.onebkk.utils;

public enum FieldErrorCode {

	SUCCESS(200, "Success"), GENERAL(500, "General application exceptions occurred while processing user requests."),
	MULTIPLE_ERROR(400, "Bad request, invalid input values."),

	;

	private int code;
	private String message;

	private FieldErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
