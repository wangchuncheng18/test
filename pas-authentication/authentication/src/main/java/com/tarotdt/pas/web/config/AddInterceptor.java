package com.tarotdt.pas.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tarotdt.pas.web.intercepter.PermissionInterceptor;

@Configuration
public class AddInterceptor extends WebMvcConfigurerAdapter {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
	registry.addInterceptor(new PermissionInterceptor()).addPathPatterns("/**");
	super.addInterceptors(registry);
  }

}