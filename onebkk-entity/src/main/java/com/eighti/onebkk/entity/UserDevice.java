package com.eighti.onebkk.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_device")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDevice extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7775781124111794018L;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;

	@Column(name = "data_synced")
	private Integer dataSynced;

	@Column(name = "synced_datetime")
	private LocalDateTime syncedDatetime;

	@Column(name = "response_code")
	private String responseCode;

	@Column(name = "response_message")
	private String responseMessage;
}
