package com.kcb.encload.util.loader;

import java.util.HashMap;
import java.util.Map;

/**
 * JAR를 동적으로 로드한다.
 * 
 * @author O117012
 */
public class DynamicJarLoader {
    /**
     * jar가 적제한 클래스로더저장소
     */
    private Map<String, ClassLoader> loaderMap = new HashMap<String, ClassLoader>();
    
    /**
     * 기본 생성자이다.
     */
    public DynamicJarLoader(){
    }

    /**
     * jar 파일을 로드한다.
     * 
     * @param jarFileName 로드할 jar 파일 이름
     * @param classLoader 클래스 로더
     * @return 적재여부
     * @throws Throwable
     */
    public boolean load(String jarFileName, ClassLoader classLoader) throws Throwable{
        if(loaderMap.containsKey(jarFileName)) {
        	//TODO 이미 적재되어 있다면 unload하고 적재할지 적재완료 여부만 반환할지...?
        	//unload(jarFileName);
        	return true;
        } 
        
        try {
        	loaderMap.put(jarFileName, classLoader);
        	return true;
        } catch (Throwable th) {
        	th.printStackTrace();
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
