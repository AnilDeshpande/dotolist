package com.codetutor.dotolist.model;

public class Status {

	private long statusCode;
	private String message;
	
	public Status() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public Status(long statusCode, String message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setStatusCode(long statusCode) {
		this.statusCode = statusCode;
	}
	
	public long getStatusCode() {
		return statusCode;
	}
	
}
