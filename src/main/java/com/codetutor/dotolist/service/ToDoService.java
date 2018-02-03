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

import javax.servlet.http.HttpServletRequest;

import com.codetutor.dotolist.exceptions.AuthorHasAlreadyLoggedInException;
import com.codetutor.dotolist.exceptions.AuthorNotRegisredException;
import com.codetutor.dotolist.exceptions.ToDoItemNotFoundException;
import com.codetutor.dotolist.model.Author;
import com.codetutor.dotolist.model.AuthorWithActiveSession;
import com.codetutor.dotolist.model.ToDoAppRestStatus;
import com.codetutor.dotolist.model.ToDoItem;
import com.codetutor.dotolist.model.ToDoList;

import db.InMemoryDB;

public class ToDoService {
	
	Map<String, Author> registeredAuthors;
	Map<String, ToDoList> toDoListOfAuthors;
	
	Map<String, String> activeSessions; 
	
	private  static ToDoService toDoServiceInstance;

	private ToDoService() {
		registeredAuthors = new HashMap<String, Author>();
		toDoListOfAuthors = new HashMap<String, ToDoList>();
		activeSessions = new HashMap<String, String>();
	}
	
	public void loginUser(String emailId, String sessionId) {
		activeSessions.put(emailId, sessionId);
	}
	
	public void endToDoAuthorSession(String emailId) {
		activeSessions.remove(emailId);
	}
	
	public boolean isUserLoggedIn(String emailId, String sessionId) {
		boolean isUserLoggedIn=false;
		if(activeSessions.containsKey(emailId)) {
			if(activeSessions.get(emailId).equals(sessionId)) {
				isUserLoggedIn = true;
			}
		}
		return isUserLoggedIn;
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
	
	public AuthorWithActiveSession login(Author author, HttpServletRequest httpServletRequest) throws AuthorNotRegisredException{
		AuthorWithActiveSession authorWithActiveSession = null;
		if(isRegisteredAuthor(author.getAuthorEmailId())) {
			if(!isAuthorPresentInActiveSessions(author)) {
				authorWithActiveSession= new AuthorWithActiveSession();
				authorWithActiveSession.setToken(httpServletRequest.getSession(true).getId());
				activeSessions.put(author.getAuthorEmailId(), authorWithActiveSession.getToken());
			}else {
				authorWithActiveSession = new AuthorWithActiveSession();
				authorWithActiveSession.setToken(activeSessions.get(author.getAuthorEmailId()));
			}
		}else {
			throw new AuthorNotRegisredException(403,"Registration is required");
		}
		
		return authorWithActiveSession;
	}
	
	public boolean signOut(Author author) {
		boolean isSignoutSuccessful = false;
		if(activeSessions!=null && activeSessions.get(author.getAuthorEmailId())!=null) {
			activeSessions.remove(author.getAuthorEmailId());
			isSignoutSuccessful=true;
		}
		return isSignoutSuccessful;
	}
	
	private boolean isAuthorPresentInActiveSessions(Author author) {
		if(activeSessions.get(author.getAuthorEmailId())!=null) {
			return true;
		}else {
			return false;
		}
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
	
	public ToDoItem addToDoItem(ToDoItem toDoItem) throws AuthorNotRegisredException{
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
		}else {
			throw new AuthorNotRegisredException(403,"Author is not registred, can't add todo item"); 
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
	
	public ToDoAppRestStatus deleteToDoItem(ToDoItem doItem) throws ToDoItemNotFoundException{
		ToDoAppRestStatus status = new ToDoAppRestStatus();
		List<ToDoItem> toDoList = toDoListOfAuthors.get(doItem.getAuthorEmailId()).getDoItems();
		if(toDoList!=null) {
			if(!toDoList.contains(doItem)) {
				throw new ToDoItemNotFoundException(404,"ToDoItems not found");
			}else {
				toDoList.remove(doItem);
				toDoListOfAuthors.get(doItem.getAuthorEmailId()).setDoItems(toDoList);
				status = new ToDoAppRestStatus(204,"Successfully deleted");
			}
		}else {
			throw new ToDoItemNotFoundException(404,"ToDoItems not found");
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
	
	public ToDoItem updateToDoItem(ToDoItem currentToDoItem, ToDoItem proposedToDoItem) throws ToDoItemNotFoundException{
		ToDoItem doItem = null;
		List<ToDoItem> toDoList = toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems();
		if(toDoList.contains(currentToDoItem)) {
			int index = toDoList.indexOf(currentToDoItem);
			toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index).setTodoString(proposedToDoItem.getTodoString());
			toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index).setPlace(proposedToDoItem.getPlace());
			toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index).setDate(proposedToDoItem.getDate());
			doItem = toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index);
		}else{
			throw new ToDoItemNotFoundException(404,"ToDoItem for current author not found");
		}
		return doItem;
	}
	
	
	public ToDoAppRestStatus isAuthorAutheticated(Author author) {
		ToDoAppRestStatus status=null;
		Author author2 = registeredAuthors.get(author.getAuthorEmailId());
		System.out.println();
		if(author2!=null) {
			if(author.equals(author2)) {
				status = new ToDoAppRestStatus(200,"Authentication Succeded");
			}else {
				status = new ToDoAppRestStatus(404,"User is not registered");
			}
		}else {
			status=new ToDoAppRestStatus(404, "User is not registered");
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
