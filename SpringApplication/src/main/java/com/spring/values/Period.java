package com.spring.values;

public class Period
{
  private String start_ts;
  private String end_ts;
  private String schemeIDend;
  private String schemeIDst;
  
  public String getSchemeIDst()
  {
    return schemeIDst;
  }
  
  public void setSchemeIDst(String schemeIDst) {
    this.schemeIDst = schemeIDst;
  }
  
  public String getSchemeIDend() {
    return schemeIDend;
  }
  
  public void setSchemeIDend(String schemeIDend) {
    this.schemeIDend = schemeIDend;
  }
  
  public String getStart_ts() {
    return start_ts;
  }
  
  public void setStart_ts(String start_ts) {
    this.start_ts = start_ts;
  }
  
  public String getEnd_ts() {
    return end_ts;
  }
  
  public void setEnd_ts(String end_ts) {
    this.end_ts = end_ts;
  }
  
  public Period() {}
}