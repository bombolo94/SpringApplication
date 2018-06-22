package com.spring.template.message;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Interface that defines a common interface to build a section of 
 * the message template for UrbanDataset
 * 
 * @author Raffaele Ianniello
 *
 */
public interface IMessageTemplate {

	/**
	 * Method that implement the logic to build a section of the message
	 * template.
	 * 
	 * @param doc	the DOM document where to write the section
	 * @return	the root element of the section where all the XML elements were added
	 */
	public Element text(Document doc);
}
