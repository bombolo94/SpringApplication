package com.spring.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import com.spring.dataaccess.ResourceWrap;
import com.spring.utils.Configs;



/**
 * Utility static class with functions for retrieve list of properties from UrbanDatasets
 * 
 * @author Raffaele Ianniello
 *
 */
public class Utils {
	
	private static String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
	private static String ompath = Configs.getInstance().getProps().getProperty("om");
	private static String uri = Configs.getInstance().getProps().getProperty("uri");
	
	/**
	 * Build a map that associate resource objects to the properties hasUrbanDatasetProperty and 
	 * hasContextProperty of the SCP platform.
	 * 
	 * @param resource	the ResourceWrap object of the UrbanDataset
	 * @return the map that associate resource objects to the properties 'hasUrbanDatasetProperty' and 'hasContextProperty'
	 */
	public static Map<Property, List<Resource>> getProperties(ResourceWrap resource) {
		Property propUD = ResourceFactory.createProperty(path + "hasUrbanDatasetProperty");
		Property propC = ResourceFactory.createProperty(path + "hasContextProperty");
		List<Resource> propertyMapUD = resource.getValues(propUD);
		List<Resource> propertyMapC = resource.getValues(propC);
		Map<Property, List<Resource>> mapResult = new HashMap<Property, List<Resource>>() {{
			put(propUD, propertyMapUD);
			put(propC, propertyMapC);
		}};
		return mapResult;
	}
	
	/**
	 * Build a list of Resource objects of the that are objects of properties and subproperties 
	 * of a UrbanDataset of the relations 'hasUrbanDatasetProperty' and 'hasContextProperty'.
	 * 
	 * @param resource	the UrbanDataset resource to search for properties and subproperties
	 * @param subproperty	select if search for subproperties
	 * @return the list of Resources
	 */
	public static List<Resource> searchSubProperty(ResourceWrap resource, boolean subproperty) {
		Property propUDkey = ResourceFactory.createProperty(path + "hasUrbanDatasetProperty");
		Property propCkey = ResourceFactory.createProperty(path + "hasContextProperty");
		Map<Property, List<Resource>> propertyMapUDinterest = getProperties(resource);
		List<Resource> listProperty = new ArrayList<Resource>();
		listProperty.addAll(propertyMapUDinterest.get(propUDkey));
		listProperty.addAll(propertyMapUDinterest.get(propCkey));
		
		if (subproperty) {
			for (Resource propObj : propertyMapUDinterest.get(propUDkey)) {
				ResourceWrap propObjWP = new ResourceWrap(propObj);
				listProperty.addAll(searchSubProperty(propObjWP, true));
			}
		}
		
		return listProperty;
	}
	
	/**
	 * Build a string composed by the names of list elements divided by separator
	 * 
	 * @param list	the list of strings to join in a single string
	 * @param separator	the separator to use between list elements
	 * @return the string containing the elements of the list
	 */
	public static String listFlattener(List<String> list, String separator) {
		StringBuilder flattenedList = new StringBuilder();
		list.forEach(item->{
			flattenedList.append(item + separator);
		});
		String returnString = flattenedList.toString();
		return returnString.substring(0, returnString.length() - separator.length());
	}
	
	/**
	 * Retrieve the list of Resource that are subproperties of the resource period defined inside the ontology
	 * 
	 * @return the list of Resource of subproperties of period
	 */
	public static List<Resource> periodSubprop() {
		Property propUDkey = ResourceFactory.createProperty(path + "hasUrbanDatasetProperty");
		Property propCkey = ResourceFactory.createProperty(path + "hasContextProperty");
		
		// lists of subproperties of period
		ResourceWrap period = new ResourceWrap(path + "period");
		Map<Property, List<Resource>> listPeriodProp = Utils.getProperties(period);
		List<Resource> listPeriodProperty = new ArrayList<Resource>();
		listPeriodProperty.add(period.getResource());
		listPeriodProperty.addAll(listPeriodProp.get(propUDkey));
		listPeriodProperty.addAll(listPeriodProp.get(propCkey)); 	//<----
		
		return listPeriodProperty;
	}
	
	/**
	 * Retrieve the list of Resource that are subproperties of the resource coordinate defined inside the ontology
	 * 
	 * @return the list of Resource of subproperties of coordinate
	 */
	public static List<Resource> coordSubprop() {
		Property propUDkey = ResourceFactory.createProperty(path + "hasUrbanDatasetProperty");
		Property propCkey = ResourceFactory.createProperty(path + "hasContextProperty");
		
		// lists of subproperties of coordinates
		ResourceWrap coordinates = new ResourceWrap(path + "coordinates");
		Map<Property, List<Resource>> listCoordProp = Utils.getProperties(coordinates);
		List<Resource> listCoordProperty = new ArrayList<Resource>();
		listCoordProperty.add(coordinates.getResource());
		listCoordProperty.addAll(listCoordProp.get(propUDkey));
		listCoordProperty.addAll(listCoordProp.get(propCkey)); 		//<----
		
		return listCoordProperty;
	}

}
