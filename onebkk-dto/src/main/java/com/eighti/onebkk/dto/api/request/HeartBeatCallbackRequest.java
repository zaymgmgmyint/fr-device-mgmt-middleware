package com.eighti.onebkk.dto.api.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeartBeatCallbackRequest implements Serializable {

	private static final long serialVersionUID = 2930964900713639499L;

	private String deviceKey;
	private String time;
	private String ip;
	private String personCount;
	private String faceCount;
	private String fingerCount;
	private String version;
	private String freeDiskSpace;
	private String cpuUsageRate;
	private String cpuTemperature;
	private String memoryUsageRate;
	private String deviceName;
	private String SDKVersion;
	private String companyName;

}
