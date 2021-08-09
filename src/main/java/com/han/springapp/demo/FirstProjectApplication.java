package com.han.springapp.demo;

import com.han.springapp.demo.security.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class FirstProjectApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FirstProjectApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(FirstProjectApplication.class, args);
	}

	/**
	 * https://docs.spring.io/spring-javaconfig/docs/1.0.0.M4/reference/html/ch02s02.html
	 * Bean is used to create an injectable object which can be used in other files
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}
}
