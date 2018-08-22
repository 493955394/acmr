package com.acmr.helper.util;

import java.security.SecureRandom;

public class SecureRandomUtil {
	public static String getSecureRandomNum(int num) {
		SecureRandom sr = new SecureRandom();
		String result = "";
		for (int i = 0; i < num; i++) {
			int n = sr.nextInt(10);
			result += n + "";
		}
		return result;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(getSecureRandomNum(15));
		}
	}
}
