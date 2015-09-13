package com.wb.httpforward.server.a_no_user_package.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author www
 * @date 2015年9月9日
 */

public class ClientHolder {

	private static Map<String, TransponderClient> transponderClientMap = new ConcurrentHashMap<String, TransponderClient>();
	
	private static Map<Integer, RequesterClient> requesterClientMap = new ConcurrentHashMap<Integer, RequesterClient>();
	
	private static ClientHolder instance = new ClientHolder();
	
	private ClientHolder() {
		
	}
	
	public static ClientHolder getInstance() {
		return instance;
	}
	
	public void setTransponderClient(String clientNo, TransponderClient client) {
		transponderClientMap.put(clientNo, client);
	}
	
	public TransponderClient getTransponderClient(String clientNo) {
		return transponderClientMap.get(clientNo);
	}
	
	public TransponderClient removeTransponderClient(String clientNo) {
		return transponderClientMap.remove(clientNo);
	}
	
	public void setRequesterClient(Integer code, RequesterClient client) {
		requesterClientMap.put(code, client);
	}
	
	public RequesterClient getRequesterClient(Integer code) {
		return requesterClientMap.get(code);
	}
	
	public RequesterClient removeRequesterClient(Integer code) {
		return requesterClientMap.remove(code);
	}
}
