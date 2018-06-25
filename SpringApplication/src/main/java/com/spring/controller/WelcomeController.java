package com.spring.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.InternalResourceViewResolver;



@Controller
public class WelcomeController
{
  public WelcomeController() {}
  
  @org.springframework.web.bind.annotation.RequestMapping({"/"})
  public String index() { System.out.println(System.getProperty("user.dir")); return "welcome"; }
    
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