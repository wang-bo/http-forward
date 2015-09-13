package com.wb.httpforward.server.client;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.async.DeferredResult;

import com.wb.httpforward.enums.CodeEnum;
import com.wb.httpforward.server.client.ClientHolder;

/**
 * @author www
 * @date 2015年9月11日
 */

public class TransponderClient {

	private String clientNo;
	
	private HttpServletResponse response;
	
	private DeferredResult<String> result;
	
	public TransponderClient(String clientNo, HttpServletResponse response,
			DeferredResult<String> result) {
		this.clientNo = clientNo;
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
	
	public void responseValue(String responseValue) {
		result.setResult(responseValue);
	}
	
	public void responseTimeout() {
		response.setHeader("timeout1", "1");
    	response.addHeader("timeout2", "2");
	    result.setResult("{\"code\":" + CodeEnum.TIMEOUT.val + ", \"message\":\"server time out.\"}");
	}

	public void add() {
		ClientHolder.getInstance().setTransponderClient(clientNo, this);
	}
	
	public void remove() {
		ClientHolder.getInstance().removeTransponderClient(clientNo);
	}
}
