package com.spring.context;

import org.w3c.dom.Element;

public class Context
{
  private Producer producer;
  private String timeZone;
  private String timestamp;
  private Coordinates coordinates;
  private String language;
  private String schemeID;
  
  public Context() {}
  
  public String getSchemeID() {
    return schemeID;
  }
  
  public void setSchemeID(String schemeID) {
    this.schemeID = schemeID;
  }
  
  public Coordinates getCoordinates()
  {
    return coordinates;
  }
  
  public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
  
  public Producer getProducer()
  {
    return producer;
  }
  
  public void setProducer(Producer producer) { this.producer = producer; }
  
  public String getTimeZone() {
    return timeZone;
  }
  
  public void setTimeZone(String timeZone) { this.timeZone = timeZone; }
  
  public String getLanguage() {
    return language;
  }
  
  public void setLanguage(String language) { this.language = language; }
  
  public String getTimestamp() {
    return timestamp;
  }
  
  public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
  
  public Element text(org.w3c.dom.Document doc) {
    Element context = doc.createElement("context");
    
    Element producer = doc.createElement("producer");
    Element ID = doc.createElement("id");
    if (getProducer().getSchemeID() != null) {
      ID.setAttribute("schemeID", getProducer().getSchemeID());
      ID.appendChild(doc.createTextNode(getProducer().getId()));
    }
    
    producer.appendChild(ID);
    
    Element timeZone = doc.createElement("timeZone");
    timeZone.appendChild(doc.createTextNode(getTimeZone()));
    
    Element timestamp = doc.createElement("timestamp");
    timestamp.appendChild(doc.createTextNode(getTimestamp()));
    if (getSchemeID() != null) {
      timestamp.setAttribute("schemeID", getSchemeID());
      timestamp.appendChild(doc.createTextNode(getSchemeID()));
    }
    
    Element coordinates = doc.createElement("coordinates");
    coordinates.setAttribute("format", getCoordinates().getFormat());
    Element latitude = doc.createElement("latitude");
    latitude.appendChild(doc.createTextNode(String.valueOf(getCoordinates().getLatitude())));
    Element longitude = doc.createElement("longitude");
    longitude.appendChild(doc.createTextNode(String.valueOf(getCoordinates().getLongitude())));
    if (getCoordinates().getHeight() != null)
    {
      Element altitude = doc.createElement("height");
      altitude.appendChild(doc.createTextNode(String.valueOf(getCoordinates().getHeight())));
      coordinates.appendChild(altitude);
    }
    

    coordinates.appendChild(latitude);
    coordinates.appendChild(longitude);
    context.appendChild(producer);
    context.appendChild(timeZone);
    context.appendChild(timestamp);
    context.appendChild(coordinates);
    if (getLanguage() != null) {
      Element lang = doc.createElement("language");
      lang.appendChild(doc.createTextNode(getLanguage()));
      context.appendChild(lang);
    }
    


    return context;
  }
}
