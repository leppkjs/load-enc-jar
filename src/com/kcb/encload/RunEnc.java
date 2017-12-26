package com.kcb.encload;

import java.io.File;

import com.kcb.encload.util.crypto.CryptoException;
import com.kcb.encload.util.crypto.CryptoUtils;

/**
 * jar 암호화 테스트를 위해서 암호화 처리
 * 
 * @author O117012
 */
public class RunEnc {
	/**
	 * 암호화키
	 */
	private static final String encKey = "!com.kcb.encKey!";
	
	/**
	 * jar 파일을 암호화 해서 enclibs폴더에 생성한다.
	 */
	public static void enc() {
		
		//암호화 대상 jar 파일
        File inputFile = new File("libs/enc-jar.jar");
        //암호화된 jar 파일
        File encryptedFile = new File("enclibs/enc-jar.jar");
        //복호화 테스트용
        File decryptedFile = new File("enclibs/enc-jar_decrypted.jar");
        
        try {
        	//jar 파일 암호화
            CryptoUtils.encrypt(RunEnc.encKey, inputFile, encryptedFile);
            //암호화 jar 파일 복호화
            CryptoUtils.decrypt(RunEnc.encKey, encryptedFile, decryptedFile);
        } catch (CryptoException ex) {
            ex.printStackTrace();
        }
	}
	
	public static void main(String[] args){
		//암호화 처리
		RunEnc.enc();
	}
	
}
