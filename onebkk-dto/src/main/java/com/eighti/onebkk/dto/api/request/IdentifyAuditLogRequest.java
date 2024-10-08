package com.eighti.onebkk.dto.api.request;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentifyAuditLogRequest implements Serializable {

	private static final long serialVersionUID = 564500753769139428L;

	private String authType; // Face, Card, QR, and Finger print

	private int authSuccessType; // 0 is All, 1 is Success, and 2 is Failure

	private String fromDate;

	private String toDate;

	private String role;

	private String company;

	private List<String> deviceKeys;
	
	private List<String> deviceIps;

	private Integer page;
}
