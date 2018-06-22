package com.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	 @Bean
	  public org.springframework.web.servlet.ViewResolver getViewResolver() {
	    InternalResourceViewResolver resolver = 
	      new InternalResourceViewResolver();
	    resolver.setPrefix("/WEB-INF/jsp/");
	    resolver.setSuffix(".jsp");
	    resolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
	    return resolver;
	  }
}
