package com.eighti.onebkk.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "heart_beat_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartBeatLog implements Serializable {

	private static final long serialVersionUID = -2305104519801483678L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "device_key")
	private String deviceKey;

	@Column(name = "time")
	private String time;

	@Column(name = "ip")
	private String ip;

	@Column(name = "person_count")
	private String personCount;

	@Column(name = "face_count")
	private String faceCount;

	@Column(name = "finger_count")
	private String fingerCount;

	@Column(name = "version")
	private String version;

	@Column(name = "free_disk_space")
	private String freeDiskSpace;

	@Column(name = "cpu_usage_rate")
	private String cpuUsageRate;

	@Column(name = "cpu_temperature")
	private String cpuTemperature;

	@Column(name = "memory_usage_rate")
	private String memoryUsageRate;

	@Column(name = "device_name")
	private String deviceName;

	@Column(name = "sdk_version")
	private String sdkVersion;

	@Column(name = "company_name")
	private String companyName;

	@Column(name = "log_datetime")
	private LocalDateTime logDateTime;
}
