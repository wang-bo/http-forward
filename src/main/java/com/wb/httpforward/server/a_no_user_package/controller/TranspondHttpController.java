package com.wb.httpforward.server.a_no_user_package.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.alibaba.fastjson.JSON;
import com.wb.httpforward.constant.ServerConstant;
import com.wb.httpforward.enums.CodeEnum;
import com.wb.httpforward.server.a_no_user_package.client.RequesterClient;
import com.wb.httpforward.server.a_no_user_package.service.TranspondHttpService;
import com.wb.httpforward.util.CommonMethod;
import com.wb.httpforward.util.RequestMessage;
import com.wb.httpforward.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by www on 2015/9/7 0007.
 * 
 * 用来转发请求的控制器
 */

@Controller
public class TranspondHttpController {
	
	@Autowired
	private TranspondHttpService transpondHttpService;

	/**
	 * 待转发的请求，转发给clientNo代表的客户端
	 * @param request
	 * @param clientNo
	 * @return
	 */
    @RequestMapping(value = {"/transpondRequest/{clientNo}", "/transpondRequest/{clientNo}/**"}, 
    		method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    DeferredResult<String> transpondRequest(HttpServletRequest request, 
    		HttpServletResponse response,
    		@PathVariable(value = "clientNo") String clientNo) {
    	DeferredResult<String> result = new DeferredResult<String>(ServerConstant.SERVER_HOLD_REQUEST_TIMEOUT);
    	// 过滤非法请求
    	if (!CommonMethod.isCorrectClientNo(clientNo) 
    			// || "/callback/transpondRequest/".contains(clientNo)
    			) {
    		result.setResult("{\"code\":" + CodeEnum.ERROR.val + ", \"message\":\"wrong uri.\"}");
    		return result;
    	}
    	// 组装请求数据
    	Integer code = CommonMethod.codeGenerator();
    	String pathInfo = request.getPathInfo();
    	pathInfo = pathInfo.substring(pathInfo.indexOf(clientNo) + clientNo.length());
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
    	new RequesterClient(code, response, result).add();
    	return result;
    }
    
    /**
	 * 客户端转发回来的response，根据code转发给相应的request端
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/transpondResponse", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String transpondResponse(HttpServletRequest request,
    		@RequestParam(value = "response", required = false) String responseMessageString) {
    	if (responseMessageString != null) {
    		transpondHttpService.transpondHttpResponse(responseMessageString);
    	}
        return "{\"code\":" + CodeEnum.SUCCESS.val + ", \"message\":\"successs\"}";
    }
    
    
}
