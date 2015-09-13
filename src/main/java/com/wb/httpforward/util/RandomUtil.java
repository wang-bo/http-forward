package com.wb.httpforward.util;

import java.util.Random;

/**
 * @author www
 * @date 2015年9月9日
 */

public class RandomUtil {

	public static Random random = new Random();
	
	public static int nextInt(int n) {
		return random.nextInt(n);
	}
}
