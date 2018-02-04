package com.codetutor.dotolist.exceptions;

public class AuthenticationFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long errorCode;

	public AuthenticationFailedException() {
		super();
	}
	public AuthenticationFailedException(String errorMessage) {
		super(errorMessage);
	}
	
	public AuthenticationFailedException(long errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode=errorCode;
	}
}
