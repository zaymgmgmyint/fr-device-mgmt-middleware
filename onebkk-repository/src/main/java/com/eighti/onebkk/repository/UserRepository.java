package com.eighti.onebkk.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eighti.onebkk.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.userId=:userId")
	Optional<User> findTopOneByUserId(@Param("userId") String userId);

	@Query("SELECT u FROM User u WHERE u.syncedDatetime>=:lastSyncedDatetime")
	Optional<List<User>> findUserListBySyncedDatetime(@Param("lastSyncedDatetime") LocalDateTime lastSyncedDatetime);

}
