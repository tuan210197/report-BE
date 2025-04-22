package com.foxconn.EmployeeManagerment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication()
@EnableScheduling
@EnableSpringDataWebSupport

public class EmployeeManagermentApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagermentApplication.class, args);
	}

}
