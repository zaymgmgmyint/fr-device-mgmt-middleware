package com.eighti.onebkk.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI defineOpenAPI() {
		Server developmentServer = new Server();
		developmentServer.setUrl("http://127.0.0.1:8080");
		developmentServer.setDescription("Development");

		Server localIpServer = new Server();
		localIpServer.setUrl("http://192.168.1.48:8080");
		localIpServer.setDescription("Local IP Server");

		Contact contact = new Contact();
		contact.setName("One BKK");
		contact.setEmail("onebkk@gmail.com");

		Info info = new Info().title("One BKK Device Middleware").version("1.0")
				.description("This API provides the endpoints of the device management system").contact(contact);

		return new OpenAPI().info(info).servers(List.of(developmentServer, localIpServer));
	}
}
