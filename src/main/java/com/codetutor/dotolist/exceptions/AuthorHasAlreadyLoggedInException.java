package com.codetutor.dotolist.exceptions;

public class AuthorHasAlreadyLoggedInException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long errorCode;

	public AuthorHasAlreadyLoggedInException() {
		super();
	}
	public AuthorHasAlreadyLoggedInException(String errorMessage) {
		super(errorMessage);
	}
	
	public AuthorHasAlreadyLoggedInException(long errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode=errorCode;
	}
}
