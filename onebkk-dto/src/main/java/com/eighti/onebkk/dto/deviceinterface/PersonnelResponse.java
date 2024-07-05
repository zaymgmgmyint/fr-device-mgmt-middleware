package com.eighti.onebkk.dto.deviceinterface;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonnelResponse implements Serializable {

	private static final long serialVersionUID = 2104788089481456732L;

	private String code;

	private String msg;

	private Integer result;

	private Boolean success;
	
	private Object data;
}
