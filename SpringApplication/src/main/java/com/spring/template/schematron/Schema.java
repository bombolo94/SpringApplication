package com.spring.template.schematron;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class consists of methods that operate on a XML file used as template 
 * in order to generate a Schematron model to validate XML messages used
 * inside the SCPS framework and based on the related ontology.
 * This class provides methods to easily actuate a substitution of the 
 * words identified inside the template using as strating chars the sequence
 * '$$'.  
 * 
 * @author Raffaele Ianniello
 */
public class Schema {
	
	private final String REGEX = "([$]{2})+(\\w*)";
	private final String REGEX_singlelinecomment = "(<!--)(.*)(-->)";
	private final String REGEX_multisinglelinecomment = ".*(<!--)((?s).*?)(-->)[\\r\\n]";
	private final String REGEX_multisinglelinecomment2 = "<!--[\\s\\S]*?-->[\\r\\n]+";
	private String originalContent;
	private String modifiedContent;
	private String fileName;
	
	/**
	 * Getter of the original schematron text template
	 * 
	 * @return	the original content of the template file
	 */
	public String getOriginalContent() {
		return originalContent;
	}

	/**
	 * Getter of the modified schematron text template
	 * 
	 * @return	the modified content of the template file
	 */
	public String getModifiedContent() {
		return modifiedContent;
	}

	
	/**
	 * Construct a Schema object from the name of file that contains the template
	 * 
	 * @param fileName	the name of the file where is the template
	 */
	public Schema(String fileName) {
		this(new File(fileName));
	}
	
	
	/**
	 * Construct a Schema object from the file that contains the template
	 *
	 * @param file	the file object containing the template
	 */
	public Schema(File file) {
		this.fileName = file.getName();
		this.loadFile(file);
	}
	
	
	private void loadFile(File file) {
		StringBuilder b = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				b.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.originalContent = b.toString();
	}
	
	
	/**
	 * Retrieve the keywords inside the template and the number of times they appear
	 * 
	 * @return	the map that links keywords to the number of times they appear
	 */
	public Map<String, Integer> getMatchMap() {
		Pattern pattern = Pattern.compile(this.REGEX);
		Matcher matcher = pattern.matcher(originalContent);
		Map<String, Integer> matchMap = new HashMap<String, Integer>();
		while (matcher.find()) {
			if (matchMap.get(matcher.group()) == null) {
				matchMap.put(matcher.group(), 1);
			} else {
				int value = matchMap.get(matcher.group());
				matchMap.put(matcher.group(), value + 1);
			}
		}
		return matchMap;		
	}
	
	
	/**
	 * Retrieve the list of keywords inside the template
	 * 
	 * @return	the list of keywords inside the template
	 */
	public List<String> getMatchList() {
		Pattern pattern = Pattern.compile(this.REGEX);
		Matcher matcher = pattern.matcher(originalContent);
		List<String> matchList = new ArrayList<String>();
		while (matcher.find()) {
			if (!matchList.contains(matcher.group())) {
				matchList.add(matcher.group());
			}
		}
		return matchList;		
	}
	
	
	private void resetComments() {
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile(this.REGEX_multisinglelinecomment);
		Matcher matcher = pattern.matcher(originalContent);
		while (matcher.find()) {  
	    	matcher.appendReplacement(sb, "");
		}
		matcher.appendTail(sb);
		this.modifiedContent = sb.toString();
		return;
	}
	
	
	private void addComments() {
		resetComments();
		String gen = "\r\n<!-- \r\n" + 
				"        	Rules for \\$\\$nomeUrbanDataset Urban Dataset\r\n" + 
				"        	Automatically generated from SCPS Ontology 1.0 on: \\$\\$dataDiGenerazione\r\n" + 
				"-->";
		String genPattern = "<\\?(.*)\\?>[\\r\\n]";
		
		StringBuffer sb = new StringBuffer();
		Pattern patternGen = Pattern.compile(genPattern);
		Matcher matcherGen = patternGen.matcher(modifiedContent);
		while (matcherGen.find()) {
			String repString = matcherGen.group() + gen;
		    if (repString != null)    
		    	matcherGen.appendReplacement(sb, repString);
		}
		matcherGen.appendTail(sb);
		this.modifiedContent = sb.toString();
		
		sb = new StringBuffer();
		String param = "<!-- \\$\\$nomeUrbanDataset Urban Dataset parameters -->\r\n  ";
		String paramPattern = "<pattern is-a=\"abstractUD\" id";
		Pattern patternParam = Pattern.compile(paramPattern);
		Matcher matcherParam = patternParam.matcher(modifiedContent);
		while (matcherParam.find()) {
			String repString = param + matcherParam.group();
		    if (repString != null)    
		    	matcherParam.appendReplacement(sb, repString);
		}
		matcherParam.appendTail(sb);
		this.modifiedContent = sb.toString();
		
		sb = new StringBuffer();
		String rules = "<!-- scps Urban Dataset Common Rules -->\r\n  ";
		String rulesPattern = "<pattern id=\"abstractUD\" abstract";
		Pattern patternRules = Pattern.compile(rulesPattern);
		Matcher matcherRules = patternRules.matcher(modifiedContent);
		while (matcherRules.find()) {
			String repString = rules + matcherRules.group();
		    if (repString != null)    
		    	matcherRules.appendReplacement(sb, repString);
		}
		matcherRules.appendTail(sb);
		this.modifiedContent = sb.toString();
	}
	
	/**
	 * Replace the template text keywords with the values passed as param
	 * 
	 * @param replacements	the map that links template keyword to the replacement text
	 * @return	the text of the template modified
	 */
	public String replace(Map<String, String> replacements) {
		System.out.println("Generating schematron file");
		resetComments();
		addComments();
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile(this.REGEX);
		Matcher matcher = pattern.matcher(modifiedContent);
		while (matcher.find()) {
			String repString = replacements.get(matcher.group());
		    if (repString != null)    
		    	matcher.appendReplacement(sb, repString);
		}
		matcher.appendTail(sb);
		this.modifiedContent = sb.toString();
		return this.modifiedContent;
	}
	
	
	/**
	 * Save the modified content of the template in a new file
	 * 
	 * @param fileName	the name of the file where to save the schematron model
	 */
	public void saveFile(String fileName) {
		File theDir = new File("./output");
		try {
			theDir.mkdir();
		} 
		catch(SecurityException se) {
			se.printStackTrace();
		}        
		try (PrintWriter out = new PrintWriter(theDir.getName() + "/" + fileName)) {
			out.println(this.modifiedContent);
			System.out.println("Schematron file saved");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Schematron file cannot be saved");
			
		}
	} 
	
	public String saveFileAndrea() {
		return this.modifiedContent;
		
	} 

}
