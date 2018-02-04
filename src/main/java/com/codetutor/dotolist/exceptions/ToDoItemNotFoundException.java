package com.codetutor.dotolist.exceptions;

/**
 * 
 * @author anildeshpande
 *	This exception is thrown when a ToDo Item is not found against an author while deleting 
 * or updating
 */
public class ToDoItemNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
