package com.wb.httpforward.server.web.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.wb.httpforward.constant.ServerConstant;
import com.wb.httpforward.enums.CodeEnum;
import com.wb.httpforward.server.client.RequesterClient;
import com.wb.httpforward.server.service.TranspondHttpService;
import com.wb.httpforward.server.web.listener.TranspondRequestAsyncListener;
import com.wb.httpforward.util.CommonMethod;
import com.wb.httpforward.util.RequestMessage;
import com.wb.httpforward.util.RequestUtil;
import com.wb.httpforward.util.ResponseUtil;

/**
 * @author www
 * @date 2015年9月11日
 */

@WebServlet(urlPatterns = { "/transpondRequest/*" }, asyncSupported = true)
public class TranspondRequestServlet extends HttpServlet {

	private static final long serialVersionUID = -7744835458987531055L;
	
	private TranspondHttpService transpondHttpService
		= TranspondHttpService.getInstance();

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.length() < 1) {
    		ResponseUtil.setCommonHeader(response);
    		ResponseUtil.sendResponse(response, "{\"code\":" + CodeEnum.ERROR.val + ", \"message\":\"wrong uri.\"}");
    		return;
		}
    	pathInfo = request.getPathInfo().substring(1);
    	String clientNo = null;
    	if (pathInfo.contains("/")) {
    		clientNo = pathInfo.substring(0, pathInfo.indexOf("/"));
    	} else {
    		clientNo = pathInfo;
    	}
    	pathInfo = pathInfo.substring(pathInfo.indexOf(clientNo) + clientNo.length());

    	// 过滤非法请求
    	if (!CommonMethod.isCorrectClientNo(clientNo)) {
    		ResponseUtil.setCommonHeader(response);
    		ResponseUtil.sendResponse(response, "{\"code\":" + CodeEnum.ERROR.val + ", \"message\":\"wrong uri.\"}");
    		return;
    	}
    	// 组装请求数据
    	Integer code = CommonMethod.codeGenerator();
    	Map<String, Object> metaMap = RequestUtil.getMetaMap(request); 
    	metaMap.put("path", pathInfo);
		Map<String, Object> headerMap = RequestUtil.getHeaderMap(request); 
		Map<String, Object> parameterMap = RequestUtil.getParameterMap(request);
		RequestMessage requestMessage = new RequestMessage();
		requestMessage.setCode(code);
		requestMessage.setMetaMap(metaMap);
		requestMessage.setHeaderMap(headerMap);
		requestMessage.setParameterMap(parameterMap);
    	String requestMessageString = JSON.toJSONString(requestMessage);
    	// 调用转发服务，转发请求数据给clientNo代表的客户端
    	transpondHttpService.transpondHttpRequest(clientNo, requestMessageString);
    	// 异步处理，需等待clientNo代表的客户端返回结果，再把结果返回。这里如果无需等待处理结果的话，可以不要异步，直接返回。
    	final AsyncContext asyncContext = request.startAsync();
    	RequesterClient client = new RequesterClient(code, asyncContext);
    	client.add();
    	asyncContext.addListener(new TranspondRequestAsyncListener(client));
    	asyncContext.setTimeout(ServerConstant.SERVER_HOLD_REQUEST_TIMEOUT);
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
}
