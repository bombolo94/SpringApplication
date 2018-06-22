package com.spring.template.message;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class generate the section of the template message of 
 * the context section.
 *      
 * @author Raffaele Ianniello
 */
public class Context implements IMessageTemplate{
	
	private String producer;
	private String timeZone;
	private String coordinateFormat;
	private String language;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	/**
	 * Constructs a Context object from the producer of an UrbanDataset
	 * 
	 * @param producer	the producer of the UrbanDataset
	 * @param timeZone	the timeZone used by the UrbanDataset
	 * @param coordinateFormat	the coordinate format of the UrbanDataset
	 * @param language	the language used by the UrbanDataset
	 */
	public Context(String producer, String timeZone, String coordinateFormat, String language) {
		this.producer = producer;
		this.timeZone = timeZone;
		this.coordinateFormat = coordinateFormat;
		this.language = language;
	}
	
	@Override
	public Element text(Document doc) {
		Element context = doc.createElement("context");
		
		Element producer = doc.createElement("producer");
		Element ID = doc.createElement("id");
		ID.setAttribute("schemeID", "schemeID1");
		ID.appendChild(doc.createTextNode(this.producer));
		producer.appendChild(ID);
		
		Element timeZone = doc.createElement("timeZone");
		timeZone.appendChild(doc.createTextNode(this.timeZone));
		
		Element timestamp = doc.createElement("timestamp");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		timestamp.appendChild(doc.createTextNode(sdf.format(time)));
		
		Element coordinates = doc.createElement("coordinates");
		coordinates.setAttribute("format", this.coordinateFormat);
		Element latitude = doc.createElement("latitude");
		latitude.appendChild(doc.createTextNode("0"));
		Element longitude = doc.createElement("longitude");
		longitude.appendChild(doc.createTextNode("0"));
		Element altitude = doc.createElement("height");
		altitude.appendChild(doc.createTextNode("0"));
		
		coordinates.appendChild(latitude);
		coordinates.appendChild(longitude);
		coordinates.appendChild(altitude);
		
		Element lang = doc.createElement("language");
		lang.appendChild(doc.createTextNode(language));
		
		context.appendChild(producer);
		context.appendChild(timeZone);
		context.appendChild(timestamp);
		context.appendChild(coordinates);
		context.appendChild(lang);

		return context;
		
	}

}
