package com.eighti.onebkk.utils;

public enum FieldErrorCode {

	SUCCESS(200, "Success"), GENERAL(500, "General application exception occurred while processing user request."),
	FAIL(-1, ""), MULTIPLE_ERROR(1000, "Invalid input values."),

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
