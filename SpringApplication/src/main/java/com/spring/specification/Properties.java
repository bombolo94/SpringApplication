package com.spring.specification;

import java.util.ArrayList;

public class Properties {
  public Properties() {}
  
  public ArrayList<PropertySpecification> getPropertyDefinition() { return propertyDefinition; }
  
  private ArrayList<PropertySpecification> propertyDefinition;
  public void setPropertyDefinition(ArrayList<PropertySpecification> propertyDefinition) { this.propertyDefinition = propertyDefinition; }
}