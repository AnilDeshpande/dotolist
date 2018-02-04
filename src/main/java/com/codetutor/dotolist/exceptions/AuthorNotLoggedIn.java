package com.codetutor.dotolist.exceptions;

/**
 * 
 * @author anildeshpande
 * This exception is thrown when any operation is attempted but user has logged in yet
 * Error code is 209 
 */

public class AuthorNotLoggedIn extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long errorCode;

	public AuthorNotLoggedIn() {
		super();
	}
	public AuthorNotLoggedIn(String errorMessage) {
		super(errorMessage);
	}
	
	public AuthorNotLoggedIn(long errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode=errorCode;
	}
}
