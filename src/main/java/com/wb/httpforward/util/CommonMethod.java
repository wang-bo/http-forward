package com.wb.httpforward.util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.wb.httpforward.constant.ServerConstant;

/**
 * @author www
 * @date 2015年9月9日
 */

public class CommonMethod {
	
	private static Set<String> CORRECT_CLIENT_NO_SET = new HashSet<String>();
	
	static {
		for (String clientNo: ServerConstant.CLIENT_NO_SET.split(",")) {
			CORRECT_CLIENT_NO_SET.add(clientNo.trim());
		}
	}
	
	private static AtomicInteger code = new AtomicInteger(ServerConstant.CODE_START_VALUE);

	/**
	 * 判断clientNo是否正确
	 * @param clientNo
	 * @return
	 */
	public static boolean isCorrectClientNo(String clientNo) {
		return CORRECT_CLIENT_NO_SET.contains(clientNo);
	}
	
	/**
	 * 线程安全的生成数据包编号
	 * @return
	 */
	public static Integer codeGenerator() {
		return code.addAndGet(1);
	}
	
	public static void main(String[] args) {
		System.out.println(isCorrectClientNo(null));
		System.out.println(isCorrectClientNo("other"));
		System.out.println(isCorrectClientNo("123"));
	}
}
