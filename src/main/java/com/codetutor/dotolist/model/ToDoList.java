package com.codetutor.dotolist.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ToDoList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -376481912229114740L;
	private Author author;
	private List<ToDoItem> doItems;
	
	public ToDoList() {
		super();
	}
	
	public ToDoList(Author author, List<ToDoItem> doItems) {
		super();
		this.author = author;
		this.doItems = doItems;
	}
	
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public List<ToDoItem> getDoItems() {
		return doItems;
	}
	public void setDoItems(List<ToDoItem> doItems) {
		this.doItems = doItems;
	}
}
