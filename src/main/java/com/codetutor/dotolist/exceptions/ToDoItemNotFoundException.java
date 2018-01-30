package com.codetutor.dotolist.exceptions;

public class ToDoItemNotFoundException extends Exception {

	public long errorCode;
	

	public ToDoItemNotFoundException() {
		super();
	}
	public ToDoItemNotFoundException(String errorMessage) {
		super(errorMessage);
	}
	
	public ToDoItemNotFoundException(long errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode=errorCode;
	}

}
