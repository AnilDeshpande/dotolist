package com.codetutor.dotolist.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class Author {
	
	private static long authorCount=0;
	
	private long authorId;
	private String authorName;
	private String authorEmailId;
	private String authorPassword;
	
	public Author() {
		super();
	}
	
	public Author(Author author) {
		this(author.authorName, author.authorEmailId, author.authorEmailId);
	}
	
	public Author(String authorName, String authorEmailId, String authorPassword) {
		super();
		this.authorId = ++authorCount;
		this.authorName = authorName;
		this.authorEmailId = authorEmailId;
		this.authorPassword = authorPassword;
	}
	
	public long getAuthorId() {
		return authorId;
	}
	
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorEmailId() {
		return authorEmailId;
	}
	public void setAuthorEmailId(String authorEmailId) {
		this.authorEmailId = authorEmailId;
	}
	
	public void setAuthorPassword(String authorPassword) {
		this.authorPassword = authorPassword;
	}
	
	

}
