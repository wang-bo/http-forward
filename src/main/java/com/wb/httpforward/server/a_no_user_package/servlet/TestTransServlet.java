package com.wb.httpforward.server.a_no_user_package.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.wb.httpforward.util.Base64Util;
import com.wb.httpforward.util.HttpClientUtil;
import com.wb.httpforward.util.ResponseUtil;

/**
 * @author www
 * @date 2015年9月13日
 * 
 * 测试转发
 */

@WebServlet(urlPatterns = "/testTrans/*")
public class TestTransServlet extends HttpServlet {

	private static final long serialVersionUID = 5876741283936802358L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = "http://img1test.imyidian.com/ttt.jpg";
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
		CloseableHttpResponse res = httpClient.execute(httpGet);
		Map<String, Object> responseHeaderMap = HttpClientUtil.getResponseHeaderMap(res);
		List<String> cookieList = HttpClientUtil.getResponseCookieList(res);
//		String bodyAsString = HttpClientUtil.getBodyFromResponse(res);
		System.out.println("header: " + responseHeaderMap);
		System.out.println("cookie: " + cookieList);
//		System.out.println("body  : " + bodyAsString);
		
		HttpEntity entity = res.getEntity();
		entity = new BufferedHttpEntity(entity);
		printByteArray(EntityUtils.toByteArray(entity));
		InputStream inputStream = entity.getContent();
		printByteArray(IOUtils.toByteArray(inputStream));
		
		inputStream = entity.getContent();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
	    byte[] buffer = new byte[4096];
	    int n = 0;
	    while (-1 != (n = inputStream.read(buffer))) {
	        output.write(buffer, 0, n);
	    }
	    byte[] out =  output.toByteArray();
	    printByteArray(out);
	    String base64String = Base64Util.encode(out);
	    byte[] regain = Base64Util.decode(base64String);
	    System.out.println(base64String);
	    printByteArray(regain);
		
		ResponseUtil.sendResponse(response, "123");
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
	
	public static void printByteArray(byte[] byteArray) {
		StringBuilder sb = new StringBuilder();
		for (byte b: byteArray) {
			sb.append(b).append(" ");
		}
		System.out.println(sb);
	}
}
