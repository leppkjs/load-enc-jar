package com.kcb.encload.util;

public class Utils {
	public static String getClassName(String fileName) {
		return fileName.substring(0, fileName.length() - 6).replace('/', '.');
	}

}
