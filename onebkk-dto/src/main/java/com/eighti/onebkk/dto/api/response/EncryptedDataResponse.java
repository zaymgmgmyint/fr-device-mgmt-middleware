package com.eighti.onebkk.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EncryptedDataResponse {

	private DataContainer data;

	@Data
	public static class DataContainer {

		@JsonProperty(value = "encrypted_data")
		private String encryptedData;
	}
}
