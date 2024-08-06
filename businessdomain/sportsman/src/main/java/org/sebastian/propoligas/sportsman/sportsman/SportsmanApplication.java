package org.sebastian.propoligas.sportsman.sportsman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@EnableFeignClients
@SpringBootApplication
@PropertySource(value = "classpath:swagger.properties", encoding = "UTF-8")
public class SportsmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportsmanApplication.class, args);
	}

}
