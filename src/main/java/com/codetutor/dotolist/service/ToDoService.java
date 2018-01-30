package com.codetutor.dotolist.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codetutor.dotolist.model.Author;
import com.codetutor.dotolist.model.Status;
import com.codetutor.dotolist.model.ToDoItem;
import com.codetutor.dotolist.model.ToDoList;

import db.InMemoryDB;

public class ToDoService {
	
	Map<String, Author> registeredAuthors;
	
	Map<String, ToDoList> toDoListOfAuthors;
	
	private  static ToDoService toDoServiceInstance;

	private ToDoService() {
		registeredAuthors = new HashMap<String, Author>();
		toDoListOfAuthors = new HashMap<String, ToDoList>();
	}
	
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
			author=new Author(InMemoryDB.getAuthorId(), authorName, authorEmailId, authorPassword);
			registeredAuthors.put(authorEmailId, author);
		}
		return author;
	}
	
	public Author registerAuthor(Author author) {
		Author createdAuthor=null;
		if(registeredAuthors.containsKey(author.getAuthorEmailId())) {
			createdAuthor=registeredAuthors.get(author.getAuthorEmailId());
		}else {
			author.setAuthorId(InMemoryDB.getAuthorId());
			createdAuthor=new Author(author);
			registeredAuthors.put(author.getAuthorEmailId(), createdAuthor);
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

	
	public List<ToDoItem> addToDoItem(String emailId, String toDoItem, String place) {
		List<ToDoItem> todoList = null;
		if(isRegisteredAuthor(emailId)){
			if(doesToDoListExists(emailId)) {
				todoList = toDoListOfAuthors.get(emailId).getDoItems();
			}else {
				Author author = registeredAuthors.get(emailId);
				List<ToDoItem> doItems = new ArrayList<ToDoItem>();
				ToDoItem doItem = new ToDoItem(InMemoryDB.getToDoId(), toDoItem, emailId, place);
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
				toDoItem.setId(InMemoryDB.getToDoId());
				newluCreatedToDItem=new ToDoItem(toDoItem);
				todoList.add(new ToDoItem(newluCreatedToDItem));
			}else {
				Author author = registeredAuthors.get(toDoItem.getAuthorEmailId());
				List<ToDoItem> doItems = new ArrayList<ToDoItem>();
				newluCreatedToDItem = new ToDoItem(InMemoryDB.getToDoId(), toDoItem.getTodoString(), toDoItem.getAuthorEmailId(), toDoItem.getPlace());
				doItems.add(newluCreatedToDItem);
				ToDoList doList = new ToDoList(author,doItems);
				toDoListOfAuthors.put(toDoItem.getAuthorEmailId(), doList);
		
			}
		}
		return newluCreatedToDItem;
	}

	public ToDoItem getToDo(String authorEmailId, long toDoId) {
		List<ToDoItem> toDoList= toDoListOfAuthors.get(authorEmailId).getDoItems();
		ToDoItem doItem = null;
		if(toDoList!=null) {
			for(ToDoItem item: toDoList) {
				if(item.getId() == toDoId) {
					doItem = item;
				}
			}
		}
		return doItem;
	}
	
	public Status deleteToDoItem(ToDoItem doItem) {
		Status status = new Status();
		List<ToDoItem> toDoList = toDoListOfAuthors.get(doItem.getAuthorEmailId()).getDoItems();
		if(!toDoList.contains(doItem)) {
			status = new Status(204, "Could not find entry to delete");
		}else {
			toDoList.remove(doItem);
			toDoListOfAuthors.get(doItem.getAuthorEmailId()).setDoItems(toDoList);
			status = new Status(200,"Successfully deleted");
		}
		return status;
	}
	
	public ToDoItem updateToDoItem(ToDoItem currentToDoItem, String todoString) {
		ToDoItem doItem = null;
		List<ToDoItem> toDoList = toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems();
		if(toDoList.contains(currentToDoItem)) {
			int index = toDoList.indexOf(currentToDoItem);
			toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index).setTodoString(todoString);
			doItem = toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index);
		}else{
			doItem = currentToDoItem;
		}
		return doItem;
	}
	
	public ToDoItem updateToDoItem(ToDoItem currentToDoItem, ToDoItem proposedToDoItem) {
		ToDoItem doItem = null;
		List<ToDoItem> toDoList = toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems();
		if(toDoList.contains(currentToDoItem)) {
			int index = toDoList.indexOf(currentToDoItem);
			toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index).setTodoString(proposedToDoItem.getTodoString());
			toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index).setPlace(proposedToDoItem.getPlace());
			toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index).setDate(proposedToDoItem.getDate());
			doItem = toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index);
		}else{
			doItem = currentToDoItem;
		}
		return doItem;
	}
	
	
	public Status isAuthorAutheticated(Author author) {
		Status status=null;
		Author author2 = registeredAuthors.get(author.getAuthorEmailId());
		System.out.println();
		if(author2!=null) {
			if(author.equals(author2)) {
				status = new Status(200,"Authentication Succeded");
			}else {
				status = new Status(204,"Authentication failed");
			}
		}else {
			status=new Status(204, "Authentication Filed");
		}
		
		return status;
	}
	
	private void serializeRegisteredAuthors() {
		System.out.println("serializeRegisteredAuthors ");
		
		try {
			File file=new File("authors.txt");
			file.createNewFile();
			FileOutputStream fileOutputStream=new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(registeredAuthors);
			objectOutputStream.flush();
			objectOutputStream.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void serializeToDos() {
		System.out.println("serializeToDos ");
		try {
			File file=new File("todos.txt");
			file.createNewFile();
			FileOutputStream fileOutputStream=new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(toDoListOfAuthors);
			objectOutputStream.flush();
			objectOutputStream.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deserializeToDos() {
		System.out.println("deserializeToDos");
		Map<String, ToDoList> toDoListOfAuthors = new HashMap<String, ToDoList>();
		try {
			FileInputStream fileInputStream=new FileInputStream("todos.txt");
			ObjectInputStream inputStream=new ObjectInputStream(fileInputStream);
			toDoListOfAuthors = (Map<String, ToDoList>)inputStream.readObject();
			inputStream.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		this.toDoListOfAuthors = toDoListOfAuthors;
	}
	
	private void desrializeRegisteredAuthors() {
		System.out.println("desrializeRegisteredAuthors");
		Map<String, Author> registeredAuthors = new HashMap<String, Author>();
		try {
			FileInputStream fileInputStream=new FileInputStream("authors.txt");
			ObjectInputStream inputStream=new ObjectInputStream(fileInputStream);
			registeredAuthors = (Map<String, Author>)inputStream.readObject();
			inputStream.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		this.registeredAuthors = registeredAuthors;
	}
}
