package com.v1.iskream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IskreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(IskreamApplication.class, args);
	}

}
