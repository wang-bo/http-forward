package com.wb.httpforward.client.handler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.wb.httpforward.util.RequestMessage;

/**
 * @author www
 * @date 2015年9月10日
 */

public class RequestMessageResponseHandler implements ResponseHandler<RequestMessage> {

	@Override
	public RequestMessage handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		StatusLine statusLine = response.getStatusLine();  
        HttpEntity entity = response.getEntity();  
        if (statusLine.getStatusCode() >= 300) {  
            throw new HttpResponseException(  
                    statusLine.getStatusCode(),  
                    statusLine.getReasonPhrase());  
        }  
        if (entity == null) {  
            throw new ClientProtocolException("Response contains no content");  
        }  
        String bodyAsString = EntityUtils.toString(entity);
        RequestMessage requestMessage = JSON.parseObject(bodyAsString, RequestMessage.class);
        return requestMessage;
	}

}
