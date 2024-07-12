package com.eighti.onebkk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eighti.onebkk.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	Optional<Admin> findByLoginNameAndPassword(String loginName, String password);

}
