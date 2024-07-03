package com.eighti.onebkk.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserImportDto implements Serializable {

	private static final long serialVersionUID = 1828748615379155261L;

	private String userId;

	private String userName;

	private String userPhoneNumber;

	private String userTag;

	private String frImageId;

	private String frCardNumber;
	
	private String frPermissionType;
	
	private String createdDate;

	private String lastModifiedDate;

}
