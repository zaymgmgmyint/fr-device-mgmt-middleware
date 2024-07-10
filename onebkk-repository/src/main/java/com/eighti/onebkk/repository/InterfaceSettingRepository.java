package com.eighti.onebkk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eighti.onebkk.entity.InterfaceSetting;

public interface InterfaceSettingRepository extends JpaRepository<InterfaceSetting, Long> {

	@Query(value = "SELECT * FROM interface_setting WHERE code = :code LIMIT 1", nativeQuery = true)
	Optional<InterfaceSetting> getValueByCode(@Param("code") String code);

}
