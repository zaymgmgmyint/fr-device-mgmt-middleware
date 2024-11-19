package com.eighti.onebkk.dto.api.response;

import lombok.Data;

@Data
public class TcctAuthResponse {

	private DataContainer data;

	// Class representing the "data" object
	@Data
	public static class DataContainer {
		private Token token;
		private Metadata metadata;
	}

	// Class representing the "token" object
	@Data
	public static class Token {
		private String value;
	}

	// Class representing the "metadata" object
	@Data
	public static class Metadata {
		private String key;
		private String endpoint;
	}
}
