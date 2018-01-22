package com.codetutor.dotolist;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codetutor.dotolist.model.Author;
import com.codetutor.dotolist.model.ToDoItem;
import com.codetutor.dotolist.service.ToDoService;

@Path("todolists")
public class MyToDos {
	
	ToDoService toDoService;
	
	public MyToDos() {
		// TODO Auto-generated constructor stub
		super();
		toDoService = ToDoService.getInstance();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Author registerUser(Author author){
		return toDoService.registerAuthor(author);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<ToDoItem> getToDoItems(ToDoItem doItem){
		return toDoService.addToDoItem(doItem);
	}
	
	
	
	
	
	
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getToDoLists() {
        return "Hello World";
    }
}
