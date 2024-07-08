package com.eighti.onebkk.request;

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
public class IdentifyCallbackRequest implements Serializable {

	private static final long serialVersionUID = -766381703735982709L;

	private String aliveType;
	private String deviceKey;
	private String idcardNum;
	private String identifyType;
	private String ip;
	private String model;
	private String personId;
	private String maskState;
	private String type;

	private Data data;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	class Data implements Serializable {

		private static final long serialVersionUID = -3184569770653654227L;

		String address;
		String id;
		String name;
	}
}
