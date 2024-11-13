package com.eighti.onebkk.utils.rsa;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class APITest {
	
	public static void main(String[] args) {
		callCustomerInfoAPI();
	}

	public static void callCustomerInfoAPI() {
		
		
		RestTemplate restTemplate = new RestTemplate();
		
		String url = "http://uat.glorymtel.xyz/ob-iam/integration/tokens/6edafb8d-adce-490f-ac50-1de583b90869";

		// Set up the headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("User-Agent", "PostmanRuntime/7.42.0");
		headers.set("Accept", "*/*");
		headers.set("Accept-Encoding", "gzip, deflate, br");
		headers.set("Connection", "keep-alive");
		headers.set("x-account-id", "3a534b2c-cf7b-4d81-af5b-7d375b5fcbc1");
		headers.set("x-permissions", "eyJzdWIiOiIzYTUzNGIyYy1jZjdiLTRkODEtYWY1Yi03ZDM3NWI1ZmNiYzEiLCJpc3MiOiJPbmVCYW5na29rIiwiaWF0IjoxNzMwODc5NjU4LCJleHAiOjE3MzM0NzE2NTgsInBlcm1pc3Npb24iOlt7ImlkIjoiNDE2YmY5OWItMDg0NC00YTVlLThjZjAtNjRiMWZkZDY3YmJlIiwicGVybWl0dGVlX3R5cGUiOiJhY2NvdW50IiwidmFsdWUiOnsibmFtZSI6Im9iLWJtczpmcyIsInNlcnZpY2UiOiJvYi1ibXMiLCJhY3Rpb25zIjpbIioiXSwicmVzb3VyY2VfdHlwZSI6ImZzIiwicmVzb3VyY2UiOnsiaWQiOiJzZWxmIn19LCJhY2NvdW50X2dyb3VwX2lkIjpudWxsLCJyb2xlX2lkIjpudWxsfSx7ImlkIjoiODExYjZiOTgtOTVkMS00NTA2LWJjZDUtYTdkYzRmMjU3NzZlIiwicGVybWl0dGVlX3R5cGUiOiJhY2NvdW50IiwidmFsdWUiOnsibmFtZSI6Im9iLWlhbTp0b2tlbiIsInNlcnZpY2UiOiJvYi1pYW0iLCJhY3Rpb25zIjpbInJlYWQiXSwicmVzb3VyY2VfdHlwZSI6InRva2VuIiwicmVzb3VyY2UiOnsiYWNjb3VudF9pZCI6InNlbGYifX0sImFjY291bnRfZ3JvdXBfaWQiOm51bGwsInJvbGVfaWQiOm51bGx9XX0"); // Ensure this is correct

		// Create HttpEntity with headers
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			// Send the GET request
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

			// Check the response
			if (response.getStatusCode().is2xxSuccessful()) {
				System.out.println("Response: " + response.getBody());
			} else {
				System.out.println("Failed with status code: " + response.getStatusCode());
			}
		} catch (HttpClientErrorException.Forbidden e) {
			// Handle 403 Forbidden error
			System.err.println("Permission Denied: " + e.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
