package com.spring.context;

public class Coordinates
{
  private String format;
  private double latitude;
  private double longitude;
  private Double height;
  
  public Coordinates() {}
  
  public String getFormat()
  {
    return format;
  }
  
  public void setFormat(String format) {
    this.format = format;
  }
  
  public double getLatitude() {
    return latitude;
  }
  
  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }
  
  public double getLongitude() {
    return longitude;
  }
  
  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
  
  public Double getHeight() {
    return height;
  }
  
  public void setHeight(Double height) {
    this.height = height;
  }
}