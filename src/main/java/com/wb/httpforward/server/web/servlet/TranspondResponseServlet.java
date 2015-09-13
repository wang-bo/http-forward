package com.wb.httpforward.server.web.servlet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wb.httpforward.enums.CodeEnum;
import com.wb.httpforward.server.service.TranspondHttpService;
import com.wb.httpforward.util.ResponseUtil;

/**
 * @author www
 * @date 2015年9月12日
 */
@WebServlet(urlPatterns = { "/transpondResponse" })
public class TranspondResponseServlet extends HttpServlet {

	private static final long serialVersionUID = 1699046038310039950L;
	
	private TranspondHttpService transpondHttpService
		= TranspondHttpService.getInstance();
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String responseMessageString = request.getParameter("response");
		if (responseMessageString != null) {
			transpondHttpService.transpondHttpResponse(responseMessageString);
		}
		ResponseUtil.setCommonHeader(response);
		ResponseUtil.sendResponse(response, "{\"code\":" + CodeEnum.SUCCESS.val + ", \"message\":\"successs\"}");
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
}
