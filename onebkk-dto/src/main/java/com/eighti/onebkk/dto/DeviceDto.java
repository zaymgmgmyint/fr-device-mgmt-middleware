package com.eighti.onebkk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

	private long id;

	private String deviceName;

	private String deviceKey;

	private String deviceIp;

	private int deviceStatus;

	private String lastHeartbeatTime;

}
