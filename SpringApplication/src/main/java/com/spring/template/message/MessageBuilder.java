package com.spring.template.message;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Property;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.spring.core.IBuilderTemplate;
import com.spring.core.UrbanDataset;
import com.spring.core.Utils;
import com.spring.utils.Configs;
import com.spring.dataaccess.ResourceWrap;



/**
 * This class provides an easy way to build the message template from the 
 * UrbanDataset object of the SCPS framework.
 *      
 * @author Raffaele Ianniello
 */
public class MessageBuilder implements IBuilderTemplate {
	// config paramas from property file
	String scpnamespace;
	String omnamespace;
	String xmlns;
	String uri;
	
	// UrbanDataset properties
	UrbanDataset ud;
	String name;
	Map<Property, List<Resource>> listProperties;
	
	/**
	 * Constructs a MessageBuilder object from the name of a UrbanDataset
	 * 
	 * @param name	the name of the UrbanDataset
	 */
	public MessageBuilder(String name) {
		this.scpnamespace = Configs.getInstance().getProps().getProperty("scpsnamespace");
		this.omnamespace = Configs.getInstance().getProps().getProperty("om");
		this.xmlns = Configs.getInstance().getProps().getProperty("xmlns");
		this.uri = Configs.getInstance().getProps().getProperty("uri");
		this.name = name;
		this.ud = new UrbanDataset(this.name);
		this.listProperties = this.ud.getProperties();
	}
	
	/**
	 * Constructs a MessageBuilder object from a UrbanDataset object
	 *
	 * @param ud	the UrbanDataset object
	 */
	public MessageBuilder(UrbanDataset ud) {
		this.scpnamespace = Configs.getInstance().getProps().getProperty("scpsnamespace");
		this.omnamespace = Configs.getInstance().getProps().getProperty("om");
		this.xmlns = Configs.getInstance().getProps().getProperty("xmlns");
		this.uri = Configs.getInstance().getProps().getProperty("uri");
		this.name = ud.getName();
		this.ud = ud;
		this.listProperties = this.ud.getProperties();
	}
	
	// retrieve general info of UrbanDataset 
	private Map<String, String> templateIDPropertires() {
		List<String> templateProperties = new ArrayList<String>();
		templateProperties.add("id");
		templateProperties.add("name");
		templateProperties.add("uri");
		
		Map<String, String> result = new HashMap<String, String>();
		for (String el : templateProperties) {
			if (el == "id") {
				String put = listProperties.get(ResourceFactory.createResource(scpnamespace+"UrbanDatasetVersion")).get(0).toString();
				result.put(el, this.name + "-" + put);
			} else if (el == "uri") {
				result.put(el, this.uri+this.name);
			} else
				result.put(el, listProperties.get(ResourceFactory.createResource(scpnamespace+el)).get(0).toString());
		}
		return result;
	}
	
	private List<com.spring.template.message.Property> templateProperties() {
		Property comment = ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#comment");
		Property propUDkey = ResourceFactory.createProperty(scpnamespace + "hasUrbanDatasetProperty");
		Property propCkey = ResourceFactory.createProperty(scpnamespace + "hasContextProperty");
		
		List<Resource> listres = ud.getSpecificSubProperties(true);
		
		// set of all properties and subproperties with no repetitions
		Set<Resource> setres = new LinkedHashSet<Resource>(listres);
//		List<String> propNameList = setres.stream().map(Resource::getLocalName).collect(Collectors.toList()); // equivalent to propNameSet
		Property dataType = ResourceFactory.createProperty(scpnamespace + "hasDataType");
		Property um = ResourceFactory.createProperty(omnamespace + "hasUnit");
		Property codeList = ResourceFactory.createProperty(scpnamespace + "hasCodeList");
		
		
		List<com.spring.template.message.Property> result = new ArrayList<com.spring.template.message.Property>();
		for (Resource el: setres) {
			com.spring.template.message.Property resProperty = new com.spring.template.message.Property();
			ResourceWrap resObjWrap = new ResourceWrap(el);
			resProperty.setName(resObjWrap.getName()[1]);
			resProperty.setDescription(resObjWrap.getPropertiesValues().get(comment).get(0).toString());
			// get dataTypes
			if (resObjWrap.getValues(dataType).isEmpty()) {
				Map<Property, List<Resource>> propertyMapUDinterest = Utils.getProperties(resObjWrap);
				List<Resource> listProperty = new ArrayList<Resource>();
				listProperty.addAll(propertyMapUDinterest.get(propUDkey));
				listProperty.addAll(propertyMapUDinterest.get(propCkey));
				// get concat prop-subprop
				for (Resource elSubP: listProperty) {
					ResourceWrap subresourceW = new ResourceWrap(elSubP);
					com.spring.template.message.Property resSubProperty = new com.spring.template.message.Property();
					resSubProperty.setName(subresourceW.getName()[1]);
					resProperty.addSubproperty(resSubProperty);

				}
			} else {
				String type = resObjWrap.getValues(dataType).get(0).getLocalName();
				resProperty.setDataType(type);
//				dataTypeSet.add(type);
			}
			// get Unit of measure
			if (resObjWrap.getValues(um).isEmpty()) {
//				resProperties.setUnitOfMeasure(null);
//				umSet.add("adimensionale");
			} else {
				String umVal = resObjWrap.getValues(um).get(0).getLocalName();
				resProperty.setUnitOfMeasure(umVal);
//				umSet.add(umVal);
			}
			// get codeList
			if (resObjWrap.getValues(codeList).isEmpty()) {
//				resProperties.setCodeList(null);
//				codeListSet.add("null");
			} else {
				String cdVal = resObjWrap.getValues(codeList).get(0).getLocalName();
				resProperty.setCodeList(cdVal);
//				codeListSet.add(cdVal);
			}
			result.add(resProperty);
			
		}
		return result;
	}
	
	// retrive firstlevel properties (without coord and period) 
//	private void getValuesProp() {
//		Property propUDkey = ResourceFactory.createProperty(scpnamespace + "hasUrbanDatasetProperty");
//		Property propCkey = ResourceFactory.createProperty(scpnamespace + "hasContextProperty");
//		
//		// lists of subproperties of coordinates and period
//		ResourceWrap period = new ResourceWrap(scpnamespace + "period");
//		ResourceWrap coordinates = new ResourceWrap(scpnamespace + "coordinates");
//		Map<Property, List<Resource>> listPeriodProp = Utils.getProperties(period);
//		Map<Property, List<Resource>> listCoordProp = Utils.getProperties(coordinates);
//		List<Resource> listPeriodProperty = new ArrayList<Resource>();
//		listPeriodProperty.add(period.getResource());
//		listPeriodProperty.addAll(listPeriodProp.get(propUDkey));
//		listPeriodProperty.addAll(listPeriodProp.get(propCkey)); 	//<----
//		List<Resource> listCoordProperty = new ArrayList<Resource>();
//		listCoordProperty.add(coordinates.getResource());
//		listCoordProperty.addAll(listCoordProp.get(propUDkey));
//		listCoordProperty.addAll(listCoordProp.get(propCkey)); 		//<----
//		
//		// list of all properties without subproperties
//		List<Resource> listresnoSub = ud.getSpecificSubProperties(false);
//		
//		// list of all properties without subproperties and coordinates and period
//		List<Resource> listresNoSubNoCoordPeriod = new ArrayList<Resource>(listresnoSub);
//		Collections.copy(listresNoSubNoCoordPeriod, listresnoSub);
//		listresNoSubNoCoordPeriod.removeAll(listCoordProperty);
//		listresNoSubNoCoordPeriod.removeAll(listPeriodProperty);
//	}
	
	
	public void build() {
//		getValuesProp(); //test build for values section
		System.out.println("Retrieving data from ontology to compile message template");
		Map<String, String> ids = this.templateIDPropertires();
		System.out.println(ids);
		List<com.spring.template.message.Property> t = this.templateProperties();
		
		String filename = ids.get("id") + "-Template.xml";
		try {
			System.out.println("Generating message file");
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			doc.setXmlStandalone(true);

			String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Timestamp(System.currentTimeMillis()));
			Comment comment = doc.createComment("\n" +
					"	File: " + filename +"\r\n" +
					"	Generato il: " + timeStamp +"\r\n" +
					"\r\n" +
					"	Descrizione: questo documento costituisce un XML Campione per l'Urban Dataset " + ids.get("id") + "\r\n" + 
					"	(sono stati valorizzati solo gli elementi e attributi che hanno valore fisso per questo \r\n" + 
					"	tipo di Urban Dataset, ad esempio gli elementi 'propertyName'; gli altri elementi e attributi, \r\n" + 
					"	ad esempio i 'val', devono essere valorizzati).\r\n");
			
			doc.appendChild(comment);
			
			Element UD = doc.createElement("UrbanDataset");
			
			//// Urban Dataset attributes definition
			Attr xmlnsxsi = doc.createAttribute("xmlns:xsi");
			xmlnsxsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
			UD.setAttributeNode(xmlnsxsi);
			Attr schemaLocation = doc.createAttribute("xsi:schemaLocation");
			schemaLocation.setValue(this.xmlns + " " + Configs.getInstance().getProps().getProperty("scpsaddr"));
			UD.setAttributeNode(schemaLocation);
			Attr xmlnsEl = doc.createAttribute("xmlns");
			xmlnsEl.setValue(this.xmlns);
			UD.setAttributeNode(xmlnsEl);
			
			doc.appendChild(UD);
			
			//// Urban Dataset Specification section definition
			Specification spec = new Specification(ids, t);
			Element specNode = spec.text(doc);
			
			//// Urban Dataset Context definition
			Context cont = new Context("id1", "UTC+1", "WGS84-DD", "IT");
			Element contextNode = cont.text(doc);
			
			Values val = new Values(t);
			Element valuesNode = val.text(doc);
			
			
			UD.appendChild(specNode);
			UD.appendChild(contextNode);
			UD.appendChild(valuesNode);
			
			//// Urban Dataset Values example
			//TODO
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
			DOMSource source = new DOMSource(doc);
			
			StreamResult result = new StreamResult(new File("output/" + filename));
			transformer.transform(source, result);
			System.out.println("Message template file saved");

			
		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String buildS() {
		String stringFile= null;
		
		System.out.println("Retrieving data from ontology to compile message template");
		Map<String, String> ids = this.templateIDPropertires();
		System.out.println(ids);
		List<com.spring.template.message.Property> t = this.templateProperties();
		
		String filename = ids.get("id") + "-Template.xml";
		try {
			System.out.println("Generating message file");
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			doc.setXmlStandalone(true);

			String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Timestamp(System.currentTimeMillis()));
			Comment comment = doc.createComment("\n" +
					"	File: " + filename +"\r\n" +
					"	Generato il: " + timeStamp +"\r\n" +
					"\r\n" +
					"	Descrizione: questo documento costituisce un XML Campione per l'Urban Dataset " + ids.get("id") + "\r\n" + 
					"	(sono stati valorizzati solo gli elementi e attributi che hanno valore fisso per questo \r\n" + 
					"	tipo di Urban Dataset, ad esempio gli elementi 'propertyName'; gli altri elementi e attributi, \r\n" + 
					"	ad esempio i 'val', devono essere valorizzati).\r\n");
			
			doc.appendChild(comment);
			
			Element UD = doc.createElement("UrbanDataset");
			
			//// Urban Dataset attributes definition
			Attr xmlnsxsi = doc.createAttribute("xmlns:xsi");
			xmlnsxsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
			UD.setAttributeNode(xmlnsxsi);
			Attr schemaLocation = doc.createAttribute("xsi:schemaLocation");
			schemaLocation.setValue(this.xmlns + " " + Configs.getInstance().getProps().getProperty("scpsaddr"));
			UD.setAttributeNode(schemaLocation);
			Attr xmlnsEl = doc.createAttribute("xmlns");
			xmlnsEl.setValue(this.xmlns);
			UD.setAttributeNode(xmlnsEl);
			
			doc.appendChild(UD);
			
			//// Urban Dataset Specification section definition
			Specification spec = new Specification(ids, t);
			Element specNode = spec.text(doc);
			
			//// Urban Dataset Context definition
			Context cont = new Context("id1", "UTC+1", "WGS84-DD", "IT");
			Element contextNode = cont.text(doc);
			
			Values val = new Values(t);
			Element valuesNode = val.text(doc);
			
			
			UD.appendChild(specNode);
			UD.appendChild(contextNode);
			UD.appendChild(valuesNode);
			
			//// Urban Dataset Values example
			//TODO
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
			DOMSource source = new DOMSource(doc);
			StringWriter strWriter = new StringWriter();
	        StreamResult r = new StreamResult(strWriter);
	    
	        transformer.transform(source, r);
	        stringFile = strWriter.getBuffer().toString();
//			StreamResult result = new StreamResult(new File("output/" + filename));
//			transformer.transform(source, result);
			System.out.println("Message template file saved");

			
		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
		
		
		return stringFile;
	}
	
	public File buildF() {
		File file = null;
		String stringFile= null;
		
		System.out.println("Retrieving data from ontology to compile message template");
		Map<String, String> ids = this.templateIDPropertires();
		System.out.println(ids);
		List<com.spring.template.message.Property> t = this.templateProperties();
		
		String filename = ids.get("id") + "-Template.xml";
		try {
			System.out.println("Generating message file");
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			doc.setXmlStandalone(true);

			String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Timestamp(System.currentTimeMillis()));
			Comment comment = doc.createComment("\n" +
					"	File: " + filename +"\r\n" +
					"	Generato il: " + timeStamp +"\r\n" +
					"\r\n" +
					"	Descrizione: questo documento costituisce un XML Campione per l'Urban Dataset " + ids.get("id") + "\r\n" + 
					"	(sono stati valorizzati solo gli elementi e attributi che hanno valore fisso per questo \r\n" + 
					"	tipo di Urban Dataset, ad esempio gli elementi 'propertyName'; gli altri elementi e attributi, \r\n" + 
					"	ad esempio i 'val', devono essere valorizzati).\r\n");
			
			doc.appendChild(comment);
			
			Element UD = doc.createElement("UrbanDataset");
			
			//// Urban Dataset attributes definition
			Attr xmlnsxsi = doc.createAttribute("xmlns:xsi");
			xmlnsxsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
			UD.setAttributeNode(xmlnsxsi);
			Attr schemaLocation = doc.createAttribute("xsi:schemaLocation");
			schemaLocation.setValue(this.xmlns + " " + Configs.getInstance().getProps().getProperty("scpsaddr"));
			UD.setAttributeNode(schemaLocation);
			Attr xmlnsEl = doc.createAttribute("xmlns");
			xmlnsEl.setValue(this.xmlns);
			UD.setAttributeNode(xmlnsEl);
			
			doc.appendChild(UD);
			
			//// Urban Dataset Specification section definition
			Specification spec = new Specification(ids, t);
			Element specNode = spec.text(doc);
			
			//// Urban Dataset Context definition
			Context cont = new Context("id1", "UTC+1", "WGS84-DD", "IT");
			Element contextNode = cont.text(doc);
			
			Values val = new Values(t);
			Element valuesNode = val.text(doc);
			
			
			UD.appendChild(specNode);
			UD.appendChild(contextNode);
			UD.appendChild(valuesNode);
			
			//// Urban Dataset Values example
			//TODO
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
			DOMSource source = new DOMSource(doc);
			StringWriter strWriter = new StringWriter();
	        StreamResult r = new StreamResult(strWriter);
	    
	        transformer.transform(source, r);
	        stringFile = strWriter.getBuffer().toString();
	        
	        file = new File(filename);
			FileWriter fw;
				fw = new FileWriter(file);
				fw.write(stringFile);
				fw.flush();
				fw.close();
				
				System.out.println("Schematron file saved");
			
//			StreamResult result = new StreamResult(new File("output/" + filename));
//			transformer.transform(source, result);
			System.out.println("Message template file saved");

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return file;
	}

}
