package com.cred.cwod;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Log4j2
@EnableSwagger2
public class RunApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunApplication.class, args);
		log.info("Backend started!!");
	}
}
