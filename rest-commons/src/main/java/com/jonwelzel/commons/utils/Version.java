package com.jonwelzel.commons.utils;

import java.util.ResourceBundle;

/**
 * Class used to read the project version from a maven filtered resource bundle. 
 */
public class Version {
	
	private static ResourceBundle BUNDLE = ResourceBundle.getBundle("rest");
	
	public static String VALUE = BUNDLE.getString("version"); 
}
