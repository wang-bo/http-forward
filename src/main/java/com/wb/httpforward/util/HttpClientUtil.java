package com.wb.httpforward.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.wb.httpforward.constant.ClientConstant;

/**
 * @author www
 * @date 2015年9月10日
 */

public class HttpClientUtil {
	
	private static CloseableHttpClient httpClient;
	
	static {
		// 使用连接池暂不实现，目前整个客户端使用这个唯一的httpClient实例
		
		// 设置HttpClient参数
		RequestConfig requestConfig = RequestConfig.custom()  
		        .setConnectTimeout(ClientConstant.HTTP_CLIENT_CONNECT_TIMEOUT)  
		        .setSocketTimeout(ClientConstant.HTTP_CLIENT_SOCKET_TIMEOUT)  
		        .build();
		
		httpClient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.build();
	}

	public static CloseableHttpClient getHttpClient() {
		return httpClient;
	}
	
	public static Map<String, Object> getResponseHeaderMap(CloseableHttpResponse response) {
		Map<String, Object> headerMap = new HashMap<String, Object>();
		HeaderIterator it = response.headerIterator();  
		while (it.hasNext()) {
			Header header = it.nextHeader();
			if (!header.getName().trim().equalsIgnoreCase("Set-Cookie")) {
				headerMap.put(header.getName(), header.getValue());  
			}
		}
		return headerMap;
	}
	
	public static List<String> getResponseCookieList(CloseableHttpResponse response) {
		List<String> cookieList = new ArrayList<String>();
		HeaderIterator it = response.headerIterator();  
		while (it.hasNext()) {
			Header header = it.nextHeader();
			if (header.getName().trim().equalsIgnoreCase("Set-Cookie")) {
				cookieList.add(header.getValue());  
			}
		}
		return cookieList;
	}
	
	public static String getBodyStringFromResponse(CloseableHttpResponse response) {
		String bodyAsString = null;
		HttpEntity entity = response.getEntity();
		if (entity == null) { // statusCode=300+的时候是没有entity的，这里直接返回null
			return null;
		}
		try {
			bodyAsString = EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bodyAsString;
	}
	
	public static byte[] getBodyByteArrayFromResponse(CloseableHttpResponse response) {
		byte[] byteArray = null;
		HttpEntity entity = response.getEntity();
		if (entity == null) { // statusCode=300+的时候是没有entity的，这里直接返回null
			return null;
		}
		try {
			byteArray = EntityUtils.toByteArray(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArray;
	}
	
	public static void printResponse(CloseableHttpResponse response) {
		System.out.println(response);
		try {
			HttpEntity entity = response.getEntity();
			entity = new BufferedHttpEntity(entity); // 转化成BufferedHttpEntity，可以重复读取Entity的内容
			String bodyAsString = EntityUtils.toString(entity);
			System.out.println(bodyAsString);
			
			System.out.println(entity.getContentType());  // 获取Header的 Content-Type
			System.out.println(entity.getContentLength());  // 获取Header的 Content-Length
			System.out.println(entity.getContentEncoding());
			
			/**
			 * 强烈不推荐使用EntityUtils这个类，除非目标服务器发出的响应是可信任的，并且http响应实体的长度不会过大
			 * 而应该使用HttpEntity的getConent()方法或者HttpEntity的writeTo(OutputStream)方法来读取Http实体内容
			 */
			System.out.println(EntityUtils.toString(entity));  
			System.out.println(EntityUtils.toByteArray(entity).length);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
