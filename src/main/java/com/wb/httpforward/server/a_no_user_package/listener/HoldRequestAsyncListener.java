package com.wb.httpforward.server.a_no_user_package.listener;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import com.wb.httpforward.server.a_no_user_package.client.TransponderClient;

/**
 * @author www
 * @date 2015年9月9日
 */

public class HoldRequestAsyncListener implements AsyncListener {
	
	private TransponderClient client;
	
	public HoldRequestAsyncListener(TransponderClient client) {
		super();
		this.client = client;
	}

	@Override
	public void onComplete(AsyncEvent event) throws IOException {
		client.remove();
	}

	@Override
	public void onError(AsyncEvent event) throws IOException {
		client.remove();
	}

	@Override
	public void onStartAsync(AsyncEvent event) throws IOException {
	}

	@Override
	public void onTimeout(AsyncEvent event) throws IOException {
		client.responseTimeout();
    	client.remove();
	}

}
