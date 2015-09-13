package com.wb.httpforward.constant;

import java.util.Properties;

import com.wb.httpforward.util.ConfigUtil;
import com.wb.httpforward.util.PropertiesLoader;

/**
 * @author www
 * @date 2015年9月9日
 */

public class ClientConstant {
	
	private static final Properties properties = PropertiesLoader.initProperties("client.properties");
	
	public static String TARGET_SERVER_URL = ConfigUtil.getConfigValue(properties, "target_server_url");
	
	private static String HTTP_FORWARD_SERVER = ConfigUtil.getConfigValue(properties, "http_forward_server");
	
	private static String HTTP_FORWARD_SERVER_HOLD_REQUEST_PATH = "/callback/holdRequest";
	
	private static String HTTP_FORWARD_SERVER_TRANSPOND_RESPONSE_PATH = "/transpondResponse";
	
	public static String HTTP_FORWARD_SERVER_HOLD_REQUEST_URL = 
			"http://" + HTTP_FORWARD_SERVER + HTTP_FORWARD_SERVER_HOLD_REQUEST_PATH;
	
	public static String HTTP_FORWARD_SERVER_TRANSPOND_RESPONSE_URL = 
			"http://" + HTTP_FORWARD_SERVER + HTTP_FORWARD_SERVER_TRANSPOND_RESPONSE_PATH;
	
	public static String LOCAL_CLIENT_NO = ConfigUtil.getConfigValue(properties, "local_client_no");
	
	public static String COMET_HTTP_METHOD = ConfigUtil.getConfigValue(properties, "comet_http_method", "GET");
	
	public static int HTTP_CLIENT_CONNECT_TIMEOUT = ConfigUtil.getConfigIntValue(properties, "http_client_connect_timeout", 10000);
	
	public static int HTTP_CLIENT_SOCKET_TIMEOUT = ConfigUtil.getConfigIntValue(properties, "http_client_socket_timeout", 60000);
	
	public static boolean LOG_WITH_BODY = ConfigUtil.getConfigBooleanValue(properties, "log_with_body");
	
}
