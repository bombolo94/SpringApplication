package com.spring.template.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.spring.core.Utils;



/**
 * This class generate the section of the template message of 
 * the values section.
 *      
 * @author Raffaele Ianniello
 */
public class Values implements IMessageTemplate{
	
	List<Property> properties;
	private final String defaultSart = "1000-12-31T00:00:00";
	private final String defaultEnd = "1000-12-31T23:59:00";

	/**
	 * Constructs a Values object from the producer of an UrbanDataset
	 * 
	 * @param properties	the list of properties to show in the section
	 */
	public Values(List<Property> properties) {
		this.properties = properties;
	}
	
	@Override
	public Element text(Document doc) {
		List<Resource> res = new ArrayList<Resource>();
		List<Resource> periodRes = Utils.periodSubprop();
		List<Resource> coordRes = Utils.coordSubprop();
		res.addAll(periodRes);
		res.addAll(coordRes);
		List<String> nameList = new ArrayList<String>();
		res.forEach(item->{
			nameList.add(item.getLocalName());
		});		
		
		Element values = doc.createElement("values");
		Element line = doc.createElement("line");
		line.setAttribute("id", "1");
		values.appendChild(line);
		
		boolean coord = false;
		boolean period = false;
		boolean description = false;
		List<Property> newProp = new ArrayList<Property>();
		for (Property p : properties) {
			if (!nameList.contains(p.name)) {
				newProp.add(p);
			}
			
			if (p.name.equalsIgnoreCase("coordinates")) {
				coord = true;
			} else if (p.name.equalsIgnoreCase("period")) {
				period = true;
//			} else if (p.name.equalsIgnoreCase("description")) {
//				description = true;
			}
		}
		
//		if (description) {
//			Element propertyCoord = doc.createElement("description");
//		}
		if (coord) {
			Element propertyCoord = doc.createElement("coordinates");
			propertyCoord.setAttribute("format", "WGS84-DD");
			Element vallat = doc.createElement("latitude");
			Element vallong = doc.createElement("longitude");
			Element valhei = doc.createElement("height");
			propertyCoord.appendChild(vallat);
			propertyCoord.appendChild(vallong);
			propertyCoord.appendChild(valhei);
			
			line.appendChild(propertyCoord);
		}
		if (period) {
			Element propertyPeriod = doc.createElement("period");
			Element valstart = doc.createElement("start_ts");
			Element valend = doc.createElement("end_ts");
			valstart.appendChild(doc.createTextNode(this.defaultSart));
			valend.appendChild(doc.createTextNode(this.defaultEnd));
			propertyPeriod.appendChild(valstart);
			propertyPeriod.appendChild(valend);
			
			line.appendChild(propertyPeriod);
		}
		
		for (Property p : newProp) {
			if (nameList.contains(p.name)) {
			} else {
				Element property = doc.createElement("property");
				property.setAttribute("name", p.name);
				
				Element val = doc.createElement("val");
				val.setNodeValue("test");
				property.appendChild(val);
				
				line.appendChild(property);
			}
		}
		return values;
	}

}
