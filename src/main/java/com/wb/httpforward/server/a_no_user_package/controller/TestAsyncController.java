package com.wb.httpforward.server.a_no_user_package.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResult.DeferredResultHandler;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author www
 * @date 2015年9月10日
 */

@Controller
public class TestAsyncController {

	/**
	 * 异步返回
	 * @return
	 */
    @RequestMapping(value="/callable", method=RequestMethod.GET)  
    public @ResponseBody Callable<String> responseBody() {  
        return new Callable<String>() {  
            public String call() throws Exception { 
            	
            	// do something
                Thread.sleep(3000L);  
                
                return "The String ResponseBody";  
            }  
        };  
    } 
    
    /**
     * 异步返回，使用ResponseEntity
     * @return
     */
    @RequestMapping(value="/callable/entity/headers", method=RequestMethod.GET)  
    public Callable<ResponseEntity<String>> responseEntityCustomHeaders() {  
        return new Callable<ResponseEntity<String>>() {  
            public ResponseEntity<String> call() throws Exception {  
  
                // do something
                Thread.sleep(3000L);  
  
                HttpHeaders headers = new HttpHeaders();  
                headers.setContentType(MediaType.TEXT_PLAIN);  
                return new ResponseEntity<String>(  
                        "The String ResponseBody with custom header Content-Type=text/plain", headers, HttpStatus.OK);  
            }  
        };  
    }
    
    /**
     * 页面跳转
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value="/callable/redirect", method=RequestMethod.GET)  
    public Callable<String> uriTemplate(final RedirectAttributes redirectAttrs) {  
  
        return new Callable<String>() {  
            public String call() throws Exception {  
  
                // do something  
                Thread.sleep(3000L);  
  
                redirectAttrs.addAttribute("account", "a123");  // Used as URI template variable  
                redirectAttrs.addAttribute("date", new LocalDate(2011, 12, 31));  // Appended as a query parameter  
                return "redirect:/redirect/{account}";  
            }  
        };  
    }
    
    /**
     * 异常处理
     * @return
     */
    @RequestMapping("/callable/exception")  
    public @ResponseBody Callable<String> exception() {  
  
        return new Callable<String>() {  
            public String call() throws Exception {  
  
                // do something  
                Thread.sleep(2000L);  
  
                throw new IllegalStateException("Sorry!");  
            }  
        };  
    }  
  
    /**
     * 异常处理器
     * @param e
     * @return
     */
    @ExceptionHandler  
    public @ResponseBody String handle(IllegalStateException e) {  
        return "IllegalStateException handled!";  
    } 
    
    @RequestMapping("/callable/webasynctask")  
    public @ResponseBody WebAsyncTask<String> webasynctask() {  
        Callable<String> callable = new Callable<String>() {  
            @Override  
            public String call() throws Exception {  
                Thread.sleep(2000);  
                return "Callable result";  
            }  
        };  
        return new WebAsyncTask<String>(3000, callable);  
    }  
    
    @RequestMapping("/deferredResult")
    public 
    @ResponseBody 
    DeferredResult<String> deferredResult(HttpServletRequest request,
    		HttpServletResponse response) {
    	DeferredResult<String> result = new DeferredResult<String>(10000);
        DeferredResultHandler resultHandler =
        		new DeferredResultHandler() {
					@Override
					public void handleResult(Object result) { // 这个方法为啥没执行，还没研究
						result = result + "789"; 
						System.out.println(result);
					}
        	
        };
        result.setResultHandler(resultHandler);
        myHolder = new MyHolder(response, result);
        result.onCompletion(new Runnable() {
			@Override
			public void run() {
				System.out.println("onCompletion");
				myHolder = null;
			}
        });
        result.onTimeout(new Runnable() {
			@Override
			public void run() {
				System.out.println("onTimeout");
				myHolder = null;
			}
        });
        return result;
    }
    
    @RequestMapping("/deferredResult/setResult")
    public @ResponseBody String setResult() {
        if (myHolder != null) {
        	
        	HttpServletResponse response = myHolder.getResponse();
        	Cookie cookie = new Cookie("aa", "aaa");
        	cookie.setPath("/");
        	response.addCookie(cookie);
        	response.setHeader("async", "async");
        	
        	
        	DeferredResult<String> result = myHolder.getResult();
        	result.setResult("456");
        }
        return "123";
    }
    
    private class MyHolder {
    	
    	HttpServletResponse response;
    	
    	DeferredResult<String> result;

		public MyHolder(HttpServletResponse response, DeferredResult<String> result) {
			super();
			this.response = response;
			this.result = result;
		}

		public HttpServletResponse getResponse() {
			return response;
		}

		public DeferredResult<String> getResult() {
			return result;
		}
    	
    }
    
    MyHolder myHolder = null;
    
    @Scheduled(fixedRate=2000)
    public void processScheduled() {
    	if (myHolder != null) {
    		HttpServletResponse response = myHolder.getResponse();
        	Cookie cookie = new Cookie("aa", "aaa");
        	cookie.setPath("/");
        	response.addCookie(cookie);
        	response.setHeader("async", "async");
        	
        	DeferredResult<String> result = myHolder.getResult();
        	result.setResult("789");
    	}
    }
    
}
