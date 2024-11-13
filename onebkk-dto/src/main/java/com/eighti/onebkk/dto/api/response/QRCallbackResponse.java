package com.eighti.onebkk.dto.api.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class QRCallbackResponse implements Serializable {

	private static final long serialVersionUID = 8286039084740157972L;
	
	private String deviceKey;
	private boolean setQRCallbackUrl;
	

	private String code;

	private String data;

	private String msg;

	private String result;

	private boolean success;

}
