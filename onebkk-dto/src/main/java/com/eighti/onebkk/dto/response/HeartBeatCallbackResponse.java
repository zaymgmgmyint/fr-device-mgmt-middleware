package com.eighti.onebkk.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartBeatCallbackResponse implements Serializable {

	private static final long serialVersionUID = 4064704496389956664L;
	private String deviceKey;
	private String time;
	private String ip;
	private String personCount;
	private String faceCount;

}
