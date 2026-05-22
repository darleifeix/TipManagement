package com.example.TipsManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TipsManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TipsManagementApplication.class, args);
	}

}
