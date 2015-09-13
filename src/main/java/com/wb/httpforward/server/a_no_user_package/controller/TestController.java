package com.wb.httpforward.server.a_no_user_package.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wb.httpforward.util.RequestUtil;


@Controller
public class TestController {
	
	@RequestMapping(value = {"/parseRequest", "/parseRequest/**"}, method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String parseRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> metaMap = RequestUtil.getMetaMap(request); 
		Map<String, Object> headerMap = RequestUtil.getHeaderMap(request); 
		Map<String, Object> parameterMap = RequestUtil.getParameterMap(request);
		Map<String, Object> returnMap = new HashMap<String, Object>();
    	returnMap.put("metaMap", metaMap);
    	returnMap.put("headerMap", headerMap);
    	returnMap.put("parameterMap", parameterMap);
    	String returnString = JSON.toJSONString(returnMap);
    	System.out.println(returnString);
        return returnString;
	}

	@RequestMapping(value = "/json", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String json(HttpServletRequest request) {
    	Map<String, Object> returnMap = new HashMap<String, Object>();
    	returnMap.put("name", "www");
    	returnMap.put("age", 21);
    	String returnString = JSON.toJSONString(returnMap);
    	System.out.println(returnString);
        return returnString;
    }
    
    @RequestMapping(value = "/xml", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String xml(HttpServletRequest request) {
    	String returnString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    	returnString += "<root><name>www</name><age>21</age></root>";
    	System.out.println(returnString);
        return returnString;
    }
    
    @RequestMapping(value = "/testRequest", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String testRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	String method = request.getMethod();
    	String protocol = request.getProtocol();
    	String scheme = request.getScheme();
    	String serverName = request.getServerName();
    	int serverPort = request.getServerPort();
    	
    	String authType = request.getAuthType();
    	String contextPath = request.getContentType();
    	String pathInfo = request.getPathInfo();
    	String pathTranslated = request.getPathTranslated();
    	String queryString = request.getQueryString();
    	String requestUri = request.getRequestURI();
    	String requestUrl = request.getRequestURL().toString();
    	String servletPath = request.getServletPath();
    	
    	String clientIp = RequestUtil.getClientIp(request);
    	
    	System.out.println("\n\n\n\n\n\nrequestPath:");
    	System.out.println("\t method: " + method);
    	System.out.println("\t protocol: " + protocol);
    	System.out.println("\t scheme: " + scheme);
    	System.out.println("\t serverName: " + serverName);
    	System.out.println("\t serverPort: " +  serverPort);
    	System.out.println("\t requestUri :" + requestUri);
    	System.out.println("\t queryString :" + queryString);
    	System.out.println("\t authType: " + authType);
    	System.out.println("\t contextPath :" + contextPath);
    	System.out.println("\t pathInfo :" + pathInfo);
    	System.out.println("\t pathTranslated :" + pathTranslated);
    	System.out.println("\t servletPath :" + servletPath);
    	System.out.println("\t clientIp :" + clientIp);
    	System.out.println("\t requestUrl :" + requestUrl);
    	System.out.println("\t my     Url :" + scheme + "://" + serverName + ":" + serverPort + requestUri + "?" + queryString);
    	
    	System.out.println("\nHeader:");
    	Enumeration<String> headerNames = request.getHeaderNames();
    	while (headerNames.hasMoreElements()) {
    		String headerName = headerNames.nextElement();
    		System.out.println("\t " + headerName + ": " + request.getHeader(headerName));
    	}
    	
    	System.out.println("\ncookie:");
    	Cookie[] cookies = request.getCookies();
    	if (cookies != null) {
	    	for (Cookie c: cookies) {
	    		System.out.println("\t " + c.getName() + " " + c.getValue() + " " + c.getPath());
	    	}
    	}
    	
    	System.out.println("\nParameter:");
    	Enumeration<String> parameterNames = request.getParameterNames();
    	while (parameterNames.hasMoreElements()) {
    		String parameterName = parameterNames.nextElement();
    		System.out.println("\t " + parameterName + ": " + request.getParameter(parameterName));
    	}
    	
    	System.out.println("\nInputStream:");
//    	String input = IOUtils.toString(request.getInputStream());
//    	System.out.println("\t " + input);
    	
    	BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
    	StringBuilder sb = new StringBuilder();
    	String line = null;
    	while ((line = br.readLine()) != null) {
    		sb.append(line + "\n");
    	}
    	System.out.println("\t " + sb.toString());
    	
    		
    	
    	
    	
//    	System.out.println(response);
    	
    	Cookie cookie = new Cookie("a", "aaa");
    	cookie.setPath("/");
    	response.addCookie(cookie);
    	cookie = new Cookie("b", "bbb");
    	response.addCookie(cookie);
    	
    	response.addHeader("Set-Cookie", "bbb=bbb");

    	String ip = request.getHeader("X-Forwarded-For");
    	String returnString = "abcdefghijklmn, and clientIp = " + ip;
    	return returnString;
    }
    
}
