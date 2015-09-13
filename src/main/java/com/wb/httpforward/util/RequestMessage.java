package com.wb.httpforward.util;

import java.util.Map;

/**
 * @author www
 * @date 2015年9月10日
 */

public class RequestMessage {

	private Integer code; // 状态码：-1=error, 0=server time out, >0=need forward request
	
	private String message;
	
	private Map<String, Object> metaMap;
	
	private Map<String, Object> headerMap;
	
	private Map<String, Object> parameterMap;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getMetaMap() {
		return metaMap;
	}

	public void setMetaMap(Map<String, Object> metaMap) {
		this.metaMap = metaMap;
	}

	public Map<String, Object> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, Object> headerMap) {
		this.headerMap = headerMap;
	}

	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(Map<String, Object> parameterMap) {
		this.parameterMap = parameterMap;
	}

	@Override
	public String toString() {
		return "code=" + code 
				+ (message == null ? "" : ", message=" + message)
				+ (metaMap == null ? "" : ", metaMap=" + metaMap)
				+ (headerMap == null ? "" : ", headerMap=" + headerMap)
				+ (parameterMap == null ? "" : ", parameterMap=" + parameterMap);
	}
	
	
}
