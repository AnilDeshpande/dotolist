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
	
	public ToDoItem addToDoItem(ToDoItem toDoItem){
		List<ToDoItem> todoList = null;
		ToDoItem newluCreatedToDItem = null;
		if(isRegisteredAuthor(toDoItem.getAuthorEmailId())){
			if(doesToDoListExists(toDoItem.getAuthorEmailId())) {
				todoList = toDoListOfAuthors.get(toDoItem.getAuthorEmailId()).getDoItems();
				newluCreatedToDItem=new ToDoItem(toDoItem);
				todoList.add(new ToDoItem(newluCreatedToDItem));
			}else {
				Author author = registeredAuthors.get(toDoItem.getAuthorEmailId());
				List<ToDoItem> doItems = new ArrayList<ToDoItem>();
				newluCreatedToDItem = new ToDoItem(toDoItem.getTodoString(), toDoItem.getAuthorEmailId());
				doItems.add(newluCreatedToDItem);
				ToDoList doList = new ToDoList(author,doItems);
				toDoListOfAuthors.put(toDoItem.getAuthorEmailId(), doList);
		
			}
		}
		return newluCreatedToDItem;
	}
	

}
