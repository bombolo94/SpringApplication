package com.spring.values;

import com.spring.context.Coordinates;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Values
{
  private java.util.ArrayList<Line> line;
  
  public java.util.ArrayList<Line> getLines()
  {
    return line;
  }
  
  public void setLines(java.util.ArrayList<Line> lines) { line = lines; }
  
  public Values() {}
  
  public Element text(Document doc) {
    Element values = doc.createElement("values");
    for (Line l : getLines()) {
      Element line = doc.createElement("line");
      if (l.getId() != null) {
        line.setAttribute("id", String.valueOf(l.getId()));
      }
      
      if (l.getDescription() != null) {
        Element description = doc.createElement("description");
        description.appendChild(doc.createTextNode(l.getDescription()));
        line.appendChild(description);
      }
      
      if (l.getTimestamp() != null) {
        Element timestmp = doc.createElement("timestamp");
        timestmp.appendChild(doc.createTextNode(l.getTimestamp()));
        line.appendChild(timestmp);
        if (l.getSchemeID() != null) {
          timestmp.setAttribute("schemeID", l.getSchemeID());
          timestmp.appendChild(doc.createTextNode(l.getSchemeID()));
        }
      }
      
      if (l.getPeriod() != null) {
        Element period = doc.createElement("period");
        Element start_ts = doc.createElement("start_ts");
        start_ts.appendChild(doc.createTextNode(l.getPeriod().getStart_ts()));
        if (l.getPeriod().getSchemeIDst() != null) {
          start_ts.setAttribute("schemeID", l.getPeriod().getSchemeIDst());
          start_ts.appendChild(doc.createTextNode(l.getPeriod().getSchemeIDst()));
        }
        Element end_ts = doc.createElement("end_ts");
        end_ts.appendChild(doc.createTextNode(l.getPeriod().getEnd_ts()));
        if (l.getPeriod().getSchemeIDend() != null) {
          end_ts.setAttribute("schemeID", l.getPeriod().getSchemeIDend());
          end_ts.appendChild(doc.createTextNode(l.getPeriod().getSchemeIDend()));
        }
        period.appendChild(start_ts);
        period.appendChild(end_ts);
        
        line.appendChild(period);
      }
      Element latitude;
      if (l.getCoordinates() != null) {
        Element coordinates = doc.createElement("coordinates");
        if (l.getCoordinates().getFormat() != null) {
          coordinates.setAttribute("format", l.getCoordinates().getFormat());
        }
        latitude = doc.createElement("latitude");
        latitude.appendChild(doc.createTextNode(String.valueOf(l.getCoordinates().getLatitude())));
        Element longitude = doc.createElement("longitude");
        longitude.appendChild(doc.createTextNode(String.valueOf(l.getCoordinates().getLongitude())));
        if (l.getCoordinates().getHeight() != null)
        {
          Element altitude = doc.createElement("height");
          altitude.appendChild(doc.createTextNode(String.valueOf(l.getCoordinates().getHeight())));
          coordinates.appendChild(altitude);
        }
        

        coordinates.appendChild(latitude);
        coordinates.appendChild(longitude);
        
        line.appendChild(coordinates);
      }
      for (PropertyValues p : l.getProperty()) {
        Element property = doc.createElement("property");
        property.setAttribute("name", p.getName());
        Element val = doc.createElement("val");
        val.appendChild(doc.createTextNode(p.getVal()));
        property.appendChild(val);
        line.appendChild(property);
      }
      values.appendChild(line);
    }
    return values;
  }
}