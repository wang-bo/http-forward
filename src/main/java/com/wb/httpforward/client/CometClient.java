package com.wb.httpforward.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wb.httpforward.client.handler.RequestMessageResponseHandler;
import com.wb.httpforward.client.service.TranspondHttpService;
import com.wb.httpforward.constant.ClientConstant;
import com.wb.httpforward.enums.CodeEnum;
import com.wb.httpforward.util.HttpClientUtil;
import com.wb.httpforward.util.RequestMessage;

/**
 * @author www
 * @date 2015年9月9日
 * 
 * http转发服务中，维持长连接的客户端
 */

public class CometClient implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(CometClient.class);
	
	/**
	 * 维持长连接的HTTP方法，目前支持GET、POST
	 */
	private String method;
	
	/**
	 * comet服务器维持长连接的接口地址
	 */
	private String serverUrl;
	
	/**
	 * 客户端编号，服务器可以和多个客户端维持长连接，每个客户端需要一个特定的编号
	 */
	private String clientNo;
	
	/**
	 * 标记client是否已启动
	 */
	private Boolean isStarted;
	
	public CometClient(String method, String serverUrl, String clientNo) {
		this.method = method;
		this.serverUrl = serverUrl;
		this.clientNo = clientNo;
		this.isStarted = false;
	}
	
	/**
	 * 启动客户端，维持长链接，并转发请求
	 */
	public void start() {
		// 参数不正确，直接返回
		if (method == null || serverUrl == null || clientNo == null) {
			System.out.println("start Client error: parameter is null");
			log.error("start Client error: parameter is null");
			return;
		}
		// 客户端已启动的话直接返回
		synchronized (isStarted) {
			if (isStarted) { 
				return;
			}
			isStarted = true;
		}
		new Thread(this).start();
		log.info("CometClient started： serverUrl = " + serverUrl + ", clientNo = " + clientNo);
		log.error("CometClient started： serverUrl = " + serverUrl + ", clientNo = " + clientNo);
	}

	@Override
	public void run() {
		CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();  
		HttpRequestBase httpRequest = null;
		if (method.equalsIgnoreCase("POST")) {
			HttpPost httpPost = new HttpPost(serverUrl);
			List<NameValuePair> params = new ArrayList<NameValuePair>();  
			params.add(new BasicNameValuePair("clientNo", clientNo));  
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);  
			httpPost.setEntity(entity);
			httpRequest = httpPost;
		} else {
			httpRequest = new HttpGet(serverUrl + "?clientNo=" + clientNo);
		}
		ResponseHandler<RequestMessage> responseHandler = new RequestMessageResponseHandler();
		
		while(true) {
			try {
				RequestMessage requestMessage = httpClient.execute(httpRequest, responseHandler);
				// server response error
				if (requestMessage.getCode() == -1) { 
					System.out.println("error: " + requestMessage.getMessage());
					log.error("error: " + requestMessage.getMessage());
					break;
				}
				// 需要转发
				else if (requestMessage.getCode() > CodeEnum.SUCCESS.val) { 
					// 暂时简单实现，健壮的实现应该使用任务队列
					new Thread(new TranspondHttpService(requestMessage)).start();
				}
			} catch (Exception e) { // 无论发生何种异常，重新发起请求
				System.out.println(e.getClass().getSimpleName());
				log.error("comet heartbeat error: ", e);
			}
		}
	}
	
	public static void main(String[] args) {
		String method = ClientConstant.COMET_HTTP_METHOD;
		String serverUrl = ClientConstant.HTTP_FORWARD_SERVER_HOLD_REQUEST_URL;
		String clientNo = ClientConstant.LOCAL_CLIENT_NO;
		if (args.length > 0) {
			if (!args[0].startsWith("http://")) {
				ClientConstant.TARGET_SERVER_URL = "http://" + args[0];
			} else {
				ClientConstant.TARGET_SERVER_URL = args[0];
			}
		}
		if (args.length > 1) {
			clientNo = args[1];
		}
		if (args.length > 2) {
			method = args[2];
		}
		CometClient client = new CometClient(method, serverUrl, clientNo);
		client.start();
	}
}
