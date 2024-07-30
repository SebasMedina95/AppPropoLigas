package org.sebastian.propoligas.sportsman.sportsman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SportsmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportsmanApplication.class, args);
	}

}
