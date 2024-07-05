package com.eighti.onebkk.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonnelDto implements Serializable {

	private static final long serialVersionUID = -1157366381625060325L;

	private Long id;

	private String userId;

	private String name;

	private String faceId;

	private String cardNoId;

	private String phoneNo;

	private String address;

	private String tag;

	private String permissionType;

	private Integer dataSynced;

	private LocalDateTime syncedDatetime;

	private Integer type;

	private String responseCode;

	private String responseMessage;

	// Device data
	private String deviceName;
	private String deviceIp;
	private String devicePassword;
}
