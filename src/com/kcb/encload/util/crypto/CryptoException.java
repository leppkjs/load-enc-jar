package com.kcb.encload.util.crypto;

/**
 * 암호화 예외처리
 * 
 * @author O117012
 */
public class CryptoException extends RuntimeException {
	 
	private static final long serialVersionUID = 5324823951707119434L;

	/**
	 * 생성자 이다.
	 */
	public CryptoException() {}
	
	/**
	 * 암호화 예외처리이다.
	 * 
	 * @param message 오류메시지
	 * @param cause 원인 예외
	 */
    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }
}
