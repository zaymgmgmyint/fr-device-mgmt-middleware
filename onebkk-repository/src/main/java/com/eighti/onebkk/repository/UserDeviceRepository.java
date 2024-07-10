package com.eighti.onebkk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eighti.onebkk.entity.UserDevice;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

	@Query("SELECT ud FROM UserDevice ud WHERE ud.user.id=:userId AND ud.device.id=:deviceId")
	Optional<UserDevice> findUserDeviceByUserIdAndDeviceId(@Param("userId") Long userId,
			@Param("deviceId") Long deviceId);

	@Query("SELECT ud FROM UserDevice ud WHERE ud.user.userId=:userId AND ud.device.deviceKey=:deviceKey")
	Optional<UserDevice> findUserDeviceByUserIdAndDeviceKey(@Param("userId") String userId,
			@Param("deviceKey") String deviceKey);

}
