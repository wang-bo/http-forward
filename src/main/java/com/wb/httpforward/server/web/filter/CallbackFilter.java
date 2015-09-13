package com.wb.httpforward.server.web.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;

import java.io.IOException;

/**
 * Created by www on 2015/9/6 0006.
 */
@Component
public class CallbackFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        String requestUri = request.getRequestURI();
//        String charSet = request.getCharacterEncoding();
//		  System.out.println("in callbackFilter, requestUri = " + requestUri + ", charSet = " + charSet);
    	
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
