package com.SmartCourierManagementApi.smartCourierManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartCourierManagementSystemApplication {

	public static void main(String[] args)
	{

		SpringApplication.run(SmartCourierManagementSystemApplication.class, args);
	}

}
