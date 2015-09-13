package com.wb.httpforward.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author www
 * @date 2015年9月13日
 */

public class PropertiesLoader {

	/**
	 * newProperties 获取配置文件
	 * @param fileName
	 * @return 
	 */
	public static Properties initProperties(String fileName) {
		Properties properties = null;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(fileName);
			properties = new Properties();
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			System.out.println("load " + fileName + " into PropertiesLoader error!");
		} finally {
            if (inputStream != null) {
                try {
                	inputStream.close();
                } catch (IOException e) {
                	System.out.println("close " + fileName + " error!");
                }
            }
        }
		return properties;
	}
	
	public static Properties initClasspathProperties(String fileName) {
        Properties properties = null;
        InputStream inputStream = null;
        try {
        	inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(
            		fileName);
            if (inputStream != null) {
                properties = new Properties();
                properties.load(inputStream);
            }
        } catch (IOException e) {
            System.out.println("load " + fileName + " into PropertiesLoader error!");
        } finally {
            if (inputStream != null) {
                try {
                	inputStream.close();
                } catch (IOException e) {
                	System.out.println("close " + fileName + " error!");
                }
            }
        }
        return properties;
    }
}
