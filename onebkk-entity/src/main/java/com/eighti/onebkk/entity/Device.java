package com.eighti.onebkk.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "device")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6021162980540326451L;

	@Column(name = "device_name")
	private String deviceName;

	@Column(name = "device_key")
	private String deviceKey;

	@Column(name = "device_ip")
	private String deviceIp;

	@Column(name = "device_username")
	private String deviceUsername;

	@Column(name = "device_password")
	private String devicePassword;

	@Column(name = "device_group")
	private String deviceGroup;

	@Column(name = "device_status")
	private Integer deviceStatus;

	@Column(name = "status")
	private Integer status;
	
	@Column(name = "last_heartbeat_time")
	private LocalDateTime lastHeartbeatTime;
	
	@Column(name = "last_fetch_time")
	private LocalDateTime lastFetchTime;
}
