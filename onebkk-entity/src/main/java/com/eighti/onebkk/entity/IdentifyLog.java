package com.eighti.onebkk.entity;

import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "identify_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentifyLog implements Serializable {

	private static final long serialVersionUID = 4484099021944001949L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "alive_type")
	private String aliveType;

	@Column(name = "base64")
	private String base64;

	@Column(name = "device_key")
	private String deviceKey;

	@Column(name = "id_card_num")
	private String idcardNum;

	@Column(name = "identify_type")
	private String identifyType;

	@Column(name = "ip")
	private String ip;

	@Column(name = "model")
	private String model;

	@Column(name = "pass_time_type")
	private String passTimeType;

	@Column(name = "path")
	private String path;

	@Column(name = "permission_time_type")
	private String permissionTimeType;

	@Column(name = "person_id")
	private String personId;

	@Column(name = "rec_type")
	private Integer recType;

	@Column(name = "time")
	private String time;

	@Column(name = "dst_offset")
	private Integer dstOffset;

	@Column(name = "passback_trigger_type")
	private Integer passbackTriggerType;

	@Column(name = "mask_state")
	private Integer maskState;

	@Column(name = "work_code")
	private String workCode;

	@Column(name = "attendance")
	private String attendance;

	@Column(name = "type")
	private String type;

	@Column(name = "log_datetime")
	private LocalDateTime logDateTime;

	@Column(name = "log_time")
	private LocalDate logTime;

}
