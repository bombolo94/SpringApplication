package com.spring.specification;

public class IdSpecification
{
  private String value;
  private String schemeID;
  
  public String getSchemeID() {
	return schemeID;
}

public void setSchemeID(String schemeID) {
	this.schemeID = schemeID;
}

public IdSpecification() {}
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
}