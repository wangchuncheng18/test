package com.tarotdt.pas.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.tarotdt.pas.web.config.WebConfig;

@SpringBootApplication
@ComponentScan({ "com.tarotdt.spring.config", "com.tarotdt.pas.web.config", "com.tarotdt.pas" })
public class Application extends WebConfig {
	
	static {
		System.setProperty("log.base", System.getenv("PAS_HOME"));
	}

	/**
	 * Start
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.run(args);
	}
}
