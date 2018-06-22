package com.spring.controller;

import org.springframework.stereotype.Controller;

@Controller
public class Confirmation {
  public Confirmation() {}
  
  @org.springframework.web.bind.annotation.RequestMapping({"/confirmation"})
  public String confirmation() { return "confirmation"; }
}