package com.wb.httpforward.server.web.listener;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import com.wb.httpforward.server.client.RequesterClient;

/**
 * @author www
 * @date 2015年9月12日
 */

public class TranspondRequestAsyncListener implements AsyncListener {
	
	private RequesterClient client;
	
	public TranspondRequestAsyncListener(RequesterClient client) {
		super();
		this.client = client;
	}

	@Override
	public void onComplete(AsyncEvent arg0) throws IOException {
		client.remove();
	}

	@Override
	public void onError(AsyncEvent arg0) throws IOException {
		client.remove();
	}

	@Override
	public void onStartAsync(AsyncEvent arg0) throws IOException {

	}

	@Override
	public void onTimeout(AsyncEvent arg0) throws IOException {
		client.responseTimeout();
    	client.remove();
	}

}
