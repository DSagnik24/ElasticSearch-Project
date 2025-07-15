package com.Project.New_Proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NewProjApplication {

	public static void main(String[] args) {

		SpringApplication.run(NewProjApplication.class, args);
	}

}
