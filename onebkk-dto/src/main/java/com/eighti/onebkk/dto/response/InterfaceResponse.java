package com.eighti.onebkk.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterfaceResponse<T> implements Serializable {

	private static final long serialVersionUID = 1595495401930431266L;

	private Integer result;

	private Boolean success;

	private String msg;

	private String code;

	private T data;
}
