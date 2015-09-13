package com.wb.httpforward.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.wb.httpforward.constant.ClientConstant;
import com.wb.httpforward.util.HttpClientUtil;

public class TestHttpClient {

	public static void main(String[] args) throws Exception {
		//sendGet();
		//sendPost();
		sendTest();
	}
	
	public static void sendTest() throws ClientProtocolException, IOException {
		String targetServerUrl = ClientConstant.TARGET_SERVER_URL;
		CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
		
		HttpGet httpGet = new HttpGet(targetServerUrl);
		
		CloseableHttpResponse response = httpClient.execute(httpGet);
		String bodyAsString = HttpClientUtil.getBodyStringFromResponse(response);
		System.out.println(bodyAsString);
	}
	
	public static void sendGet() throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = getClient();  
		HttpGet httpGet = new HttpGet("http://127.0.0.1:8880/test?a=2&t=3&myParam=abcdefg#333");
		httpGet.addHeader("myHead", "gggggggggggggggggggggggggggggggg");
		httpGet.addHeader("cookie", "a=aaa; b=bbb; a=aaa");
		CloseableHttpResponse response = httpClient.execute(httpGet);
		response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost"); 
		printResponse(response);
		response.close();
	}
	
	public static void sendPost() throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = getClient();  
		HttpPost httpPost = new HttpPost("http://127.0.0.1:8880/callback/testRequest?a=2&t=3#333");
		httpPost.addHeader("myHead", "ppppppppppppppppppppppppppppppppppp");
		
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
		formparams.add(new BasicNameValuePair("param1", "value1"));  
		formparams.add(new BasicNameValuePair("param2", "value2"));  
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);  
		httpPost.setEntity(entity); 
		
		CloseableHttpResponse response = httpClient.execute(httpPost);
		response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost"); 
		// printResponse(response);
		response.close();
	}
	
	private static CloseableHttpClient getClient() {
		// 使用连接池
		
		// 设置HttpClient参数
		RequestConfig requestConfig = RequestConfig.custom()  
		        .setConnectTimeout(5000)  
		        .setSocketTimeout(10000)  
		        .build();
		
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.build();
		
		return httpClient;
	}
	
	private static void printResponse(CloseableHttpResponse response) {
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
