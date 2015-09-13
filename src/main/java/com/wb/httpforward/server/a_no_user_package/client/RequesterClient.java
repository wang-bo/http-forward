package com.wb.httpforward.server.a_no_user_package.client;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.async.DeferredResult;

import com.wb.httpforward.enums.CodeEnum;
import com.wb.httpforward.util.ResponseMessage;

/**
 * @author www
 * @date 2015年9月11日
 */

public class RequesterClient {

	private Integer code;
	
	private HttpServletResponse response;
	
	private DeferredResult<String> result;

	public RequesterClient(Integer code, HttpServletResponse response,
			DeferredResult<String> result) {
		this.code = code;
		this.response = response;
		this.result = result;
		
		result.onCompletion(new Runnable() {
			public void run() {
				remove();
			}
		});
		result.onTimeout(new Runnable() {
			public void run() {
				responseTimeout();
			}
		});
	}
	
	public void response(ResponseMessage responseMessage) {
		Map<String, Object> headerMap = responseMessage.getHeaderMap();
		List<String> cookieList = responseMessage.getCookieList();
		String body = responseMessage.getBody();
		for (String headerName: headerMap.keySet()) {
			if (!headerName.equalsIgnoreCase("Content-Length")) {
				response.setHeader(headerName, String.valueOf(headerMap.get(headerName)));
			}
		}
		for (String cookieValue: cookieList) {
			// addHeader()会增加，setHeader()会覆盖，
			// 此处如果有多个cookie值，使用setHeader()方法会覆盖成只有最有一个cookie，所以用addHeader()
			response.addHeader("Set-Cookie", cookieValue);
		}
		result.setResult(body);
	}
	
	public void responseTimeout() {
	    result.setResult("{\"code\":" + CodeEnum.TIMEOUT.val + ", \"message\":\"server time out.\"}");
	}
	
	public void add() {
		ClientHolder.getInstance().setRequesterClient(code, this);
	}
	
	public void remove() {
		ClientHolder.getInstance().removeRequesterClient(code);
	}
	
}
