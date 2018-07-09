package com.mybatis.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.*;

public class MessageDigest {

//	public static void main(String[] args) {
//		String aa = DigestUtils.md5Hex("aa");
//		System.out.println(aa);
//		String bb = "aa";
//		try {
//			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
//			md.update(bb.getBytes());
//			char hexDigits[] = {
//	                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
//	        };
//            // 使用指定的字节更新摘要
//			md.update(bb.getBytes());
//            // 获得密文
//            byte[] mdb = md.digest();
//            // 把密文转换成十六进制的字符串形式
//            int j = mdb.length;
//            char str[] = new char[j * 2];
//            int k = 0;
//            for (int i = 0; i < j; i++) {
//                byte byte0 = mdb[i];
//                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//                str[k++] = hexDigits[byte0 & 0xf];
//            }
//            System.out.println(str);
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
