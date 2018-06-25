package com.spring.template.schematron;


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import com.spring.core.IBuilderTemplate;
import com.spring.core.UrbanDataset;
import com.spring.core.Utils;
import com.spring.dataaccess.ResourceWrap;
import com.spring.utils.Configs;




/**
 * This class provides an easy way to build the schematron template from the 
 * UrbanDataset object of the SCPS framework.
 *      
 * 
 * @author Raffaele Ianniello
 */
public class SchematronBuilder implements IBuilderTemplate {

	String scpnamespace;
	String omnamespace;
	String uri;
	String filename;
	
	// UrbanDataset properties
	UrbanDataset ud;

	private void variableInitialize() {
		scpnamespace = Configs.getInstance().getProps().getProperty("scpsnamespace");
		omnamespace = Configs.getInstance().getProps().getProperty("om");
		uri = Configs.getInstance().getProps().getProperty("uri");
		filename = Configs.getInstance().getProps().getProperty("schematronFile");
	}
	
	/**
	 * Constructs a SchematronBuilder object passing the UrbanDataset name 
	 * 
	 * @param name	the name of the UrbanDataset
	 */
	public SchematronBuilder(String name) {
		variableInitialize();
		
		this.ud = new UrbanDataset(name);
		
	}
	
	/**
	 * Constructs a SchematronBuilder object passing the UrbanDataset object 
	 * 
	 * @param ud	the UrbanDataset object
	 */
	public SchematronBuilder(UrbanDataset ud) {
		variableInitialize();
		
		this.ud = ud;
	}

	@Override
	public String buildS() {
		System.out.println("Retrieving data from ontology to compile Schematron template");
		Property propUDkey = ResourceFactory.createProperty(scpnamespace + "hasUrbanDatasetProperty");
		Property propCkey = ResourceFactory.createProperty(scpnamespace + "hasContextProperty");
		
        Map<Property, List<Resource>> propertyMapUD = ud.getProperties();

		
		// list of all properties and subproperties with repetitions
		List<Resource> listres = ud.getSpecificSubProperties(true);
		
		// set of all properties and subproperties with no repetitions
		Set<Resource> setres = new LinkedHashSet<Resource>(listres);
		
		// list of all properties and subproperties with repetitions but without coordinates and period and subproperties
		List<Resource> listresNoCoordPeriod = new ArrayList<Resource>(listres);
		Collections.copy(listresNoCoordPeriod, listres);
		listresNoCoordPeriod.removeAll(Utils.coordSubprop());
		listresNoCoordPeriod.removeAll(Utils.periodSubprop());
		
		List<Resource> listresNoSubNoCoordPeriod = ud.getFirstLevelPropNameList();
		
		String propertyNum = ""+listresNoCoordPeriod.size();
		String propertyDefNum = ""+setres.size();
		List<String> propNameList = setres.stream().map(Resource::getLocalName).collect(Collectors.toList()); // equivalent to propNameSet
		List<String> firstLevelPropNameList = listresNoSubNoCoordPeriod.stream().map(Resource::getLocalName).collect(Collectors.toList());
		
		Property dataType = ResourceFactory.createProperty(scpnamespace + "hasDataType");
		Property um = ResourceFactory.createProperty(omnamespace + "hasUnit");
		Property codeList = ResourceFactory.createProperty(scpnamespace + "hasCodeList");
		
		List<String> dataTypeSet = new ArrayList<String>();
		List<String> umSet = new ArrayList<String>();
		List<String> codeListSet = new ArrayList<String>();
		List<String> subPropNameSet = new ArrayList<String>();
		
		for (Resource el: setres) {
			// get dataTypes
			if ((new ResourceWrap(el)).getValues(dataType).isEmpty()) {
				ResourceWrap resObjWrap = new ResourceWrap(el);
				Map<Property, List<Resource>> propertyMapUDinterest = Utils.getProperties(resObjWrap);
				List<Resource> listProperty = new ArrayList<Resource>();
				listProperty.addAll(propertyMapUDinterest.get(propUDkey));
				listProperty.addAll(propertyMapUDinterest.get(propCkey));
				dataTypeSet.add("complex-" + listProperty.size());
				// get concat prop-subprop
				for (Resource elSubP: listProperty) {
					subPropNameSet.add(el.getLocalName()+elSubP.getLocalName());
				}
			} else {
				String type = (new ResourceWrap(el)).getValues(dataType).get(0).getLocalName();
				dataTypeSet.add(type);
			}
			// get Unit of measure
			if ((new ResourceWrap(el)).getValues(um).isEmpty()) {
				umSet.add("adimensionale");
			} else {
				String umVal = (new ResourceWrap(el)).getValues(um).get(0).getLocalName();
				umSet.add(umVal);
			}
			// get codeList
			if ((new ResourceWrap(el)).getValues(codeList).isEmpty()) {
				codeListSet.add("null");
			} else {
				String cdVal = (new ResourceWrap(el)).getValues(codeList).get(0).getLocalName();
				codeListSet.add(cdVal);
			}
			
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		Map<String, String> replacements = new HashMap<String, String>() {{
			put("$$nomeUrbanDataset", propertyMapUD.get(ResourceFactory.createProperty(scpnamespace + "name")).get(0).toString());
			put("$$dataDiGenerazione", dateFormat.format(date));
		    put("$$udID", ud.getName());
		    put("$$udName", propertyMapUD.get(ResourceFactory.createProperty(scpnamespace + "name")).get(0).toString());
		    put("$$udURI", uri+ud.getName());
		    put("$$udPropN", ""+propertyNum);
		    put("$$udPropDefN", ""+propertyDefNum);
		    put("$$udPropNameList", Utils.listFlattener(propNameList, " "));
		    put("$$udFirstPropNameList", Utils.listFlattener(firstLevelPropNameList, " "));
		    put("$$udPropNameSet", Utils.listFlattener(propNameList, ", "));
		    put("$$udDataTypeSet", Utils.listFlattener(dataTypeSet, ", "));
		    put("$$udUmSet", Utils.listFlattener(umSet, ", "));
		    put("$$codeList", Utils.listFlattener(codeListSet, ", "));
		    put("$$udSubPropNameSet", Utils.listFlattener(subPropNameSet, ", "));
		}};
		
		
		Schema s = new Schema(filename);
		s.replace(replacements);
		String fileC = s.saveContentFile();
		return fileC;

	}

	@Override
	public void build() {
		System.out.println("Retrieving data from ontology to compile Schematron template");
		Property propUDkey = ResourceFactory.createProperty(scpnamespace + "hasUrbanDatasetProperty");
		Property propCkey = ResourceFactory.createProperty(scpnamespace + "hasContextProperty");
		
        Map<Property, List<Resource>> propertyMapUD = ud.getProperties();

		
		// list of all properties and subproperties with repetitions
		List<Resource> listres = ud.getSpecificSubProperties(true);
		
		// set of all properties and subproperties with no repetitions
		Set<Resource> setres = new LinkedHashSet<Resource>(listres);
		
		// list of all properties and subproperties with repetitions but without coordinates and period and subproperties
		List<Resource> listresNoCoordPeriod = new ArrayList<Resource>(listres);
		Collections.copy(listresNoCoordPeriod, listres);
		listresNoCoordPeriod.removeAll(Utils.coordSubprop());
		listresNoCoordPeriod.removeAll(Utils.periodSubprop());
		
		List<Resource> listresNoSubNoCoordPeriod = ud.getFirstLevelPropNameList();
		
		String propertyNum = ""+listresNoCoordPeriod.size();
		String propertyDefNum = ""+setres.size();
		List<String> propNameList = setres.stream().map(Resource::getLocalName).collect(Collectors.toList()); // equivalent to propNameSet
		List<String> firstLevelPropNameList = listresNoSubNoCoordPeriod.stream().map(Resource::getLocalName).collect(Collectors.toList());
		
		Property dataType = ResourceFactory.createProperty(scpnamespace + "hasDataType");
		Property um = ResourceFactory.createProperty(omnamespace + "hasUnit");
		Property codeList = ResourceFactory.createProperty(scpnamespace + "hasCodeList");
		
		List<String> dataTypeSet = new ArrayList<String>();
		List<String> umSet = new ArrayList<String>();
		List<String> codeListSet = new ArrayList<String>();
		List<String> subPropNameSet = new ArrayList<String>();
		
		for (Resource el: setres) {
			// get dataTypes
			if ((new ResourceWrap(el)).getValues(dataType).isEmpty()) {
				ResourceWrap resObjWrap = new ResourceWrap(el);
				Map<Property, List<Resource>> propertyMapUDinterest = Utils.getProperties(resObjWrap);
				List<Resource> listProperty = new ArrayList<Resource>();
				listProperty.addAll(propertyMapUDinterest.get(propUDkey));
				listProperty.addAll(propertyMapUDinterest.get(propCkey));
				dataTypeSet.add("complex-" + listProperty.size());
				// get concat prop-subprop
				for (Resource elSubP: listProperty) {
					subPropNameSet.add(el.getLocalName()+elSubP.getLocalName());
				}
			} else {
				String type = (new ResourceWrap(el)).getValues(dataType).get(0).getLocalName();
				dataTypeSet.add(type);
			}
			// get Unit of measure
			if ((new ResourceWrap(el)).getValues(um).isEmpty()) {
				umSet.add("adimensionale");
			} else {
				String umVal = (new ResourceWrap(el)).getValues(um).get(0).getLocalName();
				umSet.add(umVal);
			}
			// get codeList
			if ((new ResourceWrap(el)).getValues(codeList).isEmpty()) {
				codeListSet.add("null");
			} else {
				String cdVal = (new ResourceWrap(el)).getValues(codeList).get(0).getLocalName();
				codeListSet.add(cdVal);
			}
			
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		Map<String, String> replacements = new HashMap<String, String>() {{
			put("$$nomeUrbanDataset", propertyMapUD.get(ResourceFactory.createProperty(scpnamespace + "name")).get(0).toString());
			put("$$dataDiGenerazione", dateFormat.format(date));
		    put("$$udID", ud.getName());
		    put("$$udName", propertyMapUD.get(ResourceFactory.createProperty(scpnamespace + "name")).get(0).toString());
		    put("$$udURI", uri+ud.getName());
		    put("$$udPropN", ""+propertyNum);
		    put("$$udPropDefN", ""+propertyDefNum);
		    put("$$udPropNameList", Utils.listFlattener(propNameList, " "));
		    put("$$udFirstPropNameList", Utils.listFlattener(firstLevelPropNameList, " "));
		    put("$$udPropNameSet", Utils.listFlattener(propNameList, ", "));
		    put("$$udDataTypeSet", Utils.listFlattener(dataTypeSet, ", "));
		    put("$$udUmSet", Utils.listFlattener(umSet, ", "));
		    put("$$codeList", Utils.listFlattener(codeListSet, ", "));
		    put("$$udSubPropNameSet", Utils.listFlattener(subPropNameSet, ", "));
		}};
		
		
		Schema s = new Schema(filename);
		s.replace(replacements);
		
	}
	
	public File buildF() {
		System.out.println("Retrieving data from ontology to compile Schematron template");
		Property propUDkey = ResourceFactory.createProperty(scpnamespace + "hasUrbanDatasetProperty");
		Property propCkey = ResourceFactory.createProperty(scpnamespace + "hasContextProperty");
		
        Map<Property, List<Resource>> propertyMapUD = ud.getProperties();

		
		// list of all properties and subproperties with repetitions
		List<Resource> listres = ud.getSpecificSubProperties(true);
		
		// set of all properties and subproperties with no repetitions
		Set<Resource> setres = new LinkedHashSet<Resource>(listres);
		
		// list of all properties and subproperties with repetitions but without coordinates and period and subproperties
		List<Resource> listresNoCoordPeriod = new ArrayList<Resource>(listres);
		Collections.copy(listresNoCoordPeriod, listres);
		listresNoCoordPeriod.removeAll(Utils.coordSubprop());
		listresNoCoordPeriod.removeAll(Utils.periodSubprop());
		
		List<Resource> listresNoSubNoCoordPeriod = ud.getFirstLevelPropNameList();
		
		String propertyNum = ""+listresNoCoordPeriod.size();
		String propertyDefNum = ""+setres.size();
		List<String> propNameList = setres.stream().map(Resource::getLocalName).collect(Collectors.toList()); // equivalent to propNameSet
		List<String> firstLevelPropNameList = listresNoSubNoCoordPeriod.stream().map(Resource::getLocalName).collect(Collectors.toList());
		
		Property dataType = ResourceFactory.createProperty(scpnamespace + "hasDataType");
		Property um = ResourceFactory.createProperty(omnamespace + "hasUnit");
		Property codeList = ResourceFactory.createProperty(scpnamespace + "hasCodeList");
		
		List<String> dataTypeSet = new ArrayList<String>();
		List<String> umSet = new ArrayList<String>();
		List<String> codeListSet = new ArrayList<String>();
		List<String> subPropNameSet = new ArrayList<String>();
		
		for (Resource el: setres) {
			// get dataTypes
			if ((new ResourceWrap(el)).getValues(dataType).isEmpty()) {
				ResourceWrap resObjWrap = new ResourceWrap(el);
				Map<Property, List<Resource>> propertyMapUDinterest = Utils.getProperties(resObjWrap);
				List<Resource> listProperty = new ArrayList<Resource>();
				listProperty.addAll(propertyMapUDinterest.get(propUDkey));
				listProperty.addAll(propertyMapUDinterest.get(propCkey));
				dataTypeSet.add("complex-" + listProperty.size());
				// get concat prop-subprop
				for (Resource elSubP: listProperty) {
					subPropNameSet.add(el.getLocalName()+elSubP.getLocalName());
				}
			} else {
				String type = (new ResourceWrap(el)).getValues(dataType).get(0).getLocalName();
				dataTypeSet.add(type);
			}
			// get Unit of measure
			if ((new ResourceWrap(el)).getValues(um).isEmpty()) {
				umSet.add("adimensionale");
			} else {
				String umVal = (new ResourceWrap(el)).getValues(um).get(0).getLocalName();
				umSet.add(umVal);
			}
			// get codeList
			if ((new ResourceWrap(el)).getValues(codeList).isEmpty()) {
				codeListSet.add("null");
			} else {
				String cdVal = (new ResourceWrap(el)).getValues(codeList).get(0).getLocalName();
				codeListSet.add(cdVal);
			}
			
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		Map<String, String> replacements = new HashMap<String, String>() {{
			put("$$nomeUrbanDataset", propertyMapUD.get(ResourceFactory.createProperty(scpnamespace + "name")).get(0).toString());
			put("$$dataDiGenerazione", dateFormat.format(date));
		    put("$$udID", ud.getName());
		    put("$$udName", propertyMapUD.get(ResourceFactory.createProperty(scpnamespace + "name")).get(0).toString());
		    put("$$udURI", uri+ud.getName());
		    put("$$udPropN", ""+propertyNum);
		    put("$$udPropDefN", ""+propertyDefNum);
		    put("$$udPropNameList", Utils.listFlattener(propNameList, " "));
		    put("$$udFirstPropNameList", Utils.listFlattener(firstLevelPropNameList, " "));
		    put("$$udPropNameSet", Utils.listFlattener(propNameList, ", "));
		    put("$$udDataTypeSet", Utils.listFlattener(dataTypeSet, ", "));
		    put("$$udUmSet", Utils.listFlattener(umSet, ", "));
		    put("$$codeList", Utils.listFlattener(codeListSet, ", "));
		    put("$$udSubPropNameSet", Utils.listFlattener(subPropNameSet, ", "));
		}};
		
		
		Schema s = new Schema(filename);
		s.replace(replacements);
		File f = s.saveFile(ud.getName()+".sch");
		return f;
		
	
	}

}
