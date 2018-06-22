package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ParserController {
	

	  @RequestMapping(value={"/parser"})
	  public String index() {
		  return "/parser"; 
	  }
}
