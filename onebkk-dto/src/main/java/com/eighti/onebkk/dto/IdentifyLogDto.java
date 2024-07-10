package com.eighti.onebkk.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentifyLogDto implements Serializable {

	private static final long serialVersionUID = 7245373879498988551L;

	private long logId;
	private Boolean success;

	private String personId;
	private String personName;
	private String personPhoneNumber;

	private String deviceName;
	private String deviceKey;
	private String deviceIp;

	private String aliveType;
	private String aliveTypeDesc;
	private String comparisonType;
	private String comparisonTypeDesc;
	private String idCardNum;
	private String identifyType;
	private String identifyTypeDesc;
	private String identifyPattern;
	private String identifyPatternDesc;
	private String identifyDate;
	private String identifyDateTime;

}
