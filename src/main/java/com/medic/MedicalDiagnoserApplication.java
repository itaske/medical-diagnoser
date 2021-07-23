package com.medic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MedicalDiagnoserApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalDiagnoserApplication.class, args);
	}

}
