package com.tarotdt.pas.web;

import java.io.IOException;
import java.util.Properties;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ServletInitializer extends SpringBootServletInitializer{
	static {
		 Resource resource = new ClassPathResource("/application.yml");
		 try {
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			System.setProperty("log.base",props.getProperty("logPath"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(Application.class);
	}

}
