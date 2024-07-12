package com.eighti.onebkk.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4819987980054618315L;

	@Column(name = "name")
	private String name;

	@Column(name = "login_name")
	private String loginName;

	@Column(name = "password")
	private String password;

	@Column(name = "status")
	private String status;

	@Column(name = "created_datetime")
	private LocalDateTime createdDateTime;

	@Column(name = "updated_datetime")
	private LocalDateTime updatedDateTime;

}
