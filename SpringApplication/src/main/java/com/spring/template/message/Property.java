package com.spring.template.message;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class describe the property concept that is used to generate 
 * the message template and schematron for an UrbanDataset of the 
 * Smart City Platform. It provides a convenient way to group and 
 * retrieve all the information needed to build those documents 
 * from the corresponding ontology.
 * A property can reference another one and therefore have a subproperty.
 * A subproperty must be represented in a different way from a property, 
 * so must be kept trace of the relation. 
 * 
 * @author Raffaele Ianniello
 *
 */
public class Property {
	
	String name;
	String description;
	String dataType;
	String unitOfMeasure;
	String codeList;
	boolean subproperties;
	List<Property> subporpertiesList;
	
	/**
	 * Constructs an empty property 
	 */
	public Property() {
		this.subproperties = false;
		this.subporpertiesList = new ArrayList<Property>();
	}
	
	/**
	 * Constructs a Property setting up the name and the description
	 * 
	 * @param name	name of the Property
	 * @param description	description of the Property
	 */
	public Property(String name, String description) {
		this();
		this.name = name;
		this.description = description;
	}
	
	/**
	 * Returns the description of the Property
	 * @return	the description of the Property
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of the Property
	 * @param description	the description to associate to the Property
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the name of the Property
	 * @return	the name of the Property
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the Property
	 * @param name	the name to associate to the Property
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Add a subproperty reference to the property
	 * @param p	the property to reference
	 */
	public void addSubproperty(Property p) {
		this.subproperties = true;
		this.subporpertiesList.add(p);
	}
	
	/**
	 * Returns the data type of the Property
	 * @return	the data type of the Property
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Set the data type of the Property
	 * @param dataType	the data type to associate to the Property
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * Returns the unit of measure of the Property
	 * @return	the unit of measure of the Property
	 */
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	/**
	 * Set the unit of measure of the Property
	 * @param unitOfMeasure	the unit of measure to associate to the Property
	 */
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	
	/**
	 * Returns the code list of the Property
	 * @return	the code list of the Property
	 */
	public String getCodeList() {
		return codeList;
	}
	
	/**
	 * Set the code list of the Property
	 * @param codeList	the code list to associate to the Property
	 */
	public void setCodeList(String codeList) {
		this.codeList = codeList;
	}
	
	
	public Element text(Document doc) {
		Element def = doc.createElement("propertyDefinition");
		
		Element name = doc.createElement("propertyName");
		name.appendChild(doc.createTextNode(this.name));
		
		Element descr = doc.createElement("propertyDescription");
		descr.appendChild(doc.createTextNode(this.description));
		
		def.appendChild(name);
		def.appendChild(descr);
		
		if (this.subproperties == false) {
//			if (this.dataType != null) {
			Element dataType = doc.createElement("dataType");
//			if (this.dataType == null)
//				dataType.appendChild(doc.createTextNode("test"));
//			else
			dataType.appendChild(doc.createTextNode(this.dataType));
			
			def.appendChild(dataType);
//			}
			Element unit = doc.createElement("unitOfMeasure");
			if (this.unitOfMeasure != null) {
				unit.appendChild(doc.createTextNode(this.unitOfMeasure));
			} else {
				unit.appendChild(doc.createTextNode("adimensionale"));
			}
			def.appendChild(unit);
		} else {
			Element subP = doc.createElement("subProperties");
			for (Property prop : subporpertiesList) {
				Element subPName = doc.createElement("propertyName");
				subPName.appendChild(doc.createTextNode(prop.getName()));
				subP.appendChild(subPName);
			}
			
			
			def.appendChild(subP);
		}
		
		return def;
	}
	
	public String toString() {
		return this.name + "; " + this.description + "; " + this.dataType + "; " + this.unitOfMeasure + "; " + this.codeList + subporpertiesList;
	}

}
