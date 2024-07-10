package com.eighti.onebkk.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Value("${server.localhost}")
	private String serverLocalhost;

	@Value("${server.localhost.ip}")
	private String serverLocalhostIp;

	@Bean
	OpenAPI defineOpenAPI() {
		Server developmentServer = new Server();
		developmentServer.setUrl(serverLocalhost);
		developmentServer.setDescription("Development");

		Server localIpServer = new Server();
		localIpServer.setUrl(serverLocalhostIp);
		localIpServer.setDescription("Local IP Server");

		Contact contact = new Contact();
		contact.setName("One BKK");
		contact.setEmail("onebkk@gmail.com");

		Info info = new Info().title("One BKK Device Middleware").version("1.0")
				.description("This API provides the endpoints of the device management system").contact(contact);

		return new OpenAPI().info(info).servers(List.of(developmentServer, localIpServer));
	}
}
