package com.codetutor.dotolist;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.codetutor.dotolist.model.Author;
import com.codetutor.dotolist.model.ToDoAppRestStatus;
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
	public Response registerUser(Author author){
		Author entity = toDoService.registerAuthor(author);	
		return Response.status(Status.CREATED).entity(entity).build();
	}
	
	@POST
	@Path("/autheticate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response isAuthorAuthorized(Author author) {
		ToDoAppRestStatus entity= toDoService.isAuthorAutheticated(author);
		Response response=null;
		if(entity.getStatusCode()==200) {
			response= Response.status(Status.OK).entity(entity).build();
		}else if (entity.getStatusCode()==404) {
			response= Response.status(Status.NOT_FOUND).entity(entity).build();
		}
		return response;
	}
}
