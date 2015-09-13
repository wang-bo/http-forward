package com.wb.httpforward.server.a_no_user_package.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author www
 * @date 2015年9月9日
 */

@WebServlet(urlPatterns = { "/testAsync" }, asyncSupported = true)
public class TestAsyncServlet extends HttpServlet {

	private static final long serialVersionUID = -4442818110220744267L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");  
        PrintWriter out = response.getWriter();  
        out.println("进入Servlet的时间：" + new Date() + ".");  
        out.flush();  
        
        String myHead = request.getHeader("myHead");
        String myhead = request.getHeader("myhead");
        String myParam = request.getParameter("myParam");
        String myparam = request.getParameter("myparam");
        out.println("myHead=" + myHead + ", myParam=" + myParam); 
        out.println("myhead=" + myhead + ", myparam=" + myparam); 

        //在子线程中执行业务调用，并由其负责输出响应，主线程退出  
        final AsyncContext ctx = request.startAsync();  
        ctx.setTimeout(200000);  
        new Work(ctx).start();  
        out.println("结束Servlet的时间：" + new Date() + ".");  
        out.flush(); 
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
	
	class Work extends Thread{  
	    private AsyncContext context;  
	      
	    public Work(AsyncContext context){  
	        this.context = context;  
	    }  
	    @Override  
	    public void run() {  
	        try {  
	            Thread.sleep(2000);//让线程休眠2s钟模拟超时操作  
	            PrintWriter wirter = context.getResponse().getWriter();           
	            wirter.write("延迟输出");  
	            wirter.flush();  
	            context.complete();  
	        } catch (InterruptedException e) {  
	              
	        } catch (IOException e) {  
	              
	        }  
	    }  
	}  
}
