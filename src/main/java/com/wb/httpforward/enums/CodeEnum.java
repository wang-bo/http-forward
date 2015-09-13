package com.wb.httpforward.enums;

/**
 * @author www
 * @date 2015年9月10日
 */

public enum CodeEnum {
	
	ERROR(-1),
	
	TIMEOUT(0),
	
	SUCCESS(1);

	public final int val;
	
	private CodeEnum(int val) {
		this.val = val;
	}
}
