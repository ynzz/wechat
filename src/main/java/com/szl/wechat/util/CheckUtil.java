package com.szl.wechat.util;


public class CheckUtil {
	private static final String token = "ynzz1234567";
//	private static final String token = "";
//	private static final String token = "";
	public static boolean checkSignature(String signature, String timestamp, String nonce, String encrypt){
		//sha1加密
		String temp =  SHA1.getSHA1(token, timestamp, nonce, encrypt);
		
		return temp.equals(signature);
	}
}
