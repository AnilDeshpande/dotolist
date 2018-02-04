package com.codetutor.dotolist.exceptions;

/**
 * 
 * @author anildeshpande
 * This exception is typically thrown when the Author has not
 * yet been registered in the app
 */

public class AuthorNotRegisredException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long errorCode;
	

	public AuthorNotRegisredException() {
		super();
	}
	public AuthorNotRegisredException(String errorMessage) {
		super(errorMessage);
	}
	
	public AuthorNotRegisredException(long errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode=errorCode;
	}
}
