package com.spring.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import com.spring.dataaccess.ResourceWrap;
import com.spring.utils.Configs;






/**
 * This class provides an easy access to the properties of an UrbanDataset
 * of the SCPS framework. This class is used to collect information to
 * build templates of the exchange messages and schematron files of the
 * UrbanDataset  
 * 
 * @author Raffaele Ianniello
 */
public class UrbanDataset {
	
	ResourceWrap res;
	String name;
	
	String path;
	String ompath;
	String uri;
	
	
	private void variableInitialize() {
		path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ompath = Configs.getInstance().getProps().getProperty("om");
		uri = Configs.getInstance().getProps().getProperty("uri");
	}
	
	/**
	 * Constructs a UrbanDataset object from the name of a UrbanDataset
	 *
	 * @param name	the name of the UrbanDataset
	 */
	public UrbanDataset(String name) { //test if defined on server?
		this.variableInitialize();
		this.name = name;
		this.res = new ResourceWrap(path, name);
	}
	
	/**
	 * Constructs a UrbanDataset object from the Resource object of a UrbanDataset
	 * 
	 * @param resource	the Resource that contains the UrbanDataset
	 */
	public UrbanDataset(Resource resource) {
		this.variableInitialize();
		this.name = resource.getLocalName();
		this.res = new ResourceWrap(resource);
	}
	
	/**
	 * Constructs a UrbanDataset object from the ResourceWrap object  of a UrbanDataset
	 * 
	 * @param resWrap	the ResourceWrap that contains the UrbanDataset
	 */
	public UrbanDataset(ResourceWrap resWrap) {
		this.variableInitialize();
		this.name = resWrap.getName()[1];
		this.res = resWrap;
	}
	
	/**
	 * Returns the name of the UrbanDataset
	 * 
	 * @return	the string containing the name of the UrbanDataset
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieve all the properties of an Urban Dataset
	 * 
	 * @return	the map that links a Property to a list of its Resources object of the relation
	 */
    public Map<Property, List<Resource>> getProperties() {
        Map<Property, List<Resource>> propertyMapUD = res.getPropertiesValues();
        return propertyMapUD;
    }

    /**
	 * Retrieve the map with the direct subproperties of the properties 
	 * 'hasUrbanDatasetProperty' and 'hasContextProperty' of an Urban Dataset
	 * 
	 * @return	the map that links a Property to a list of its Resources object of the relation
	 */
    public Map<Property, List<Resource>> getSpecificProperties() {
    	return Utils.getProperties(res);
    }
	
	/**
	 * Retrieve the map with the direct subproperties of the properties 
	 * 'hasUrbanDatasetProperty' and 'hasContextProperty' of an Urban Dataset
	 * 
	 * @param subproperty	choose to search recursively for subproperties or not
	 * @return	the list of the Resources object of the relations with the UrbanDataset as subject
	 */
	public List<Resource> getSpecificSubProperties(boolean subproperty) {
    	return Utils.searchSubProperty(res, subproperty);
    }
	
	/**
	 * Retrieve the list of Resource that are first level properties of the UrbanDataset
	 * 
	 * @return the list of first level properties
	 */
	public List<Resource> getFirstLevelPropNameList() {
		// list of all properties without subproperties
		List<Resource> listresnoSub = this.getSpecificSubProperties(false);
		
		// list of all properties without subproperties and coordinates and period
		List<Resource> listresNoSubNoCoordPeriod = new ArrayList<Resource>(listresnoSub);
		Collections.copy(listresNoSubNoCoordPeriod, listresnoSub);
		listresNoSubNoCoordPeriod.removeAll(Utils.coordSubprop());
		listresNoSubNoCoordPeriod.removeAll(Utils.periodSubprop());
		
//		System.out.println(listresNoSubNoCoordPeriod);
		return listresNoSubNoCoordPeriod;
	}

}
