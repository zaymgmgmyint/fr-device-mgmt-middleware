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
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6087655054485171033L;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "user_phone_number")
	private String userPhoneNumber;

	@Column(name = "user_tag")
	private String userTag;

	@Column(name = "user_created_date")
	private LocalDateTime userCreatedDate;

	@Column(name = "user_last_modified_date")
	private LocalDateTime userLastModifiedDate;

	@Column(name = "fr_card_number")
	private String frCardNumber;

	@Column(name = "fr_image_id")
	private String frImageId;

	@Column(name = "permission_type")
	private Integer permissionType;

	@Column(name = "companny")
	private String companny;

	@Column(name = "baseLocation")
	private String baseLocation;

	@Column(name = "data_synced")
	private Integer dataSynced;

	@Column(name = "synced_datetime")
	private LocalDateTime syncedDatetime;

	@Column(name = "status")
	private Integer status;
}
