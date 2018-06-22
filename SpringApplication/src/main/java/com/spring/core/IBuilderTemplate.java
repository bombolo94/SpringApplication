package com.spring.core;
/**
 * Interface that defines a common interface to compute a template
 * for UrbanDataset schematron and message
 * 
 * @author Raffaele Ianniello
 *
 */
public interface IBuilderTemplate {
	
	/**
	 * Method that implement the logic to build the schematron or message
	 * template. It saves the result on a file named after the UrbanDatset.
	 */
	public void build();
	public String buildS();

}
