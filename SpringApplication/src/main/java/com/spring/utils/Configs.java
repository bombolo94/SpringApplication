package com.spring.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Configs {
	
	private static Configs instance = null;
	
	public static Configs getInstance() {
		if (instance == null)
			return new Configs();
		return instance;
	}
	
	
	Properties props = new Properties();
	
	public Properties getProps() {
		return props;
	}

	private Configs(){
		
		InputStream input = null;
	
		try {
	
			input = new FileInputStream("C:\\Users\\bomb-\\git\\SpringApplication\\SpringApplication\\config.properties");
	
			// load a properties file
			props.load(input);
	
			// get the property value and print it out
//			System.out.println(props.getProperty("address"));
	
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
