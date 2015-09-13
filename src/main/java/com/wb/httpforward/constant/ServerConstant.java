package com.wb.httpforward.constant;

import java.util.Properties;

import com.wb.httpforward.util.ConfigUtil;
import com.wb.httpforward.util.PropertiesLoader;

/**
 * @author www
 * @date 2015年9月13日
 */

public class ServerConstant {

	private static final Properties properties = PropertiesLoader.initClasspathProperties("server.properties");
	
	public static String CLIENT_NO_SET = ConfigUtil.getConfigValue(properties, "client_no_set");
	
	public static int SERVER_HOLD_REQUEST_TIMEOUT = ConfigUtil.getConfigIntValue(properties, "server_hold_request_timeout", 30000);
	
	public static int CODE_START_VALUE = ConfigUtil.getConfigIntValue(properties, "code_start_value", 100);
	
	public static boolean LOG_WITH_BODY = ConfigUtil.getConfigBooleanValue(properties, "log_with_body");
	
}
