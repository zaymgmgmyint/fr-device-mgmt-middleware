package com.eighti.onebkk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eighti.onebkk.entity.SyncTime;

public interface SyncTimeRepository extends JpaRepository<SyncTime, Long> {

	@Query("SELECT st FROM SyncTime st ORDER BY st.lastSyncTime DESC LIMIT 1")
	Optional<SyncTime> findTopOneByOrderByLastSyncTimeDesc();

}
