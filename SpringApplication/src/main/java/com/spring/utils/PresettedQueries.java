package com.spring.utils;

import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.query.Query;

/**
 * This class provides static methods to facilitate SPARQL query creation.
 * 
 * @author Raffaele Ianniello
 */
public class PresettedQueries {
	
	/**
	 * Create a basic graph select SPARQL query with one triple and only the subject bounded
	 * 
	 * @param subject	the subject of the triple in the where section
	 * @return	the constructed SPARQL query 
	 */
	public static Query selectAsSubject(String subject){
		SelectBuilder sb = new SelectBuilder().addVar("?p ?o").addWhere("<"+subject+">", "?p", "?o");
		return sb.build();
	}
	
	/**
	 * Create a basic graph select SPARQL query with one triple and only the property bounded
	 * 
	 * @param property	the property of the triple in the where section
	 * @return	the constructed SPARQL query 
	 */
	public static Query selectAsProperty(String property){
		SelectBuilder sb = new SelectBuilder()
				.addVar("?s ?o")
				.addWhere("?s", "<"+property+">", "?o");
		return sb.build();
	}
	
	/**
	 * Create a basic graph select SPARQL query with one triple and only the object bounded
	 * 
	 * @param object	the object of the triple in the where section
	 * @return	the constructed SPARQL query 
	 */
	public static Query selectAsObject(String object){
		SelectBuilder sb = new SelectBuilder()
				.addVar("?s ?p")
				.addWhere("?s", "?p", "<"+object+">");
		return sb.build();
	}
	
	/**
	 * Create a basic graph select SPARQL query with one triple with the property and the object bounded
	 * 
	 * @param property	the property of the triple in the where section
	 * @param object	the object of the triple in the where section
	 * @return	the constructed SPARQL query 
	 */
	public static Query selectSubjects(String property, String object){
		SelectBuilder sb = new SelectBuilder()
				.addVar("?s")
				.addWhere("?s", "<"+property+">", "<"+object+">");
		return sb.build();
	}
	
	/**
	 * Create a basic graph select SPARQL query with one triple with the subject and the object bounded
	 * 
	 * @param subject	the subject of the triple in the where section
	 * @param object	the object of the triple in the where section
	 * @return	the constructed SPARQL query 
	 */
	public static Query selectProperties(String subject, String object){
		SelectBuilder sb = new SelectBuilder()
				.addVar("?p")
				.addWhere("<"+subject+">", "?p", "<"+object+">");
		return sb.build();
	}
	
	/**
	 * Create a basic graph select SPARQL query with one triple with the subject and the property bounded
	 * 
	 * @param subject	the subject of the triple in the where section
	 * @param property	the property of the triple in the where section
	 * @return	the constructed SPARQL query 
	 */
	public static Query selectObjects(String subject, String property){
		SelectBuilder sb = new SelectBuilder()
				.addVar("?o")
				.addWhere("<"+subject+">", "<"+property+">", "?o");
		return sb.build();
	}
	
	/**
	 * Create a basic graph select SPARQL query to retrieve instances on a class
	 * 
	 * @param entity	the class of the individuals searched
	 * @return	the constructed SPARQL query 
	 */
	public static Query selectInstances(String entity){
		SelectBuilder sb = new SelectBuilder()
				.addPrefix("owl", "http://www.w3.org/2002/07/owl#")
				.addVar("?s").setDistinct(true)
				.addWhere( "?s", "?p", "<"+entity+">")
				.addWhere("?s", "?p", "owl:NamedIndividual");
		return sb.build();
	}

}
