package com.spring.dataaccess;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.arq.querybuilder.AskBuilder;
import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import com.spring.utils.PresettedQueries;



/**
 * This class consists of methods that operate on a RDF Resource using as 
 * base the Jena RDF library. The approach consists in create a wrapper to
 * operate on Jena Resource object and provide methods in order to easily 
 * create queries, towards a selected SPARQL endpoint, for knowing some basic
 * information on the wrapped resource.   
 * 
 * @author Raffaele Ianniello
 */
public class ResourceWrap {
	
	private String localName;
	private String nameSpace;
	private Resource resource;
	private EndpointConnection connection;
	
	/**
	 * Construct an empty ResourceWrap object
	 */
	public ResourceWrap(){
		connection = EndpointConnection.getInstance();
	}
	
	/**
	 * Construct a ResourceWrap object creating a Resource object from the IRI string
	 * 
	 * @param name	the string that represent the URI of the resource
	 */
	public ResourceWrap(String name){
		this();
		this.resource = ResourceFactory.createResource(name);
		this.setName(this.resource.getNameSpace(), this.resource.getLocalName());
	}
	
	/**
	 * Construct a ResourceWrap object creating a Resource object from the IRI string
	 * in the form of prefix and local name
	 * 
	 * @param NameSpace	prefix of the resource URI
	 * @param LocalName	local name of the resource URI
	 */
	public ResourceWrap(String NameSpace, String LocalName){
		this();
		this.setName(NameSpace, LocalName);
	}
	
	/**
	 * Construct a ResourceWrap object from a Resource object
	 * 
	 * @param res	the resource object to wrap
	 */
	public ResourceWrap(Resource res){
		this();
		this.setName(res.getNameSpace(), res.getLocalName());
	}
	
	public String[] getName(){
		String[] val = {nameSpace, localName};
		return val;
	}
	
	private void setName(String NameSpace, String LocalName){
		this.nameSpace = NameSpace;
		this.localName = LocalName;
	}
	
	/**
	 * Returns the URI representation of the resource
	 * 
	 * @return	the string that represent the URI
	 */
	public String getIRI(){
		return nameSpace+localName;
	}
	
	/**
	 * Returns the Resource object
	 * 
	 * @return the Resource object wrapped by this class
	 */
	public Resource getResource() {
		return resource;
	}
	
	/**
	 * Checks whether if a property, passed as Property object, is defined for the resource 
	 * 
	 * @param property	the property to check
	 * @return	True if the resource has the property defined
	 */
	public boolean hasProperty(Property property){
		Query ab = new AskBuilder()
				.addWhere("<"+getIRI()+">", property, "?o").build();
		return QueryExecutionFactory.sparqlService(connection.getAddress(), ab).execAsk();
	}

	/**
	 * Checks whether if a property, passed as IRI string, is defined for the resource 
	 * 
	 * @param property	the property to check
	 * @return	True if the resource has the property defined
	 */
	public boolean hasProperty(String property){
		Property prop = ResourceFactory.createProperty(property);
		return hasProperty(prop);
	}
	
	/**
	 * Checks whether if a property is defined for the resource. The property is defined 
	 * as IRI string separated in IRI prefix and resource name 
	 * 
	 * @param prefix	the namespace of the property 
	 * @param localname	the actual name of the property without the prefix 
	 * @return	True if the resource has the property defined
	 */
	public boolean hasProperty(String prefix, String localname){
		return hasProperty(prefix+localname);
	}
	
	/**
	 * Checks whether if the resource is an Individual or not
	 * 
	 * @return	True if the resource is Individual
	 */
	public boolean isIndividual(){
		Query ab = new AskBuilder()
				.addPrefix("owl", "http://www.w3.org/2002/07/owl#")
				.addPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
				.addWhere("<"+getIRI()+">", "rdf:type", "owl:NamedIndividual").build();
		return QueryExecutionFactory.sparqlService(connection.getAddress(), ab).execAsk();
	}
	
	
	/**
	 * Retrieve the list of triples where the resource appears as subject. Select the
	 * properties from the triples list.
	 *  
	 * @return	The list of Property as List collection 
	 */
	public List<Property> getProperties(){
		//Query sb = queryPorperties();,
		Query sb = PresettedQueries.selectAsSubject(getIRI());
		QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sb);
		ResultSet results = exec.execSelect();
		ArrayList<Property> ar = new ArrayList<Property>();

        while ( results.hasNext() ) {
        	RDFNode p = results.next().get( "p" );
            //System.out.println(p);
            Property pr = ResourceFactory.createProperty(p.toString());
            ar.add(pr);
        }
		return ar;
	}
	
	/**
	 * Retrieve the values, object of the relation between the resource and the property 
	 * passed as String
	 * 
	 * @param property	The property of the relation
	 * @return	The list of elements object of the relation
	 */
	public List<Resource> getValues(String property) {
		Query sb = PresettedQueries.selectObjects(getIRI(), property);
		QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sb);
		ResultSet results = exec.execSelect();
		ArrayList<Resource> ar = new ArrayList<Resource>();
		
		while ( results.hasNext() ) {
        	RDFNode o = results.next().get( "o" );
            //System.out.println(p);
        	Resource res = ResourceFactory.createResource(o.toString());
            ar.add(res);
        }
		return ar;
	}
	
	/**
	 * Retrieve the values, object of the relation between the resource and the property 
	 * passed as Property Object
	 * 
	 * @param property	The property of the relation
	 * @return	The list of elements object of the relation
	 */
	public List<Resource> getValues(Property property) {
		return getValues(property.getURI());
	}
	
	/**
	 * Retrieve the list of triples where the resource appears as subject. Returns a map
	 * with associated to every property, the list of resources.
	 *  
	 * @return	The map of Property and list of resources
	 */
	public Map<Property, List<Resource>> getPropertiesValues(){
		Query sb = PresettedQueries.selectAsSubject(getIRI());
		QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sb);
		ResultSet results = exec.execSelect();
		Map<Property, List<Resource>> mappa = new HashMap<>();

        while ( results.hasNext() ) {
        	QuerySolution result = results.next();
        	RDFNode p = result.get( "p" );
        	RDFNode o = result.get( "o" );
            Property pr = ResourceFactory.createProperty(p.toString());
            Resource res = ResourceFactory.createResource(o.toString());
            List<Resource> listres = mappa.get(pr);
            if (listres == null) {
            	listres = new ArrayList<Resource>();
            }
            listres.add(res);
            mappa.put(pr, listres);
        }
		return mappa;
	}
	
	
	/**
	 * Constructs a Jena RDF Model object with all the triples that have the resource as subject
	 * 
	 * @return	The constructed model
	 */
	public Model getSubModel(){
		//Query sb = queryPorperties();
		Query sb = PresettedQueries.selectAsSubject(getIRI());
		QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sb);
		ResultSet results = exec.execSelect();
		
		Model model = ModelFactory.createDefaultModel();
		
		while ( results.hasNext() ) {
			QuerySolution qs = results.next();
        	RDFNode p = qs.get( "p" );
        	RDFNode o = qs.get( "o" );
            Property pr = ResourceFactory.createProperty(p.toString());
            model.createResource(getIRI()).addProperty(pr, o);
        }
		return model;
	}

	/**
	 * Determines the properties that connect the resource with another specified resource.
	 * The resource object of the relations is specified with its IRI
	 * 
	 * @param object	The resource as object of the relations 
	 * @return	The List of the properties between the two resources
	 */
	public List<Property> getRelations(String object){
		Query sq = PresettedQueries.selectProperties(getIRI(), object);
		
		QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sq);
		ResultSet results = exec.execSelect();		
		ArrayList<Property> ar = new ArrayList<Property>();
		
		while ( results.hasNext() ) {
        	RDFNode p = results.next().get( "p" );
            Property pr = ResourceFactory.createProperty(p.toString());
            ar.add(pr);
        }
		return ar;
	}
	
	/**
	 * Determines the properties that connect the resource with another specified resource
	 * 
	 * @param object	The resource as object of the relations 
	 * @return	The List of the properties between the two resources
	 */
	public List<Property> getRelations(Resource object){
		List<Property> ar = this.getRelations(object.getURI());
		return ar;		
	}
	
	/**
	 * If the resource is an Entity, retrieves all the instances of it.
	 * 
	 * @param limit	the maximum number of values to retrieve (0 if no limit)
	 * @return	The List of the instances of the resource, if any.
	 */
	public List<Resource> getInstances(int limit){
		if (!this.isIndividual()){
			SelectBuilder sb = new SelectBuilder()
					.addPrefix("", nameSpace)
					.addPrefix("owl", "http://www.w3.org/2002/07/owl#")
					.addVar("?s").setDistinct(true)
					.addWhere( "?s", "?p", ":"+localName )
					.addWhere("?s", "?p", "owl:NamedIndividual");
//			System.out.println(sb);
			if (limit > 0) sb.setLimit(limit);
			QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sb.build());
			ResultSet results = exec.execSelect();
			ArrayList<Resource> ar = new ArrayList<Resource>();
			while ( results.hasNext() ) {
	        	RDFNode s = results.next().get( "s" );
	            Resource res = ResourceFactory.createResource(s.toString());
	            ar.add(res);
	        }
			
			return ar;
		}
		
		return new ArrayList<Resource>();		
	}
	
	/**
	 * Retrieve the list of subclasses of a Class.
	 *  
	 * @return	The list of Property as List collection
	 */
	public List<Resource> getSubClasses(){
		if (!this.isIndividual()){
			Query sb = PresettedQueries.selectSubjects("http://www.w3.org/2000/01/rdf-schema#subClassOf" ,getIRI());
			QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sb);
			ResultSet results = exec.execSelect();
			ArrayList<Resource> ar = new ArrayList<Resource>();
			while ( results.hasNext() ) {
	        	RDFNode p = results.next().get( "s" );
	        	Resource pr = ResourceFactory.createProperty(p.toString());
	            ar.add(pr);
	        }
			return ar;
		}
		return new ArrayList<Resource>();
	}
	
	/**
	 * If the resource is an Individual, retrieves all the types of it.
	 * 
	 * @return	The List of the types of the resource, if any.
	 */
	public List<Resource> getClasses(){
		if (this.isIndividual()){
			SelectBuilder sb = new SelectBuilder()
					.addPrefix("", nameSpace)
					.addPrefix("owl", "http://www.w3.org/2002/07/owl#")
					.addVar("?o").setDistinct(true)
					.addWhere( ":"+localName, "a", "?o" )
					.addWhere(":"+localName, "a", "owl:NamedIndividual");
			QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sb.build());
			ResultSet results = exec.execSelect();
			ArrayList<Resource> ar = new ArrayList<Resource>();
			while ( results.hasNext() ) {
	        	RDFNode s = results.next().get( "o" );
	            Resource res = ResourceFactory.createResource(s.toString());
	            ar.add(res);
	        }
			
			return ar;
		}
		
		return new ArrayList<Resource>();
	}
	
	// ottenere le propriet� di cui � oggetto e le altre entit� con cui � in relazione come oggetto e non come soggetto

}
