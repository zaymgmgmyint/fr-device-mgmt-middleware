package com.eighti.onebkk.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eighti.onebkk.entity.IdentifyLog;

public interface IdentifyLogRepository extends JpaRepository<IdentifyLog, Long> {

	@Query(value = "SELECT * FROM identify_log log WHERE log.log_datetime >= :fromDate AND log.log_datetime <= :toDate AND log.model IN (:models) AND log.identify_type In (:identifyTypes) ORDER BY id DESC", nativeQuery = true)
	List<IdentifyLog> findByLogDateTimeBetween(@Param("fromDate") LocalDateTime fromDate,
			@Param("toDate") LocalDateTime toDate, @Param("models") List<String> models,
			@Param("identifyTypes") List<String> identifyTypes);

	@Query(value = "SELECT * FROM identify_log log WHERE log.log_datetime >= :fromDate AND log.log_datetime <= :toDate AND log.device_key IN (:deviceKeys) AND log.model IN (:models) AND log.identify_type In (:identifyTypes) ORDER BY id DESC", nativeQuery = true)
	List<IdentifyLog> findByLogDateTimeBetweenAndDeviceKeyIn(@Param("fromDate") LocalDateTime fromDate,
			@Param("toDate") LocalDateTime toDate, @Param("deviceKeys") List<String> deviceKeys,
			@Param("models") List<String> models, @Param("identifyTypes") List<String> identifyTypes);

}
