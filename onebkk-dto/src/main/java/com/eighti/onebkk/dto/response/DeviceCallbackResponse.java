package com.eighti.onebkk.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
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
