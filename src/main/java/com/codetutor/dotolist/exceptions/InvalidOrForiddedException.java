package com.codetutor.dotolist.exceptions;

public class InvalidOrForiddedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public long errorCode;
	

	public InvalidOrForiddedException() {
		super();
	}
	public InvalidOrForiddedException(String errorMessage) {
		super(errorMessage);
	}
	
	public InvalidOrForiddedException(long errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode=errorCode;
	}

}
