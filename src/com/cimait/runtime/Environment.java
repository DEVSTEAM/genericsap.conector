package com.cimait.runtime;

import java.io.IOException;

//import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.cimait.Util.CtrlFile;
 


public class Environment {

	static String classReference = "Environment->";
	public static CtrlFile cf;
	public static Configuration c;
	public static Logger log;
	
	public static void setConfiguration(String configFile) throws IOException {
		c= loadInitialConfiguration(configFile);
	}
	
	public static Configuration loadInitialConfiguration(String file) throws IOException{
        Configuration configuration= null;
        ConfigurationFactory factory = null;
        try {
        	configuration= new PropertiesConfiguration(file);  
        	if(file.endsWith(".xml")){
        	   factory = new ConfigurationFactory(file);
			   configuration= factory.getConfiguration();
        	}else{
        	   configuration= new PropertiesConfiguration(file);
        	}
		} catch (ConfigurationException e) {
			System.err.println(classReference+"loadInitialConfiguration[ConfigurationException] " + e.getMessage());
		}
        return configuration;
	}
	
	public static void setCtrlFile(){
		System.out.println(classReference+"Loading ControlFile "+Environment.c.getString("Empresa.ctrl-on-off.file"));
		cf = new CtrlFile(Environment.c.getString("Empresa.ctrl-on-off.file"));
	}
			
	public static Logger getLog(){
		return log;
	}
	
	public static void setLogger(String logConfigFile) {
		log = Logger.getLogger("file");
		PropertyConfigurator.configureAndWatch("Dispatcher.ctr");
	}

}
