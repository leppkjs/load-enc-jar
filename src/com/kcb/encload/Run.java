package com.kcb.encload;

import com.kcb.encload.util.loader.DynamicJarLoader;

import api.AppApi;

/**
 * 암호화 jar파일 동적 적재 테스트용
 * 
 * @author O117012
 */
public class Run {
	/**
	 * 암호화키
	 */
	private static final String ENC_KEY = "!com.kcb.encKey!";
	/**
	 * 암호화 JAR 폴더
	 */
	private static final String ENC_LIB_PATH = "enclibs";
	
	/**
	 * 암호화 jar 파일을 부트스트랩 한다.
	 */
	public void bootstrap(){
		DynamicJarLoader loader;
		boolean isLoaded;
		String target = "enc-jar.jar";
		AppApi imp;
		
		try {
			loader = new DynamicJarLoader(ENC_LIB_PATH);
			isLoaded = loader.load(target, ENC_KEY.getBytes());
			
			System.out.println("isLoaded :" + isLoaded);
			
			imp = (AppApi)loader.newInstance("app.AppApiImpl");
			System.out.println(imp.getResourceInfo());
			
		} catch (Throwable th) {
			th.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		new Run().bootstrap();
	}
	
}
