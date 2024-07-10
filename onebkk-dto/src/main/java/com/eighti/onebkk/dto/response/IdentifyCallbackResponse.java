package com.eighti.onebkk.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentifyCallbackResponse implements Serializable {

	private static final long serialVersionUID = -8007229939083179088L;

	private String deviceName;

	private boolean setIdenfityCallback;

	private boolean setDeviceHeartBeat;

	private String code;
	private String data;
	private String msg;
	private Integer result;
	private String success;

}
