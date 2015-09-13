package com.wb.httpforward.server.a_no_user_package.service;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wb.httpforward.server.a_no_user_package.client.ClientHolder;
import com.wb.httpforward.server.a_no_user_package.client.RequesterClient;
import com.wb.httpforward.server.a_no_user_package.client.TransponderClient;
import com.wb.httpforward.util.ResponseMessage;

/**
 * @author www
 * @date 2015年9月9日
 * 
 * 请求转发服务
 */

@Service
public class TranspondHttpService {
	
	private static int transpondRequestCount = 0;
	
	private static int transpondResponseCount = 0;

	/**
	 * 请求转发方法的简单实现
	 * @param clientNo
	 * @param responseValue
	 */
	public void transpondHttpRequest(final String clientNo, final String requestMessageString) {
    	System.out.println(++transpondRequestCount + " need transpondRequest : " + requestMessageString);
    	
		// 用开启新线程的方式简单实现异步，以后再改成线程池
		new Thread(new Runnable() {
			public void run() {
				TransponderClient client = null;
				// 尝试发送10次，这里简单实现下，当有多个消息积压的时候，会开启多个线程，会失去这些消息的顺序，而且50秒后客户端未接收消息，就会丢失。
				// 更健壮的实现应该使用任务队列
				for (int i = 0; i < 10; i++) {
					client = ClientHolder.getInstance().getTransponderClient(clientNo);
					if (client == null) {
						try {
							TimeUnit.SECONDS.sleep(5); // 没获取到的话休眠5秒再重试
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (i == 9) {
							System.out.println("no client with clientNo: " + clientNo);
						}
						continue;
					}
					client.responseValue(requestMessageString);
					break;
				}
			}
		}).start();
	}
	
	/**
	 * 转发response给请求端
	 * @param responseMessage
	 */
	public void transpondHttpResponse(String responseMessageString) {
    	System.out.println(++transpondResponseCount + " need transpondResponse: " + responseMessageString);
    	
    	ResponseMessage responseMessage = JSON.parseObject(responseMessageString, ResponseMessage.class);
    	Integer code = responseMessage.getCode();
    	if (code != null) {
        	RequesterClient client = ClientHolder.getInstance().getRequesterClient(code);
        	if (client != null) {
        		client.response(responseMessage);
        	}
    	}
	}
}
