package com.eighti.onebkk.dto.api.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class QRCallbackRequest implements Serializable {

	private static final long serialVersionUID = -4770347429993156429L;

	@JsonProperty(value = "deviceKey")
	private String deviceKey;

	@JsonProperty(value = "time")
	private String time;

	@JsonProperty(value = "ip")
	private String ip;

	@JsonProperty(value = "QRdata")
	private String QRdata;

}
