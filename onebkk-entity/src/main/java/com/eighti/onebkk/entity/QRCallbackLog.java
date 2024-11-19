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
@Table(name = "qr_callback_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QRCallbackLog extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4156295757549354768L;

	@Column(name = "qr_data")
	private String qrData;

	@Column(name = "qr_id")
	private String qrId;

	@Column(name = "decrypted_data")
	private String decryptedData;

	@Column(name = "encrypted_data")
	private String encryptedData;

	@Column(name = "response")
	private String response;

	@Column(name = "success")
	private Boolean success;

	@Column(name = "error_code")
	private String errorCode;

	@Column(name = "error_message")
	private String errrMessage;

	@Column(name = "log_datetime")
	private LocalDateTime logDateTime;
}
