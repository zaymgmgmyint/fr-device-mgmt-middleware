package com.eighti.onebkk.dto.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes({ @Type(value = Data.class, name = "data") })
public class IdentifyCallbackRequest implements Serializable {

	private static final long serialVersionUID = -766381703735982709L;

	private String aliveType;
	private String base64;
	private String deviceKey;
	private String idcardNum;
	private String identifyType;
	private String ip;
	private String model;
	private String passTimeType;
	private String path;
	private String permissionTimeType;
	private String personId;
	private Integer recType;
	private String time;
	private Integer dstOffset;
	private Integer passbackTriggerType;
	private Integer maskState;
	private String workCode;
	private String attendance;
	private String type;

	private Data data;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class Data implements Serializable {

		private static final long serialVersionUID = -3184569770653654227L;

		String address;
		String birthday;
		String compareResult;
		String createTime;
		String id;
		String idNum;
		String issuingOrgan;
		String name;
		String nation;
		String sex;
		String usefulLife;
	}
}
