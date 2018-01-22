package com.codetutor.dotolist.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codetutor.dotolist.model.Author;
import com.codetutor.dotolist.model.ToDoItem;
import com.codetutor.dotolist.model.ToDoList;

public class ToDoService {
	
	Map<String, Author> registeredAuthors = new HashMap<String, Author>();
	
	Map<String, ToDoList> toDoListOfAuthors = new HashMap<String, ToDoList>();
	
	private  static ToDoService toDoServiceInstance;
	
	public static ToDoService getInstance() {
		if(toDoServiceInstance==null) {
			toDoServiceInstance = new ToDoService();
		}
		return toDoServiceInstance;
	}
	
	private boolean isRegisteredAuthor(String authorEmailId) {
		boolean isRegisteredUser= false;
		if(registeredAuthors!=null) {
			if(registeredAuthors.containsKey(authorEmailId)) {
				isRegisteredUser =  true;
			}
		}
		return isRegisteredUser;
	}
	
	private boolean doesToDoListExists(String emailId) {
		boolean doesToDoEntryExists = false;
		if(isRegisteredAuthor(emailId)) {
			if(toDoListOfAuthors!=null) {
				if(toDoListOfAuthors.containsKey(emailId)) {
					doesToDoEntryExists = true;
				}
			}
		}
		return doesToDoEntryExists;
	}
	
	
	
	public Author registerAuthor(String authorName, String authorEmailId, String authorPassword){
		Author author=null;
		if(registeredAuthors.containsKey(authorEmailId)) {
			return author;
		}else {
			author=new Author(authorName, authorEmailId, authorPassword);
			registeredAuthors.put(authorEmailId, author);
		}
		return author;
	}
	
	public Author registerAuthor(Author author) {
		Author createdAuthor=null;
		if(registeredAuthors.containsKey(author.getAuthorEmailId())) {
			createdAuthor=author;
		}else {
			createdAuthor=new Author(author);
			registeredAuthors.put(author.getAuthorEmailId(), author);
		}
		return createdAuthor;
		
	}
	
	public List<ToDoItem> getMessages(String authorEmailId){
		ToDoList toDoList = toDoListOfAuthors.get(authorEmailId);
		if(toDoList==null) {
			return null;
		}else {
			return toDoList.getDoItems();
		}
	}

	
	public List<ToDoItem> addToDoItem(String emailId, String toDoItem) {
		List<ToDoItem> todoList = null;
		if(isRegisteredAuthor(emailId)){
			if(doesToDoListExists(emailId)) {
				todoList = toDoListOfAuthors.get(emailId).getDoItems();
			}else {
				Author author = registeredAuthors.get(emailId);
				List<ToDoItem> doItems = new ArrayList<ToDoItem>();
				ToDoItem doItem = new ToDoItem(toDoItem, emailId);
				doItems.add(doItem);
				ToDoList doList = new ToDoList(author,doItems);
				toDoListOfAuthors.put(emailId, doList);
				
				todoList = toDoListOfAuthors.get(emailId).getDoItems();
			}
		}
		return todoList;
	}
	
	public List<ToDoItem> addToDoItem(ToDoItem doItem){
		List<ToDoItem> todoList = null;
		if(isRegisteredAuthor(doItem.getAuthorEmailId())){
			if(doesToDoListExists(doItem.getAuthorEmailId())) {
				todoList = toDoListOfAuthors.get(doItem.getAuthorEmailId()).getDoItems();
			}else {
				Author author = registeredAuthors.get(doItem.getAuthorEmailId());
				List<ToDoItem> doItems = new ArrayList<ToDoItem>();
				ToDoItem newToDoItem = new ToDoItem(doItem.getTodoString(), doItem.getAuthorEmailId());
				doItems.add(newToDoItem);
				ToDoList doList = new ToDoList(author,doItems);
				toDoListOfAuthors.put(doItem.getAuthorEmailId(), doList);
				
				todoList = toDoListOfAuthors.get(doItem.getAuthorEmailId()).getDoItems();
			}
		}
		return todoList;
	}
	

}
