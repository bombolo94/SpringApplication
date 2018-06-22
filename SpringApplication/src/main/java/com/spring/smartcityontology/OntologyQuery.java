package com.spring.smartcityontology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Resource;

import com.spring.dataaccess.ResourceWrap;
import com.spring.utils.Configs;


/**
 * This class consists of static methods that operate on SCPS-UrbanDataset ontology. 
 * It provides methods to access basic structure of the ontology and help explore it.
  * 
 * @author Raffaele Ianniello
 */
public class OntologyQuery {
	
	//general instances of class
	/**
	 * Retrieve the instances of a particular class passed as parameter  
	 * 
	 * @param entity	the class of the instances to retrieve
	 * @param limit	the maximum number of values to retrieve (0 if no limit)
	 * @return	the list instances 
	 */
	public static List<Resource> getInstancesByClassName(String entity, int limit){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path+entity);
		List<Resource> res = ud.getInstances(limit);
		return res;
	}
	
	/**
	 * Retrieve the instances of DataType class  
	 * 
	 * @param limit	the maximum number of values to retrieve (0 if no limit)
	 * @return	the list DataType instances 
	 */
	public static List<Resource> getDataTypeInstances(int limit){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ita = new ResourceWrap(path + "DataType");
		List<Resource> res = ita.getInstances(limit);
		return res;
	}
	
	//urbandataset
	/**
	 * Retrieve the subclasses of UrbanDataset class  
	 * 
	 * @return	the list of UrbanDataset subclasses 
	 */
	public static List<Resource> getUrbanDatasetSubClasses(){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "UrbanDataset");
		List<Resource> res = ud.getSubClasses();
		return res;
	}
	
	/**
	 * Retrieve the instances of subclasses of UrbanDataset class  
	 * 
	 * @return	the list of instances of UrbanDataset subclasses 
	 */
	public static Map<Resource, List<Resource>> getUrbanDatasetInstancesSubClasses(){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "UrbanDataset");
		List<Resource> subclasses = ud.getSubClasses();
		Map<Resource, List<Resource>> mappa = new HashMap<>();
		for(Resource el : subclasses){
			ResourceWrap elw = new ResourceWrap(el);
			List<Resource> inst = elw.getInstances(0);
			mappa.put(el, inst);
		}
		return mappa;
	}
	
	//aggregation
	/**
	 * Retrieve the subclasses of Aggregation class  
	 * 
	 * @return	the list of Aggregation subclasses 
	 */
	public static List<Resource> getAggregationSubClasses(){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "Aggregation");
		List<Resource> res = ud.getSubClasses();
		return res;
	}
	
	/**
	 * Retrieve the instances of subclasses of Aggregation class  
	 * 
	 * @return	the list of instances of Aggregation subclasses 
	 */
	public static Map<Resource, List<Resource>> getAggregationInstancesSubClasses(){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "Aggregation");
		List<Resource> subclasses = ud.getSubClasses();
		Map<Resource, List<Resource>> mappa = new HashMap<>();
		for(Resource el : subclasses){
			ResourceWrap elw = new ResourceWrap(el);
			List<Resource> inst = elw.getInstances(0);
			mappa.put(el, inst);
		}
		return mappa;
	}
	
	//aggregation space and time
	/**
	 * Retrieve the instances of SpaceAggregation class  
	 * 
	 * @param limit	the maximum number of values to retrieve (0 if no limit)
	 * @return	the list SpaceAggregation instances 
	 */
	public static List<Resource> getSpaceAggregationInstances(int limit){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "SpaceAggregation");
		List<Resource> res = ud.getInstances(limit);
		return res;
	}
	
	/**
	 * Retrieve the instances of TimeAggregation class  
	 * 
	 * @param limit	the maximum number of values to retrieve (0 if no limit)
	 * @return	the list TimeAggregation instances 
	 */
	public static List<Resource> getTimeAggregationInstances(int limit){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "TimeAggregation");
		List<Resource> res = ud.getInstances(limit);
		return res;
	}
	
	//appl context
	/**
	 * Retrieve the instances of ApplicationContext class  
	 * 
	 * @param limit	the maximum number of values to retrieve (0 if no limit)
	 * @return	the list ApplicationContext instances 
	 */
	public static List<Resource> getApplicationContextInstances(int limit){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "ApplicationContext");
		List<Resource> res = ud.getInstances(limit);
		return res;
	}
	
	//property
	//property context e urban
	/**
	 * Retrieve the subclasses of Property class  
	 * 
	 * @return	the list of Property subclasses 
	 */
	public static List<Resource> getPropertySubClasses(){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "Property");
		List<Resource> res = ud.getSubClasses();
		return res;
	}
	
	/**
	 * Retrieve the instances of subclasses of Property class  
	 * 
	 * @return	the list of instances of Property subclasses 
	 */
	public static Map<Resource, List<Resource>> getPropertyInstancesSubClasses(){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "Property");
		List<Resource> subclasses = ud.getSubClasses();
		Map<Resource, List<Resource>> mappa = new HashMap<>();
		for(Resource el : subclasses){
			ResourceWrap elw = new ResourceWrap(el);
			List<Resource> inst = elw.getInstances(0);
			mappa.put(el, inst);
		}
		return mappa;
	}
	
	/**
	 * Retrieve the instances of ContextProperty class  
	 * 
	 * @param limit	the maximum number of values to retrieve (0 if no limit)
	 * @return	the list of ContextProperty instances 
	 */
	public static List<Resource> getContextPropertyInstances(int limit){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "ContextProperty");
		List<Resource> res = ud.getInstances(limit);
		return res;
	}
	
	/**
	 * Retrieve the instances of UrbanDatasetProperty class  
	 * 
	 * @param limit	the maximum number of values to retrieve (0 if no limit)
	 * @return	the list of UrbanDatasetProperty instances 
	 */
	public static List<Resource> getUrbanDatasetPropertyInstances(int limit){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "UrbanDatasetProperty");
		List<Resource> res = ud.getInstances(limit);
		return res;
	}
	
	//producer
	/**
	 * Retrieve the instances of Producer class  
	 * 
	 * @param limit	the maximum number of values to retrieve (0 if no limit)
	 * @return	the list of Producer instances 
	 */
	public static List<Resource> getProducerInstances(int limit){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "Producer");
		List<Resource> res = ud.getInstances(limit);
		return res;
	}
	
	//urbandatasetcontext
	/**
	 * Retrieve the instances of UrbanDatasetContext class  
	 * 
	 * @param limit	the maximum number of values to retrieve (0 if no limit)
	 * @return	the list of UrbanDatasetContext instances 
	 */
	public static List<Resource> getUrbanDatasetContextInstances(int limit){
		String path = Configs.getInstance().getProps().getProperty("scpsnamespace");
		ResourceWrap ud = new ResourceWrap(path + "UrbanDatasetContext");
		List<Resource> res = ud.getInstances(limit);
		return res;
	}
	
}
