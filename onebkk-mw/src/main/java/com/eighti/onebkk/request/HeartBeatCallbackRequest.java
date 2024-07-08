package com.eighti.onebkk.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartBeatCallbackRequest implements Serializable {

	private static final long serialVersionUID = 2930964900713639499L;

	private String deviceKey;
	private String time;
	private String ip;
	private String personCount;
	private String faceCount;

}
