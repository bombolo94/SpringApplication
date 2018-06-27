package com.spring.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spring.container.Container;
import com.spring.container.UrbanDataset;
import com.spring.context.Context;
import com.spring.specification.Specification;
import com.spring.values.Values;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Controller
public class FileUploadController
{
  public FileUploadController() {}
  
  @RequestMapping(value={"/parsing"}, method={RequestMethod.POST})
  public String setForm(@RequestParam("parser") String parser)
  {
    return "/parser";
  }
  
  @RequestMapping(value={"/uploadFile"}, method={RequestMethod.POST})
  public ModelAndView uploadFileXML(@RequestParam("fileContent") String fileUploaded, @RequestParam("inputFileName") String inputFileName) {
    ModelAndView modelAndView = new ModelAndView("confirmation");
    String stringFile = null;
    String fileToSaveName = null;
    try
    {
    	if(inputFileName.endsWith(".json")) {
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	      DocumentBuilder db = dbf.newDocumentBuilder();
    	      InputStream is = new ByteArrayInputStream(fileUploaded.getBytes("UTF-8"));
    	      
    	      Document doc = db.parse(is);
    	      
    	      Container container = new Container();
    	      UrbanDataset urban = container.getUrbanDataset();
    	      NodeList nList = doc.getChildNodes();
    	      NodeList mainTag = nList.item(1).getChildNodes();
    	      int len = mainTag.getLength();
    	      
    	      for (int i = 1; i < len; i += 2) {
    	        Node node = mainTag.item(i);
    	        String nodeName = node.getNodeName();
    	        if (nodeName.equals("specification")) {
    	          Specification s = container.specification(node);
    	           urban.setSpecification(s);
    	        } else if (nodeName.equals("context")) {
    	          Context c = container.context(node);
    	          urban.setContext(c);
    	        }
    	        else {
    	          Values v = container.values(node);
    	          urban.setValues(v);
    	        }
    	      }
    	      
    	      GsonBuilder builder = new GsonBuilder();
    	      builder.disableHtmlEscaping();
    	      Gson gson = builder.setPrettyPrinting().create();
    	      stringFile = gson.toJson(container);
    	      
    	      
    	      String pattern = Pattern.quote(System.getProperty("file.separator"));
    	      String[] splittedFileName = inputFileName.split(pattern);
    	      fileToSaveName = splittedFileName[2];
    	     
    	}else {
    		GsonBuilder builder = new GsonBuilder();
			builder.disableHtmlEscaping();
			Gson gson = builder.create();
		
			Container result = gson.fromJson(fileUploaded, Container.class);
			
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			doc.setXmlStandalone(true);
			
			
			Element UD = doc.createElement("UrbanDataset");
			Attr xmlnsxsi = doc.createAttribute("xmlns:xsi");
			xmlnsxsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
			UD.setAttributeNode(xmlnsxsi);
			
			Attr schemaLocation = doc.createAttribute("xsi:schemaLocation");
			schemaLocation.setValue("smartcityplatform:enea:information:xml:schemas:main:urbandataset http://smartcityplatform.enea.it/specification/information/1.0/xml/schemas/scps-urbandataset-schema-1.0.xsd");
			UD.setAttributeNode(schemaLocation);
			
			Attr xmlnsEl = doc.createAttribute("xmlns");
			xmlnsEl.setValue("smartcityplatform:enea:information:xml:schemas:main:urbandataset");
			UD.setAttributeNode(xmlnsEl);
			
			doc.appendChild(UD);
			Specification spec = result.getUrbanDataset().getSpecification();
			Element specNode = spec.text(doc);
			
			Context cont = result.getUrbanDataset().getContext();
			Element contextNode = cont.text(doc);
			
			Values val = result.getUrbanDataset().getValues();
			Element valuesNode = val.text(doc);
			
			
			UD.appendChild(specNode);
			UD.appendChild(contextNode);
			UD.appendChild(valuesNode);				
			
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
	        
	        String pattern = Pattern.quote(System.getProperty("file.separator"));
  	      String[] splittedFileName = inputFileName.split(pattern);
  	      fileToSaveName = splittedFileName[2];
  	      
    	}
//    	modelAndView.addObject("extension", extension);
    	modelAndView.addObject("stringFile", stringFile);
    	modelAndView.addObject("fileToSaveName", fileToSaveName);
    } catch (Exception e) {
      System.out.println(e);
    }
    return modelAndView;
  }
}