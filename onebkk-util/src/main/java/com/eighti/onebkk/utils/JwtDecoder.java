package com.eighti.onebkk.utils;

import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtDecoder {

	public static String decodeJwtPayload(String jwt) {
		// Split the JWT into its parts (Header, Payload, Signature)
		String[] jwtParts = jwt.split("\\.");

		if (jwtParts.length != 3) {
			throw new IllegalArgumentException("Invalid JWT token format");
		}

		// Decode the Payload part (second part)
		String payload = jwtParts[1];

		// Base64 decode the Payload (URL Safe Base64 decoding)
		byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);

		// Convert decoded bytes to a String (usually UTF-8)
		return new String(decodedBytes);
	}

	public static String extractSubFromJwt(String jwt) throws Exception {
		// Decode the payload from the JWT
		String payload = decodeJwtPayload(jwt);

		// Use Jackson ObjectMapper to parse the JSON payload
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(payload);

		// Extract the 'sub' claim
		return jsonNode.get("sub").asText();
	}

	public static String extractPayloadTokenFromJwt(String jwt) {
		// Split the JWT into its parts (Header, Payload, Signature)
		String[] jwtParts = jwt.split("\\.");

		if (jwtParts.length != 3) {
			throw new IllegalArgumentException("Invalid JWT token format");
		}

		// Decode the Payload part (second part)
		String payload = jwtParts[1];

		// Extract the payload token
		return payload;
	}

}
