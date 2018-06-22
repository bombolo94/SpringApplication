package com.spring.specification;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Specification
{
  private String version;
  private IdSpecification id;
  private String name;
  private java.net.URI uri;
  private Properties properties;
  
  public Specification() {}
  
  public String getVersion()
  {
    return version;
  }
  
  public Properties getProperties() {
    return properties;
  }
  
  public void setProperties(Properties properties) {
    this.properties = properties;
  }
  
  public void setVersion(String version) {
    this.version = version;
  }
  
  public IdSpecification getId() {
    return id;
  }
  
  public void setId(IdSpecification id) { this.id = id; }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) { this.name = name; }
  

  public java.net.URI getUri()
  {
    return uri;
  }
  
  public void setUri(java.net.URI uri) { this.uri = uri; }
  
  public Element text(Document doc)
  {
    Element spec = doc.createElement("specification");
    if (getVersion() != null) {
      spec.setAttribute("version", getVersion());
    }
    
    Element ID = doc.createElement("id");
    ID.appendChild(doc.createTextNode(getId().getValue()));
    if(getId().getSchemeID()!=null) {
    	ID.setAttribute("schemeID", getId().getSchemeID());
    }
    
    Element name = doc.createElement("name");
    name.appendChild(doc.createTextNode(getName()));
    
    Element uri = doc.createElement("uri");
    uri.appendChild(doc.createTextNode(String.valueOf(getUri())));
    spec.appendChild(ID);
    spec.appendChild(name);
    spec.appendChild(uri);
    if (getProperties() != null) {
      Element properties = doc.createElement("properties");
      
      for (PropertySpecification prop : getProperties().getPropertyDefinition()) {
        if (prop.getName() != null) {
          Element s = prop.text(doc);
          properties.appendChild(s);
        }
      }
      spec.appendChild(properties);
    }
    return spec;
  }
}