package com.wb.httpforward.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/**
 * @author www
 * @date 2015年9月13日
 */

public class Base64Util {

	/**
     * 将二进制数据编码为BASE64字符串
     * @param binaryData
     * @return
     */
	public static String encode(byte[] binaryData) {
		if (binaryData == null) {
			return null;
		}
		try {
			return new String(Base64.encodeBase64(binaryData), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
     * 将BASE64字符串恢复为二进制数据
     * @param base64String
     * @return
     */
	public static byte[] decode(String base64String) {
		if (base64String == null) {
			return null;
		}
		try {
			return Base64.decodeBase64(base64String.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}
