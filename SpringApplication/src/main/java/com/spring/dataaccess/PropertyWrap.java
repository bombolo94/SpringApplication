package com.spring.dataaccess;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.arq.querybuilder.AskBuilder;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import com.spring.utils.PresettedQueries;

/**
 * This class consists of methods that operate on a RDF Property using as 
 * base the Jena RDF library. The approach consists in create a wrapper to
 * operate on Jena Property object and provide methods in order to easily 
 * create queries, towards a selected SPARQL endpoint, for knowing some basic
 * information on the wrapped property.   
 * 
 * @author Raffaele Ianniello
 */
public class PropertyWrap {
	
	private String localName;
	private String nameSpace;
	private Property property;
	private EndpointConnection connection;
	
	/**
	 * Construct an empty PropertyWrap object
	 */
	public PropertyWrap(){
		connection = EndpointConnection.getInstance();
	}
	
	/**
	 * Construct a PropertyWrap object creating a Property object from the IRI string
	 * 
	 * @param name	the string that represent the URI of the property
	 */
	public PropertyWrap(String name){
		this();
		this.property = ResourceFactory.createProperty(name);
		this.setName(this.property.getNameSpace(), this.property.getLocalName());
	}
	
	/**
	 * Construct a PropertyWrap object creating a Property object from the IRI string
	 * in the form of prefix and local name
	 * 
	 * @param NameSpace	prefix of the property URI
	 * @param LocalName	local name of the property URI
	 */
	public PropertyWrap(String NameSpace, String LocalName){
		this();
		this.setName(NameSpace, LocalName);
	}
	
	/**
	 * Construct a PropertyWrap object from a Property object
	 * 
	 * @param res	the property object to wrap
	 */
	public PropertyWrap(Property res){
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
	 * Returns the URI representation of the property
	 * 
	 * @return	the string that represent the URI
	 */
	public String getIRI(){
		return nameSpace+localName;
	}
	
	/**
	 * Returns the Property object
	 * 
	 * @return the Property object wrapped by this class
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * Checks whether if a resource, passed as Resource object, is present as subject of the property  
	 * 
	 * @param resource	the resource to check
	 * @return	True if the property has the resource as subject
	 */
	public boolean hasSubject(Resource resource){
		Query ab = new AskBuilder()
				.addWhere(resource, "<"+getIRI()+">", "?o").build();
		return QueryExecutionFactory.sparqlService(connection.getAddress(), ab).execAsk();
	}
	
	/**
	 * Checks whether if a resource, passed as IRI string, is present as subject of the property
	 *  
	 * @param resource	the resource to check
	 * @return	True if the property has the resource as subject
	 */
	public boolean hasSubject(String resource){
		Resource res = ResourceFactory.createProperty(resource);
		return hasSubject(res);
	}
	
	/**
	 * Checks whether if a resource is present as subject of the property. The resource is defined 
	 * as IRI string separated in IRI prefix and resource name 
	 * 
	 * @param prefix	the namespace of the resource 
	 * @param localname	the actual name of the resource without the prefix 
	 * @return	True if the property has the resource as subject
	 */
	public boolean hasSubject(String prefix, String localname){
		return hasSubject(prefix+localname);
	}
	
	/**
	 * Checks whether if a resource, passed as Resource object, is present as object of the property  
	 * 
	 * @param resource	the resource to check
	 * @return	True if the property has the resource as object
	 */
	public boolean hasObject(Resource resource){
		Query ab = new AskBuilder()
				.addWhere("?s", "<"+getIRI()+">", resource).build();
		return QueryExecutionFactory.sparqlService(connection.getAddress(), ab).execAsk();
	}
	
	/**
	 * Checks whether if a resource, passed as IRI string, is present as object of the property
	 *  
	 * @param resource	the resource to check
	 * @return	True if the property has the resource as object
	 */
	public boolean hasObject(String resource){
		Resource res = ResourceFactory.createProperty(resource);
		return hasObject(res);
	}
	
	/**
	 * Checks whether if a resource is present as object of the property. The resource is defined 
	 * as IRI string separated in IRI prefix and resource name 
	 * 
	 * @param prefix	the namespace of the resource 
	 * @param localname	the actual name of the resource without the prefix 
	 * @return	True if the property has the resource as subject
	 */
	public boolean hasObject(String prefix, String localname){
		return hasObject(prefix+localname);
	}

	//il successiovo anche per gli oggetti?
	/**
	 * Retrieve the list of triples where the resource appears as property. Returns the
	 * subjects from the triples list.
	 *  
	 * @return	The list of Resource as List collection 
	 */
	public List<Resource> getSubjects(){
		Query sb = PresettedQueries.selectAsProperty(getIRI());
		QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sb);
		ResultSet results = exec.execSelect();
		ArrayList<Resource> ar = new ArrayList<Resource>();

        while ( results.hasNext() ) {
        	RDFNode p = results.next().get( "s" );
        	Resource pr = ResourceFactory.createResource(p.toString());
            ar.add(pr);
        }
		return ar;
	}
	
	/**
	 * Retrieve the list of triples where the resource appears as property. Returns the
	 * objects from the triples list.
	 *  
	 * @return	The list of Resource as List collection 
	 */
	public List<Resource> getObjects(){
		Query sb = PresettedQueries.selectAsProperty(getIRI());
		QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sb);
		ResultSet results = exec.execSelect();
		ArrayList<Resource> ar = new ArrayList<Resource>();

        while ( results.hasNext() ) {
        	RDFNode p = results.next().get( "o" );
        	Resource pr = ResourceFactory.createResource(p.toString());
            ar.add(pr);
        }
		return ar;
	}
	
	/**
	 * Returns the list of resources that act as subjects given the object as Resource.
	 *  
	 * @param object	the Resource object of the triples 
	 * @return	The list of Resource as List collection 
	 */
	public List<Resource> getSubjectFromObject(Resource object){
		return this.getSubjectFromObject(object.getURI());
	}
	
	/**
	 * Returns the list of resources that act as subjects given the object as IRI string.
	 *  
	 * @param object	the string object of the triples 
	 * @return	The list of Resource as List collection 
	 */
	public List<Resource> getSubjectFromObject(String object){
		Query sq = PresettedQueries.selectSubjects(getIRI(), object);
		
		QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sq);
		ResultSet results = exec.execSelect();		
		ArrayList<Resource> ar = new ArrayList<Resource>();
		
		while ( results.hasNext() ) {
        	RDFNode p = results.next().get( "s" );
        	Resource pr = ResourceFactory.createResource(p.toString());
            ar.add(pr);
        }
		return ar;
	}
	
	/**
	 * Returns the list of resources that act as subjects given the object as string. The resource is defined 
	 * as IRI string separated in IRI prefix and resource name 
	 *  
	 * @param prefix	the namespace of the resource 
	 * @param localname	the actual name of the resource without the prefix 	
	 * @return	The list of Resource as List collection 
	 */
	public List<Resource> getSubjectFromObject(String prefix, String localname){
		return this.getSubjectFromObject(prefix+localname);
		
	}
	
	/**
	 * Returns the list of resources that act as objects given the subject as Resource.
	 *  
	 * @param subject	the Resource subject of the triples 
	 * @return	The list of Resource as List collection 
	 */
	public List<Resource> getObjectFromSubject(Resource subject){
		return this.getObjectFromSubject(subject.getURI());
	}
	
	/**
	 * Returns the list of resources that act as objects given the subject as IRI string.
	 *  
	 * @param subject	the string subject of the triples 
	 * @return	The list of Resource as List collection 
	 */
	public List<Resource> getObjectFromSubject(String subject){
		Query sq = PresettedQueries.selectObjects(subject, getIRI());
		
		QueryExecution exec = QueryExecutionFactory.sparqlService(connection.getAddress(), sq);
		ResultSet results = exec.execSelect();		
		ArrayList<Resource> ar = new ArrayList<Resource>();
		
		while ( results.hasNext() ) {
        	RDFNode p = results.next().get( "o" );
        	Resource pr = ResourceFactory.createResource(p.toString());
            ar.add(pr);
        }
		return ar;
	}
	
	/**
	 * Returns the list of resources that act as objects given the subject as string. The resource is defined 
	 * as IRI string separated in IRI prefix and resource name 
	 *  
	 * @param prefix	the namespace of the resource 
	 * @param localname	the actual name of the resource without the prefix 	
	 * @return	The list of Resource as List collection 
	 */
	public List<Resource> getObjectFromSubject(String prefix, String localname){
		return this.getObjectFromSubject(prefix+localname);
	}
	
}
