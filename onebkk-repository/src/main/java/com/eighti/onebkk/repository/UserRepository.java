package com.eighti.onebkk.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eighti.onebkk.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT * FROM user u WHERE u.user_id=:userId LIMIT 1", nativeQuery = true)
	Optional<User> findTopOneByUserId(@Param("userId") String userId);

	@Query("SELECT u FROM User u WHERE u.syncedDatetime>=:lastSyncedDatetime")
	Optional<List<User>> findUserListBySyncedDatetime(@Param("lastSyncedDatetime") LocalDateTime lastSyncedDatetime);

	@Query("SELECT u FROM User u WHERE u.userAccountId = :userAccountId")
	Page<User> findUserByAccountId(@Param("userAccountId") String userAccountId, Pageable pageable);

	default Optional<User> findUserByAccountIdWithLimit(String userAccountId) {
		Pageable limit = PageRequest.of(0, 1);
		Page<User> page = findUserByAccountId(userAccountId, limit);
		return page.getContent().stream().findFirst();
	}

}
