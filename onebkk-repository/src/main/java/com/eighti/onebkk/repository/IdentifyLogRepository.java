package com.eighti.onebkk.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eighti.onebkk.entity.IdentifyLog;

public interface IdentifyLogRepository extends JpaRepository<IdentifyLog, Long> {

	@Query(value = "SELECT * FROM identify_log log WHERE log.log_time >= :fromDate AND log.log_time <= :toDate ORDER BY id DESC", nativeQuery = true)
	List<IdentifyLog> findByLogDateTimeBetween(@Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate);

	@Query(value = "SELECT * FROM identify_log log WHERE log.log_time >= :fromDate AND log.log_time <= :toDate AND log.device_key IN (:deviceKeys) ORDER BY id DESC", nativeQuery = true)
	List<IdentifyLog> findByLogDateTimeBetweenAndDeviceKeyIn(@Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate, @Param("deviceKeys") List<String> deviceKeys);

}
