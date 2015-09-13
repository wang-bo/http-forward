package com.wb.httpforward.server.a_no_user_package.client;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;

import com.wb.httpforward.enums.CodeEnum;
import com.wb.httpforward.util.ResponseUtil;

/**
 * @author www
 * @date 2015年9月9日
 */

public class TransponderClient {

	private String clientNo;
	
	private AsyncContext asyncContext;
	
	public void setAsyncContext(AsyncContext asyncContext) {
		this.asyncContext = asyncContext;
	}

	public TransponderClient(String clientNo, AsyncContext asyncContext) {
		this.clientNo = clientNo;
		this.asyncContext = asyncContext;
	}
	
	public void responseValue(String responseValue) {
		try {
			ResponseUtil.sendResponse(asyncContext, responseValue);
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
	    	response.setHeader("timeout1", "1");
	    	response.addHeader("timeout2", "2");
    		ResponseUtil.sendResponse(response, "{\"code\":" + CodeEnum.TIMEOUT.val + ", \"message\":\"server time out.\"}");
    		asyncContext.complete();
    	} catch (IllegalStateException e) {
			e.printStackTrace();
			remove(); // 发送这个异常表示response已返回过，需要remove掉。
		}
	}
	
	public void add() {
		ClientHolder.getInstance().setTransponderClient(clientNo, this);
	}
	
	public void remove() {
		ClientHolder.getInstance().removeTransponderClient(clientNo);
	}
	
}
