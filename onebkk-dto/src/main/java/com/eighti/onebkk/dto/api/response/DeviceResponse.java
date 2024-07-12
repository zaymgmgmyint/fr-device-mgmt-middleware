package com.eighti.onebkk.dto.api.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceResponse implements Serializable {

	private static final long serialVersionUID = -2515912524688360940L;

	private Integer result;
	private Boolean success;
	private String msg;
	private String code;
	private String data;

}
