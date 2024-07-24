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
	private Integer authSuccessType;
	
	private String authType;
	private String authTypeDesc;

	private String userId;
	private String firstName;
	private String lastName;
	private String userRole;
	private String userCompany;
	private String userBaseLocation;

	private String deviceName;
	private String deviceKey;
	private String deviceIp;

	private String idCardNum;
	private String identifyTypeDesc;
	private String identifyPatternDesc;
	private String identifyDateTime;
	
	private String identifyTimestamp;

}
