package com.dhanashri.Response.Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ResponseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResponseServiceApplication.class, args);
	}

}
