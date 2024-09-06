package com.eighti.onebkk.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.eighti.onebkk.dto.PaginatedResponse;
import com.eighti.onebkk.entity.IdentifyLog;

public interface IdentifyLogCustomRepository {

	PaginatedResponse<IdentifyLog> searchIdentifyLogs(LocalDateTime fromDate, LocalDateTime toDate, String role,
			String company, List<String> deviceKeys, List<String> models, List<String> identifyTypes, List<String> deviceIps, Integer page);

}
