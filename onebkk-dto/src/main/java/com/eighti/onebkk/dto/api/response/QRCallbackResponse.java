package com.eighti.onebkk.dto.api.response;

import java.io.Serializable;

import com.eighti.onebkk.utils.device.common.OpenRelayStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class QRCallbackResponse implements Serializable {

	private static final long serialVersionUID = -7348122194807487728L;

	@JsonProperty(value = "displayModContent")
	private String displayModContent;

	@JsonProperty(value = "isOpenRelay")
	private int isOpenRelay; // Whether the relay opens the door 1 Open, others Not open

	@JsonProperty(value = "cardNo")
	private String cardNo = "1234";

	public QRCallbackResponse() {
		this.displayModContent = "Invalid Access";
		this.isOpenRelay = OpenRelayStatusEnum.NOT_OPEN.getCode();
	}
}
