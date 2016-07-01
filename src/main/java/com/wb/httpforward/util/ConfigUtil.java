package com.wb.httpforward.util;

import java.util.Properties;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * @author www
 * @date 2015年9月13日
 */

public class ConfigUtil {

	public static String getConfigValue(Properties properties, String key) {
		return properties.getProperty(key);
    }
	
	public static String getConfigValue(Properties properties, String key, String defaultValue) {
		String value = getConfigValue(properties, key);
        if (StringUtils.isBlank(value)) {
        	value = defaultValue;
        }
        return value;
	}
	
	public static int getConfigIntValue(Properties properties, String key) {
        return NumberUtils.toInt(getConfigValue(properties, key));
    }

    public static int getConfigIntValue(Properties properties, String key, int defaultInt) {
        return NumberUtils.toInt(getConfigValue(properties, key), defaultInt);
    }
    
    public static boolean getConfigBooleanValue(Properties properties, String key) {
        return BooleanUtils.toBoolean(getConfigValue(properties, key));
    }
   
}
