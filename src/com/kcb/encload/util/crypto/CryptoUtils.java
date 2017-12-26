package com.kcb.encload.util.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
 
/**
 * 암호화 처리 유틸리티 클래스이다.
 * 
 * @author O117012
 */
public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    
    private static final String TRANSFORMATION = "AES";
    
    /**
     * 파일을 암호화 한다.
     * 
     * @param key 암복호화 키
     * @param inputFile 대상파일객체
     * @param outputFile 암호화파일객체
     */
    public static void encrypt(String key, File inputFile, File outputFile) {
    	CryptoUtils.doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }
    
    /**
     * 파일을 복호화 한다.
     * 
     * @param key 암복호화 키
     * @param inputFile 암호화파일객체
     * @param outputFile 복호화파일객체
     */
    public static void decrypt(String key, File inputFile, File outputFile) {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }
    
    /**
     * 파일을 암/복호화 한다.
     * 
     * @param cipherMode 암/복호화모드
     * @param key 암/복보화키
     * @param inputFile 대상파일
     * @param outputFile 목적파일
     */
    private static void doCrypto(int cipherMode, String key, File inputFile, File outputFile) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, new SecretKeySpec(key.getBytes(), ALGORITHM));
             
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
             
            byte[] outputBytes = cipher.doFinal(inputBytes);
             
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);
             
            inputStream.close();
            outputStream.close();
             
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
}
