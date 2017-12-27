package com.kcb.encload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;

import com.kcb.encload.util.loader.DynamicJarLoader;
import com.kcb.encload.util.loader.enc.EncryptedClassLoader;

import api.AppApi;

/**
 * 암호화 jar파일 동적 적재 테스트용
 * 
 * @author O117012
 */
public class RunSpring {
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
	public void bootstrap() {
		DynamicJarLoader loader;
		boolean isLoaded;
		String target = "enc-jar.jar";
		String ENC_TYPE = "AES";
		AppApi imp;

		try {
			loader = new DynamicJarLoader();

			InputStream resource = new FileInputStream(new File(correction(ENC_LIB_PATH) + target));
			Cipher cipher = Cipher.getInstance(ENC_TYPE);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(ENC_KEY.getBytes(), ENC_TYPE));

			isLoaded = loader.load(target, new EncryptedClassLoader(RunSpring.class.getClassLoader(),
					new JarInputStream(new CipherInputStream(resource, cipher)), true));

			System.out.println("isLoaded :" + isLoaded);

			imp = (AppApi) loader.newInstance("app.AppApiImpl");
			System.out.println(imp.getResourceInfo());

		} catch (Throwable th) {
			th.printStackTrace();
		}

	}

	/**
	 * 주어진 경로를 정재해서 반환한다.
	 * 
	 * @param jarPath
	 *            jar 패스
	 * @return 수정된 패스
	 */
	private String correction(String jarPath) {
		jarPath.replaceAll("\\\\", "/");

		if (jarPath.endsWith("/") == false) {
			jarPath = jarPath + "/";
		}

		return jarPath;
	}

	public static void main(String[] args2) {
		// new RunSpring().bootstrap();
		springBootstrap();
	}
	
	
	public static void springBootstrap() {
		//URL classURL = new URL("jar:" + new File("D:\\INNO-Studio\\WorkSpaces\\sample\\load-enc-jar\\libs\\springboot.jar").toURI().toURL() + "!/");
		//ClassLoader classLoader = new URLClassLoader(new URL[] { classURL }, Thread.currentThread().getContextClassLoader());

		/*
		try {
			Class<?> mainClass = classLoader.loadClass("org.springframework.boot.loader.JarLauncher");
			mainClass.getMethod("main", String[].class).invoke(null, (Object)args);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		*/
		
		String jarFile = "file:/D:/INNO-Studio/WorkSpaces/sample/load-enc-jar/libs/springboot.jar";

		URL jarurl;
		try {
			jarurl = new URL(jarFile);
			URL[] urls = { jarurl };
			URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
		
			Class<?> main;
			main = Class.forName("org.springframework.boot.loader.JarLauncher", true, classLoader);
			Object obj = main.newInstance();
			Method mainMethod = main.getDeclaredMethod("main", new Class[] { String[].class });
			String[] args = new String[0];
			mainMethod.invoke(obj, new Object[] { args });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		
	}

}
