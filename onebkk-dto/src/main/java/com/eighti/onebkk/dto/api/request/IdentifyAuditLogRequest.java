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

	private String fromDate;

	private String toDate;

	private List<String> deviceKeys;

}
