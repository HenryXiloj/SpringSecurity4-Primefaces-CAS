package com.hxiloj.base.security.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author hxiloj
 */
public class UtilClass {

	
	
	public static <T> String getPropertieValue(String key, Class<T> theClass)
	{
		String nameFileProperty = "messages.properties";
				
		Properties prop = new Properties();
    	InputStream input = null;
    	
    	try 
    	{
    		input = theClass.getClassLoader().getResourceAsStream(nameFileProperty);
    	
    		//load a properties file from class path, inside static method
    		prop.load(input);
    		
            //return property value and print it out
    		return prop.getProperty(key);
 
    	} 
    	catch (IOException ex) 
    	{
    		ex.printStackTrace();
    		return null;
        } 
    	finally
        {
        	if(input!=null)
        	{
	        	try 
	        	{
	        		input.close();
				} 
	        	catch (IOException e)
				{
					e.printStackTrace();
				}
        	}
        }
		
	}
	
}