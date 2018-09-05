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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.codetutor.dotolist.exceptions.AuthenticationFailedException;
import com.codetutor.dotolist.exceptions.AuthorHasAlreadyLoggedInException;
import com.codetutor.dotolist.exceptions.AuthorNotLoggedIn;
import com.codetutor.dotolist.exceptions.AuthorNotRegisredException;
import com.codetutor.dotolist.exceptions.InvalidOrForiddedException;
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
	
	private Author getAuthorBasedOnSessionId(String token) throws InvalidOrForiddedException{
		Author author = null;
		Set<String> emailIds = activeSessions.keySet();
		for(String emailId: emailIds) {
			if(activeSessions.containsKey(emailId)) {
				author = registeredAuthors.get(emailId);
				break;
			}
		}
		if(author==null) {
			throw new InvalidOrForiddedException(403, "Invalid session or forbidden from doing this operation");
		}
		return author;
	}
	
	private ToDoItem getToDoItemForAuthor(Author author, long toDoId) throws ToDoItemNotFoundException{
		List<ToDoItem> toDoList = toDoListOfAuthors.get(author.getAuthorEmailId()).getDoItems();
		ToDoItem toDoItem=null;
		if(toDoList!=null) {
			for(ToDoItem item: toDoList) {
				if(item.getId()==toDoId) {
					toDoItem = item;
					break;
				}
			}
			if (toDoItem==null) {
				throw new ToDoItemNotFoundException(404,"ToDoItems not found");
			}
		}else {
			throw new ToDoItemNotFoundException(404,"ToDoItems not found");
		}
		
		return toDoItem;
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
	
	public AuthorWithActiveSession login(Author author, HttpServletRequest httpServletRequest) throws AuthorNotRegisredException, AuthenticationFailedException{
		AuthorWithActiveSession authorWithActiveSession = null;
		if(authenticateAuthor(author)) {
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
	
	public List<ToDoItem> getMessages(String authorEmailId, String token) throws ToDoItemNotFoundException, AuthorNotLoggedIn, InvalidOrForiddedException{
		ToDoList toDoList = toDoListOfAuthors.get(authorEmailId);
		if(toDoList==null) {
			throw new ToDoItemNotFoundException(404,"ToDoItems Not found");
		}else {
			if(activeSessions.get(authorEmailId)!=null) {
				if(activeSessions.get(authorEmailId).equals(token)) {
					return toDoList.getDoItems();
				}else {
					throw new InvalidOrForiddedException(403,"Forbidden from this operation");
				}
			}else {
				throw new AuthorNotLoggedIn(209, "Author not logged in Excetion");
			}
			
		}
	}

	
	public List<ToDoItem> addToDoItem(String emailId, String toDoItem, String place, String token) throws InvalidOrForiddedException{
		List<ToDoItem> todoList = null;
		if(activeSessions.get(emailId)!=null && activeSessions.get(emailId).equals(token)) {
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
		}else {
			throw new InvalidOrForiddedException(403, "Invalid session or forbidden from doing this operation");
		}
		
		return todoList;
	}
	
	public ToDoItem addToDoItem(ToDoItem toDoItem, String token) throws AuthorNotRegisredException, InvalidOrForiddedException{
		List<ToDoItem> todoList = null;
		ToDoItem newluCreatedToDItem = null;
		if(activeSessions.get(toDoItem.getAuthorEmailId())!=null && activeSessions.get(toDoItem.getAuthorEmailId()).equals(token)) {
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
		}else {
			throw new InvalidOrForiddedException(403, "Invalid session or forbidden from doing this operation");
		}
		
		return newluCreatedToDItem;
	}

	public ToDoItem getToDo(String authorEmailId, long toDoId, String token) throws InvalidOrForiddedException{
		ToDoItem doItem = null;
		if(activeSessions.get(authorEmailId)!=null && activeSessions.get(authorEmailId).equals(token)) {
			List<ToDoItem> toDoList= toDoListOfAuthors.get(authorEmailId).getDoItems();
			if(toDoList!=null) {
				for(ToDoItem item: toDoList) {
					if(item.getId() == toDoId) {
						doItem = item;
					}
				}
			}
		}else {
			throw new InvalidOrForiddedException(403, "Invalid session or forbidden from doing this operation");
		}
		
		return doItem;
	}
	
	public ToDoAppRestStatus deleteToDoItem(ToDoItem doItem, String token) throws ToDoItemNotFoundException, InvalidOrForiddedException{
		ToDoAppRestStatus status = new ToDoAppRestStatus();
		if(activeSessions.get(doItem.getAuthorEmailId())!=null && activeSessions.get(doItem.getAuthorEmailId()).equals(token)) {
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
		}else {
			throw new InvalidOrForiddedException(403, "Invalid session or forbidden from doing this operation");
		}
		
		
		return status;
	}
	
	
	
	public ToDoItem updateToDoItem(ToDoItem currentToDoItem, String todoString, String token) throws InvalidOrForiddedException{
		ToDoItem doItem = null;
		if(activeSessions.get(currentToDoItem.getAuthorEmailId())!=null && activeSessions.get(currentToDoItem.getAuthorEmailId()).equals(token)) {
			List<ToDoItem> toDoList = toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems();
			if(toDoList.contains(currentToDoItem)) {
				int index = toDoList.indexOf(currentToDoItem);
				toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index).setTodoString(todoString);
				doItem = toDoListOfAuthors.get(currentToDoItem.getAuthorEmailId()).getDoItems().get(index);
			}else{
				doItem = currentToDoItem;
			}
		}else {
			throw new InvalidOrForiddedException(403, "Invalid session or forbidden from doing this operation");
		}
		
		return doItem;
	}
	
	public ToDoItem updateToDoItem(ToDoItem currentToDoItem, ToDoItem proposedToDoItem, String token) throws ToDoItemNotFoundException,InvalidOrForiddedException {
		ToDoItem doItem = null;
		if(activeSessions.get(currentToDoItem.getAuthorEmailId())!=null && activeSessions.get(currentToDoItem.getAuthorEmailId()).equals(token)) {
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
		}else {
			throw new InvalidOrForiddedException(403, "Invalid session or forbidden from doing this operation");
		}
		
		return doItem;
	}
	
	private boolean authenticateAuthor(Author author) throws AuthorNotRegisredException, AuthenticationFailedException{
		boolean isAuthorAuthenticated=false;
		
		Author author2 = registeredAuthors.get(author.getAuthorEmailId());
		if(author2!=null) {
			if(author.equals(author2)) {
				isAuthorAuthenticated=true;
			}else {
				throw new AuthenticationFailedException(403,"Authetication Failed");
			}
		}else {
			throw new AuthorNotRegisredException(404,"User not registered");
		}
		
		return isAuthorAuthenticated;
		
	}
	
	public ToDoAppRestStatus isAuthorAutheticated(Author author) {
		ToDoAppRestStatus status=null;
		Author author2 = registeredAuthors.get(author.getAuthorEmailId());
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

	public ToDoAppRestStatus deleteToDoItem(long toDoId, String token) throws InvalidOrForiddedException, ToDoItemNotFoundException{
		ToDoAppRestStatus status = new ToDoAppRestStatus();
		Author author = getAuthorBasedOnSessionId(token);
		ToDoItem toBeDeleted = getToDoItemForAuthor(author, toDoId);
		
		List<ToDoItem> toDoList = toDoListOfAuthors.get(toBeDeleted.getAuthorEmailId()).getDoItems();
		if(toBeDeleted!=null) {
			toDoList.remove(toBeDeleted);
			toDoListOfAuthors.get(toBeDeleted.getAuthorEmailId()).setDoItems(toDoList);
			status = new ToDoAppRestStatus(204,"Successfully deleted");
		}
		return status;
	}
	
	/*private void serializeRegisteredAuthors() {
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
	}*/
}
