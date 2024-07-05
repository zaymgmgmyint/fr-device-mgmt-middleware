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
@Table(name = "sync_time")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SyncTime extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1181273328871513368L;
	@Column(name = "last_sync_time")
	private LocalDateTime lastSyncTime;
}
