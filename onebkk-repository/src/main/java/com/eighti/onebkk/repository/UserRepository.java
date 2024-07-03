package com.eighti.onebkk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eighti.onebkk.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
