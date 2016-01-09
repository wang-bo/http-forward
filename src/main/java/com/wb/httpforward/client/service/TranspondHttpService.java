package com.wb.httpforward.client.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.wb.httpforward.constant.ClientConstant;
import com.wb.httpforward.util.Base64Util;
import com.wb.httpforward.util.HttpClientUtil;
import com.wb.httpforward.util.RequestMessage;
import com.wb.httpforward.util.ResponseMessage;

/**
 * @author www
 * @date 2015年9月10日
 */

public class TranspondHttpService implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(TranspondHttpService.class);
	
	private RequestMessage requestMessage;

	public TranspondHttpService(RequestMessage requestMessage) {
		this.requestMessage = requestMessage;
	}
	
	private static int transpondRequestCount = 0;
	
	private static int transpondResponseCount = 0;

	@Override
	public void run() {
		log.info(++transpondRequestCount + " client transpond request : " + JSON.toJSONString(requestMessage));
		Integer code = requestMessage.getCode();
		Map<String, Object> metaMap = requestMessage.getMetaMap();
		Map<String, Object> headerMap = requestMessage.getHeaderMap();
		Map<String, Object> parameterMap = requestMessage.getParameterMap();
		
		String method = metaMap.get("method").toString();
		String clientIp = metaMap.get("clientIp").toString();
		String queryString = metaMap.get("queryString") == null ? null : metaMap.get("queryString").toString();
		String path = metaMap.get("path").toString();
		
		String targetServerUrl = ClientConstant.TARGET_SERVER_URL + path;
		
		HttpRequestBase httpRequest = null;
		if (method.equalsIgnoreCase("POST")) {
			HttpPost httpPost = new HttpPost(targetServerUrl);
			List<NameValuePair> params = new ArrayList<NameValuePair>();  
			for (String parameterName: parameterMap.keySet()) {
				params.add(new BasicNameValuePair(parameterName, String.valueOf(parameterMap.get(parameterName)))); 
			} 
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);  
			httpPost.setEntity(entity);
			httpRequest = httpPost;
		} else {
			httpRequest = new HttpGet(targetServerUrl 
					+ (queryString == null ? "" :"?" + queryString));
		}
		for (String headerName: headerMap.keySet()) {
			if (!headerName.equalsIgnoreCase("Content-Length") && !headerName.equalsIgnoreCase("Host")) {
				httpRequest.addHeader(headerName, String.valueOf(headerMap.get(headerName)));
			}
		}
		httpRequest.addHeader("X-Forwarded-For", clientIp);
		
		CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			Map<String, Object> responseHeaderMap = HttpClientUtil.getResponseHeaderMap(response);
			List<String> cookieList = HttpClientUtil.getResponseCookieList(response);
			boolean isBase64 = false;
			String bodyString = null;
			String contentType = response.getFirstHeader("Content-Type") == null 
					? null : response.getFirstHeader("Content-Type").getValue();
			if (contentType != null && (contentType.contains("text")
					|| contentType.contains("json") || contentType.contains("javascript"))) {
				bodyString = HttpClientUtil.getBodyStringFromResponse(response);
			} else {
				isBase64 = true;
				byte[] bodyByteArray = HttpClientUtil.getBodyByteArrayFromResponse(response);
				bodyString = Base64Util.encode(bodyByteArray);
			}
			
			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setCode(code);
			responseMessage.setStatusCode(statusCode);
			responseMessage.setHeaderMap(responseHeaderMap);
			responseMessage.setCookieList(cookieList);
			responseMessage.setBody(bodyString);
			responseMessage.setBase64(isBase64);
			String responseMessageString = JSON.toJSONString(responseMessage);
			
			if (ClientConstant.LOG_WITH_BODY || responseMessage.getBody() == null) {
				log.info(++transpondResponseCount + " client transpond response: " + responseMessageString);
			} else {
				log.info(++transpondResponseCount + " client transpond response: " + responseMessage.logStringWithNoBody());
			}
			
			String transpondResponseUrl = ClientConstant.HTTP_FORWARD_SERVER_TRANSPOND_RESPONSE_URL;
			HttpPost httpPost = new HttpPost(transpondResponseUrl);
			List<NameValuePair> params = new ArrayList<NameValuePair>();  
			params.add(new BasicNameValuePair("response", responseMessageString)); 
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);  
			httpPost.setEntity(entity);
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			log.error("client transpord request error: ", e);
		} catch (IOException e) {
			log.error("client transpord request error: ", e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					log.error("client transpord request error: ", e);
				}
			}
		}
	}

}
