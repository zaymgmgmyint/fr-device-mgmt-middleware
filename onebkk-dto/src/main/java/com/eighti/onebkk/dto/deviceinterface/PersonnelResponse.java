package com.eighti.onebkk.dto.deviceinterface;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes({ @Type(value = Data.class, name = "data") })
public class PersonnelResponse implements Serializable {

	private static final long serialVersionUID = 2104788089481456732L;

	private String code;

	private String msg;

	private Integer result;

	private Boolean success;

	private Data data;

	@Getter
	@Setter
	public class Data implements Serializable {

		private static final long serialVersionUID = 6574566187083931013L;

		private String cardAndPasswordPermission;
	}
}
