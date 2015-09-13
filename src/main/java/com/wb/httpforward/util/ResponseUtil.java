package com.wb.httpforward.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author www
 * @date 2015年9月9日
 */

public class ResponseUtil {
	
	public static void setCommonHeader(HttpServletResponse response) {
		response.setHeader("Connection", "Keep-Alive");
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Pragma", "no-cache");
		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
	}

	public static void sendResponse(ServletResponse response, String content) throws IOException {
		PrintWriter out = response.getWriter();
    	out.write(content);
    	out.flush(); // 或者使用 ，response.flushBuffer()效果相同
	}
	
	public static void sendResponse(ServletResponse response, byte[] content) throws IOException {
		ServletOutputStream out = response.getOutputStream();
		out.write(content);
    	out.flush(); // 或者使用 ，response.flushBuffer()效果相同
	}
	
	public static void sendResponse(AsyncContext asyncContext, String content) throws IOException {
		ServletResponse response = asyncContext.getResponse();
		PrintWriter out = response.getWriter();
    	out.write(content);
    	out.flush(); // 或者使用 ，response.flushBuffer()效果相同
	}
	
}
