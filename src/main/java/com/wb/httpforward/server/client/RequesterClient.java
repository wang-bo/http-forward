package com.wb.httpforward.server.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;

import com.wb.httpforward.enums.CodeEnum;
import com.wb.httpforward.server.client.ClientHolder;
import com.wb.httpforward.util.Base64Util;
import com.wb.httpforward.util.ResponseMessage;
import com.wb.httpforward.util.ResponseUtil;

/**
 * @author www
 * @date 2015年9月11日
 */

public class RequesterClient {

	private Integer code;
	
	private AsyncContext asyncContext;
	
	public RequesterClient(Integer code, AsyncContext asyncContext) {
		this.code = code;
		this.asyncContext = asyncContext;
	}
	
	public void response(ResponseMessage responseMessage) {
		try {
			HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
			int statusCode = responseMessage.getStatusCode();
			response.setStatus(statusCode);
			Map<String, Object> headerMap = responseMessage.getHeaderMap();
			List<String> cookieList = responseMessage.getCookieList();
			String body = responseMessage.getBody();
			for (String headerName: headerMap.keySet()) {
			//	if (!headerName.equalsIgnoreCase("Content-Length")) {
					response.setHeader(headerName, String.valueOf(headerMap.get(headerName)));
			//	}
			}
			for (String cookieValue: cookieList) {
				// addHeader()会增加，setHeader()会覆盖，
				// 此处如果有多个cookie值，使用setHeader()方法会覆盖成只有最有一个cookie，所以用addHeader()
				response.addHeader("Set-Cookie", cookieValue);
			}
			if (body != null) {
				if (responseMessage.isBase64()) {
					ResponseUtil.sendResponse(response, Base64Util.decode(body));
				} else {
					ResponseUtil.sendResponse(response, body);
				}
			}
			asyncContext.complete();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			remove(); // 发送这个异常表示response已返回过，需要remove掉。
		}
	}
	
	public void responseTimeout() throws IOException {
		try {
			HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
			ResponseUtil.setCommonHeader(response);
			ResponseUtil.sendResponse(response, "{\"code\":" + CodeEnum.TIMEOUT.val + ", \"message\":\"server time out.\"}");
			asyncContext.complete();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			remove(); // 发送这个异常表示response已返回过，需要remove掉。
		}
	}

	public void add() {
		ClientHolder.getInstance().setRequesterClient(code, this);
	}
	
	public void remove() {
		ClientHolder.getInstance().removeRequesterClient(code);
	}
}
