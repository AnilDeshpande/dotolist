package com.codetutor.dotolist.exceptions;

public class AuthorNotRegisredException extends Exception {
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
