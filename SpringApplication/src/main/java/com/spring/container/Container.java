package com.spring.container;

import java.net.URI;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.spring.context.Context;
import com.spring.context.Coordinates;
import com.spring.context.Producer;
import com.spring.specification.*;
import com.spring.values.Line;
import com.spring.values.Period;
import com.spring.values.PropertyValues;
import com.spring.values.Values;



public class Container
{
  public Container() {}
  
  private UrbanDataset UrbanDataset = new UrbanDataset();
  
  public UrbanDataset getUrbanDataset() { return UrbanDataset; }
  
  public void setUrbanDataset(UrbanDataset urbanDataset) {
    UrbanDataset = urbanDataset;
  }
  
  public Specification specification(Node node) {
    Specification specification = new Specification();
    
    Element eElement = (Element)node;
    
    if (!eElement.getAttribute("version").isEmpty()) {
      specification.setVersion(eElement.getAttribute("version"));
    }
    
    IdSpecification id = new IdSpecification();
    Node nID = eElement.getElementsByTagName("id").item(0);
    id.setValue(nID.getTextContent());
    Element eID = (Element) nID;
    if(!eID.getAttribute("schemeID").isEmpty()) {
    	id.setSchemeID(eID.getAttribute("schemeID"));
    }
    specification.setId(id);
    specification.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
    URI uri = URI.create(eElement.getElementsByTagName("uri").item(0).getTextContent());
    specification.setUri(uri);
    if (eElement.getElementsByTagName("properties").item(0) != null)
    {
      NodeList nListProperties = eElement.getElementsByTagName("properties").item(0).getChildNodes();
      ArrayList<PropertySpecification> arrayProperties = new ArrayList(nListProperties.getLength());
      Properties properties = new Properties();
      
      for (int j = 1; j < nListProperties.getLength(); j += 2) {
        PropertySpecification propertySpecification = new PropertySpecification();
        
        Node nodeProperty = nListProperties.item(j);
        if (nodeProperty.getNodeType() == 1) {
          Element elementProperty = (Element)nodeProperty;
          
          String name = elementProperty.getElementsByTagName("propertyName").item(0).getTextContent();
          propertySpecification.setName(name);
          if (elementProperty.getElementsByTagName("propertyDescription").item(0) != null) {
            String description = elementProperty.getElementsByTagName("propertyDescription").item(0).getTextContent();
            
            description = description.replaceAll("\r\n|\r|\n", " ");
            description = description.replaceAll("   ", "");
            propertySpecification.setDescription(description);
          }
          if (elementProperty.getElementsByTagName("dataType").item(0) != null) {
            String dataType = elementProperty.getElementsByTagName("dataType").item(0).getTextContent();
            propertySpecification.setDataType(dataType);
          }
          
          if (elementProperty.getElementsByTagName("codeList").item(0) != null)
          {
            URI codeList = URI.create(elementProperty.getElementsByTagName("codeList").item(0).getTextContent());
            propertySpecification.setCodeList(codeList);
          }
          if (elementProperty.getElementsByTagName("unitOfMeasure").item(0) != null) {
            String unitOfMeasure = elementProperty.getElementsByTagName("unitOfMeasure").item(0).getTextContent();
            propertySpecification.setUnitOfMeasure(unitOfMeasure);
          } else {
            propertySpecification.setUnitOfMeasure(null);
          }
          
          Node subprop = elementProperty.getElementsByTagName("subProperties").item(0);
          ArrayList<String> arraySubproperty = null;
          SubProperties subProperties = new SubProperties();
          
          if (subprop != null) {
            Element elementSubproperties = (Element)subprop;
            NodeList nodeSubproperty = elementSubproperties.getElementsByTagName("propertyName");
            arraySubproperty = new ArrayList();
            
            for (int m = 0; m < elementSubproperties.getElementsByTagName("propertyName").getLength(); m++) {
              String subp = "";
              Node nm = nodeSubproperty.item(m);
              subp = subp + nm.getTextContent();
              arraySubproperty.add(subp);
            }
            subProperties.setPropertyName(arraySubproperty);
            propertySpecification.setSubProperties(subProperties);
          }
          
          arrayProperties.add(propertySpecification);
        }
        properties.setPropertyDefinition(arrayProperties);
        specification.setProperties(properties);
      }
    }
    
    return specification;
  }
  
  public Context context(Node node) {
    Context context = new Context();
    NodeList listContext = node.getChildNodes();
    
    for (int j = 1; j < listContext.getLength(); j += 2) {
      Node childContext = listContext.item(j);
      if (childContext.getNodeName().equals("producer")) {
        Producer producer = new Producer();
        Node nodeProducer = childContext;
        Element elementProducer = (Element)nodeProducer;
        producer.setId(elementProducer.getElementsByTagName("id").item(0).getTextContent());
        Node nodeId = elementProducer.getElementsByTagName("id").item(0);
        Element elementId = (Element)nodeId;
        if (!elementId.getAttribute("schemeID").isEmpty())
          producer.setSchemeID(elementId.getAttribute("schemeID"));
        context.setProducer(producer);
      } else if (childContext.getNodeName().equals("timeZone")) {
        context.setTimeZone(childContext.getTextContent());
      } else if (childContext.getNodeName().equals("timestamp")) {
        context.setTimestamp(childContext.getTextContent());
        Element etimestamp = (Element)childContext;
        if (!etimestamp.getAttribute("schemeID").isEmpty()) {
          context.setSchemeID(etimestamp.getAttribute("schemeID"));
        }
      } else if (childContext.getNodeName().equals("coordinates")) {
        Coordinates coordinates = new Coordinates();
        Element elementCoordinate = (Element)childContext;
        if (!elementCoordinate.getAttribute("format").isEmpty())
        {

          coordinates.setFormat(elementCoordinate.getAttribute("format"));
        }
        coordinates.setLongitude(Double.parseDouble(elementCoordinate.getElementsByTagName("longitude").item(0).getTextContent()));
        coordinates.setLatitude(Double.parseDouble(elementCoordinate.getElementsByTagName("latitude").item(0).getTextContent()));
        if (elementCoordinate.getElementsByTagName("height").item(0) != null)
          coordinates.setHeight(Double.valueOf(Double.parseDouble(elementCoordinate.getElementsByTagName("height").item(0).getTextContent())));
        context.setCoordinates(coordinates);
      } else if (childContext.getNodeName().equals("language")) {
        context.setLanguage(childContext.getTextContent());
      }
    }
    return context;
  }
  
  public Values values(Node node) {
    Values values = new Values();
    Element elementValues = (Element)node;
    ArrayList<PropertyValues> arrayPropertyValues = new ArrayList();
    ArrayList<Line> lines = new ArrayList();
    NodeList nLines = elementValues.getElementsByTagName("line");
    
    for (int i = 0; i < nLines.getLength(); i++) {
      Node nodeLine = nLines.item(i);
      Line line = new Line();
      Element eELine = (Element)nodeLine;
      if (!eELine.getAttribute("id").isEmpty())
        line.setId(Integer.valueOf(Integer.parseInt(eELine.getAttribute("id"))));
      NodeList childLine = nodeLine.getChildNodes();
      
      for (int j = 1; j < childLine.getLength(); j += 2) {
        Node child = childLine.item(j);
        if (child.getNodeName().equals("description")) {
          line.setDescription(child.getTextContent());
        } else if (child.getNodeName().equals("timestamp")) {
          line.setTimestamp(child.getTextContent());
          Element etimestamp = (Element)child;
          if (!etimestamp.getAttribute("schemeID").isEmpty()) {
            line.setSchemeID(etimestamp.getAttribute("schemeID"));
          }
        }
        else if (child.getNodeName().equals("coordinates")) {
          Coordinates coordinates = new Coordinates();
          Element elementCoordinate = (Element)child;
          coordinates.setFormat(elementCoordinate.getAttribute("format"));
          coordinates.setLongitude(Double.parseDouble(elementCoordinate.getElementsByTagName("longitude").item(0).getTextContent()));
          coordinates.setLatitude(Double.parseDouble(elementCoordinate.getElementsByTagName("latitude").item(0).getTextContent()));
          if (elementCoordinate.getElementsByTagName("height").item(0) != null)
            coordinates.setHeight(Double.valueOf(Double.parseDouble(elementCoordinate.getElementsByTagName("height").item(0).getTextContent())));
          line.setCoordinates(coordinates);
        } else if (child.getNodeName().equals("period")) {
          Period period = new Period();
          Element elementPeriod = (Element)child;
          Node nodeEnd = elementPeriod.getElementsByTagName("end_ts").item(0);
          period.setEnd_ts(nodeEnd.getTextContent());
          
          Element elementEnd = (Element)nodeEnd;
          if (!elementEnd.getAttribute("schemeID").isEmpty()) {
            period.setSchemeIDend(elementEnd.getAttribute("schemeID"));
          }
          Node nodeST = elementPeriod.getElementsByTagName("start_ts").item(0);
          period.setStart_ts(nodeST.getTextContent());
          Element elementSt = (Element)nodeST;
          if (!elementSt.getAttribute("schemeID").isEmpty()) {
            period.setSchemeIDend(elementSt.getAttribute("schemeID"));
          }
          
          line.setPeriod(period);
        } else if (child.getNodeName().equals("property")) {
          PropertyValues propertyValue = new PropertyValues();
          Element elementProperyValue = (Element)child;
          propertyValue.setName(elementProperyValue.getAttribute("name"));
          propertyValue.setVal(elementProperyValue.getElementsByTagName("val").item(0).getTextContent());
          arrayPropertyValues.add(propertyValue);
        }
      }
      


      line.setProperty(arrayPropertyValues);
      lines.add(line);
    }
    
    values.setLines(lines);
    return values;
  }
}