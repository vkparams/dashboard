package com.evault.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {
	

	public static String getProperty(String key) {
		String value = null;
		Properties props;
		try {
			Resource resource = new ClassPathResource("/conf/environment.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			value = props.getProperty(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;

	}

}
