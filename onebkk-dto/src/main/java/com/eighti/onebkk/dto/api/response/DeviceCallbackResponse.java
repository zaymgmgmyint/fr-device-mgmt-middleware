package com.eighti.onebkk.dto.api.response;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeExclude;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceCallbackResponse implements Serializable {

	private static final long serialVersionUID = -3250151259587832628L;

	private Integer result;
	private Boolean success;
	private String msg;
	private String code;
	private Data data;
	
	private String deviceName;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ToString
	public class Data implements Serializable {

		private static final long serialVersionUID = -7172085486988836882L;

		String heartBeatCallback;
		String identifyCallback;
		String imgRegCallback;
		String eventCallback;
		String QRCodeCallback;
		String fingerRegCallback;
		String cardRegCallback;

	}

}
