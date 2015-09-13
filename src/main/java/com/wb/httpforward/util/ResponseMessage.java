package com.wb.httpforward.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * @author www
 * @date 2015年9月10日
 */

public class ResponseMessage {

	private Integer code; // 转发信息的编号
	
	private int statusCode;
	
	private Map<String, Object> headerMap;
	
	private List<String> cookieList;
	
	private String body;
	
	private boolean isBase64 = false;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, Object> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, Object> headerMap) {
		this.headerMap = headerMap;
	}
	
	public List<String> getCookieList() {
		return cookieList;
	}

	public void setCookieList(List<String> cookieList) {
		this.cookieList = cookieList;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public boolean isBase64() {
		return isBase64;
	}

	public void setBase64(boolean isBase64) {
		this.isBase64 = isBase64;
	}

	@Override
	public String toString() {
		return "code=" + code 
				+ ", statusCode=" + statusCode
				+ ", headerMap=" + headerMap
				+ ", cookieList=" + cookieList
				+ ", body=" + body
				+ ", isBase64=" + isBase64;
	}
	
	/**
	 * 返回没有body的json格式字符串
	 * @return
	 */
	public String logStringWithNoBody() {
		if (body == null) {
			return JSON.toJSONString(this);
		} else {
			String tmp = this.body;
			this.body = null;
			String logString = JSON.toJSONString(this);
			this.body = tmp;
			return logString;
		}
	}
	
}
