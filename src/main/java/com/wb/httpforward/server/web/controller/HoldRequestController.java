package com.wb.httpforward.server.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.wb.httpforward.constant.ServerConstant;
import com.wb.httpforward.enums.CodeEnum;
import com.wb.httpforward.server.client.TransponderClient;
import com.wb.httpforward.util.CommonMethod;

/**
 * @author www
 * @date 2015年9月11日
 * 用来阻塞客户端请求的Controller，以维持长连接
 */

@Controller
public class HoldRequestController {

	/**
	 * 客户端转发回来的response，根据code转发给相应的request端
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/holdRequest", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    DeferredResult<String> holdRequest(HttpServletRequest request,
    		HttpServletResponse response,
    		@RequestParam(value = "clientNo", required = false) String clientNo) {
    	DeferredResult<String> result = new DeferredResult<String>(ServerConstant.SERVER_HOLD_REQUEST_TIMEOUT);
    	// clientNo参数错误的话直接返回
		if (!CommonMethod.isCorrectClientNo(clientNo)) {
			result.setResult("{\"code\":" + CodeEnum.ERROR.val + ", \"message\":\"no clientNo or clientNo is wrong.\"}");
        	return result;
		}
		// 异步处理，等待需转发的请求到来
		new TransponderClient(clientNo, response, result).add();
		return result;
    }
}
