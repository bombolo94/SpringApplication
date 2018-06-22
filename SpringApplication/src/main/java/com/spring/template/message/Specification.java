package com.spring.template.message;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class generate the section of the template message of 
 * the specification section.
 *      
 * @author Raffaele Ianniello
 */
public class Specification implements IMessageTemplate{
	
	private String ID;
	private String name;
	private String URI;
	private List<Property> properties;
	private final String defaulSchemeID = "SCPS";
	
	/**
	 * Constructs a Specification object from the ID, name and URI of an UrbanDataset
	 * 
	 * @param ID	ID of the UrbanDataset
	 * @param name	name of the UrbanDataset
	 * @param URI	URI of the UrbanDataset
	 */
	public Specification(String ID, String name, String URI) {
		this.ID = ID;
		this.name = name;
		this.URI = URI;
	}
	
	/**
	 * Construct a Specification object from the lists of params and properties of an UrbanDataset
	 * 
	 * @param paramsList	list of parameters to set up the template
	 * @param properties	list of properties to insert into the template
	 */
	public Specification(Map<String, String> paramsList, List<Property> properties) {
		this(paramsList.get("id"), paramsList.get("name"), paramsList.get("uri"));
		this.properties = properties;
	}
	
	@Override
	public Element text(Document doc) {
		Element spec = doc.createElement("specification");
		spec.setAttribute("version", "1.0");
		
		Element ID = doc.createElement("id");
		ID.setAttribute("schemeID", this.defaulSchemeID);
		ID.appendChild(doc.createTextNode(this.ID));
		
		Element name = doc.createElement("name");
		name.appendChild(doc.createTextNode(this.name));
		
		Element uri = doc.createElement("uri");
		uri.appendChild(doc.createTextNode(this.URI));
		
		Element properties = doc.createElement("properties");
		
		for (Property prop : this.properties) {
			if (prop.getName() != null) {
				Element s = prop.text(doc);
				properties.appendChild(s);
			}
			
		}
		
		spec.appendChild(ID);
		spec.appendChild(name);
		spec.appendChild(uri);
		
		
		spec.appendChild(properties);
		return spec;
	}

}
