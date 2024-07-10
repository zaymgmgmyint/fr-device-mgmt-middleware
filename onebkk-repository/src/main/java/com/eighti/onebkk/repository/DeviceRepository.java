package com.eighti.onebkk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eighti.onebkk.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {

	List<Device> findByStatus(int status);

	@Query(value = "SELECT * FROM device d WHERE d.device_key=:deviceKey", nativeQuery = true)
	Optional<Device> findDeviceByDeviceKey(@Param("deviceKey") String deviceKey);

}
