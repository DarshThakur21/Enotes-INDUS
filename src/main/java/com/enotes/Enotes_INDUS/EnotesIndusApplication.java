package com.enotes.Enotes_INDUS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class EnotesIndusApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnotesIndusApplication.class, args);

	}

}
