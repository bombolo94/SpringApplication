package com.spring.values;

import com.spring.context.Coordinates;
import java.util.ArrayList;

public class Line
{
  private Integer id;
  private String description;
  private String timestamp;
  private String schemeID;
  private Coordinates coordinates;
  
  public String getSchemeID()
  {
    return schemeID;
  }
  
  public void setSchemeID(String schemeID) {
    this.schemeID = schemeID;
  }
  

  private Period period = null;
  private ArrayList<PropertyValues> property;
  
  public String getDescription()
  {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getTimestamp() {
    return timestamp;
  }
  
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public Coordinates getCoordinates() {
    return coordinates;
  }
  
  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }
  
  public Period getPeriod() {
    return period;
  }
  
  public void setPeriod(Period period) {
    this.period = period;
  }
  
  public ArrayList<PropertyValues> getProperty()
  {
    return property;
  }
  
  public void setProperty(ArrayList<PropertyValues> property) {
    this.property = property;
  }
  
  public Line() {}
}