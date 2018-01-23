package com.codetutor.dotolist;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codetutor.dotolist.model.Author;
import com.codetutor.dotolist.model.Status;
import com.codetutor.dotolist.service.ToDoService;

@Path("authors")
public class MyAuthors {

ToDoService toDoService;
	
	public MyAuthors() {
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
	@Path("/autheticate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Status isAuthorAuthorized(Author author) {
		System.out.println(" MyAuthors isAuthorAuthorized: "+ author);
		return toDoService.isAuthorAutheticated(author);
	}
	
}
