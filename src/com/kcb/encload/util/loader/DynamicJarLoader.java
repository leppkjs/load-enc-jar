package com.kcb.encload.util.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;

import com.kcb.encload.Run;
import com.kcb.encload.util.loader.enc.EncryptedClassLoader;

/**
 * JAR를 동적으로 로드한다.
 * 
 * @author O117012
 */
public class DynamicJarLoader {
	
	/**
	 * jar 경로
	 */
    private String jarPath;
    
    /**
     * jar가 적제한 클래스로더저장소
     */
    private Map<String, ClassLoader> loaderMap = new HashMap<String, ClassLoader>();
    
    /**
     * jar 경로를 정재한다.
     * 
     * @param jarPath jar경로
     */
    public DynamicJarLoader(String jarPath){
        this.jarPath = jarPath;

        this.jarPath.replaceAll("\\\\", "/");

        if(this.jarPath.endsWith("/") == false) {
        	this.jarPath = this.jarPath + "/";
        }

    }

    /**
     * jar 파일을 로드한다.
     * 
     * @param jarFileName 로드할 jar 파일 이름
     * @param encKey 암/복호화 대칭키
     * @return 적재여부
     * @throws Throwable
     */
    public boolean load(String jarFileName, byte[] encKey) throws Throwable{
        if(loaderMap.containsKey(jarFileName)) {
        	//TODO 이미 적재되어 있다면 unload하고 적재할지 적재완료 여부만 반환할지...?
        	//unload(jarFileName);
        	return true;
        } 

        String jarFilePath = jarPath + jarFileName;
        File jarFile = new File(jarFilePath);
        String ENC_TYPE = "AES";
        try {
            InputStream resource = new FileInputStream(jarFile);
            
    		Key secretKey = new SecretKeySpec(encKey, ENC_TYPE);
            Cipher cipher = Cipher.getInstance(ENC_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
    		
            JarInputStream jarInputStream = new JarInputStream(new CipherInputStream(resource, cipher));
            ClassLoader classLoader = new EncryptedClassLoader(Run.class.getClassLoader(), jarInputStream, true);
            
            loaderMap.put(jarFileName, classLoader);
            
            return true;
        } catch (MalformedURLException e) {
        	e.printStackTrace();
            return false;
        }

    }

    /**
     * 클래스로더로 부터 인스턴스를 생성해서 반환한다.
     * 
     * @param jarFileName jar파일이름
     * @param className 클래스명
     * @return 클래스객체
     * @throws ReflectiveOperationException
     */
    public Object newInstance(String jarFileName, String className) throws ReflectiveOperationException{
    	ClassLoader loader = loaderMap.get(jarFileName);

        if(loader == null) 
        	return true;

        try {
            Class<?> clazz = loader.loadClass(className);
            return clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        	throw e;
        }

    }

    /**
     * 클래스로더목록으로 부터 클래스 객체를 생성하여 반환한다.
     * @param className
     * @return
     * @throws ReflectiveOperationException
     */
    public Object newInstance(String className) throws ReflectiveOperationException{
        for(String each : loaderMap.keySet()){
            Object object = newInstance(each, className);
            if(object != null) 
            	return object;
        }

        return null;

    }

}
