package com.eighti.onebkk.response;

import com.eighti.onebkk.utils.FieldErrorCode;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseStatus {

	private Integer responseCode;
	private String responseMessage;

	public ResponseStatus() {
		this.responseCode = FieldErrorCode.SUCCESS.getCode();
		this.responseMessage = FieldErrorCode.SUCCESS.getMessage();
	}

	public ResponseStatus(FieldErrorCode error) {
		this.responseCode = error.getCode();
		this.responseMessage = error.getMessage();
	}

}
