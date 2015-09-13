package com.wb.httpforward.server.a_no_user_package.servlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wb.httpforward.constant.ServerConstant;
import com.wb.httpforward.enums.CodeEnum;
import com.wb.httpforward.server.a_no_user_package.client.ClientHolder;
import com.wb.httpforward.server.a_no_user_package.client.TransponderClient;
import com.wb.httpforward.server.a_no_user_package.listener.HoldRequestAsyncListener;
import com.wb.httpforward.util.CommonMethod;
import com.wb.httpforward.util.ResponseUtil;

/**
 * @author www
 * @date 2015年9月9日
 * 
 * 用来阻塞客户端请求的Servlet。
 * 也可以写在Spring MVC的控制器中，注意：需配置servlet的async-supported属性为true，以便Spring MVC支持异步操作。
 */

@WebServlet(urlPatterns = { "/holdRequest" }, asyncSupported = true)
public class HoldRequestServlet extends HttpServlet {

	private static final long serialVersionUID = -7365961658935824061L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ResponseUtil.setCommonHeader(response);
		String clientNo = request.getParameter("clientNo");
		// clientNo参数错误的话直接返回
		if (!CommonMethod.isCorrectClientNo(clientNo)) {
			ResponseUtil.sendResponse(response, "{\"code\":" + CodeEnum.ERROR.val + ", \"message\":\"no clientNo or clientNo is wrong.\"}");
        	return;
		}
    	
		// 开启异步
    	final AsyncContext asyncContext = request.startAsync();
    	TransponderClient client = ClientHolder.getInstance().getTransponderClient(clientNo);
    	if (client == null) {
    		client = new TransponderClient(clientNo, asyncContext);
    	}
    	client.setAsyncContext(asyncContext);
    	client.add(); 
    	asyncContext.addListener(new HoldRequestAsyncListener(client));
    	asyncContext.setTimeout(ServerConstant.SERVER_HOLD_REQUEST_TIMEOUT);
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
	
	
}
