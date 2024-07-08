package com.eighti.onebkk.response;

import java.util.List;

import org.springframework.validation.FieldError;

import com.eighti.onebkk.utils.CommonUtil;
import com.eighti.onebkk.utils.FieldErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@Data
public class Response<T> {

	private ResponseStatus status;
	private T data;
	private List<FieldError> error;

	public Response() {
		super();
		status = new ResponseStatus();
	}

	public Response(FieldErrorCode error) {
		super();
		status = new ResponseStatus(error);
	}

	public Response(List<FieldError> error) {
		status = new ResponseStatus(FieldErrorCode.MULTIPLE_ERROR);
		if (CommonUtil.validCollection(error)) {
			this.error = error;
		}
	}
}
