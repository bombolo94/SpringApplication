package com.spring.dataaccess;

import com.spring.utils.Configs;

/**
 * This class provides methods to access remote SPARQL endpoint as singleton to this java library.
 * It uses as default address the one defined inside the file 'config.properties'
 * 
 * @author Raffaele Ianniello
 */
public class EndpointConnection {
	
	private static EndpointConnection instance = null;
	
	/**
	 * Create or retrive the instance of connection to the remote endpoint
	 * 
	 * @return	the endpoint connection 
	 */
	public static EndpointConnection getInstance() {
		if (instance == null){
			instance = new EndpointConnection();
			return instance;
		}
		return instance;
	}
	
	
	private String address = null;
	
	/**
	 * Retrive the address of the remote endpoint
	 * 
	 * @return	the endpoint address 
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Allows to set the address of the remote endpoint
	 * 
	 * @param address	the address of the remote endpoint 
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Reset the address of the remote endpoint to the one defined inside configuration file
	 */
	public void setDefaultAddress() {
		this.address = Configs.getInstance().getProps().getProperty("address");
	}

	private EndpointConnection(){
		this.address = Configs.getInstance().getProps().getProperty("address");
	}


}
