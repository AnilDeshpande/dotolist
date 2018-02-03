package com.codetutor.dotolist;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.codetutor.dotolist.exceptions.AuthorNotRegisredException;
import com.codetutor.dotolist.model.Author;
import com.codetutor.dotolist.model.AuthorWithActiveSession;
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
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginAuthor(Author author, @Context HttpServletRequest request) {
		AuthorWithActiveSession entity=null;
		Response response=null;
		try {
			entity = toDoService.login(author,request);
			response= Response.status(Status.CREATED).entity(entity).build();
		}catch (AuthorNotRegisredException e) {
			ToDoAppRestStatus appRestStatus=new ToDoAppRestStatus();
			appRestStatus.setStatusCode(e.errorCode);
			appRestStatus.setMessage(e.getMessage());
			response = Response.status(Status.FORBIDDEN).entity(appRestStatus).build();
		}catch (Exception e) {
			response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}	
		return response;
	}
	
	@POST
	@Path("/signout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signoutAuthor(Author author) {
		Response response=null;
		boolean isSignedOut = toDoService.signOut(author);
		if(isSignedOut) {
			response = Response.status(Status.NO_CONTENT).build();
		}else {
			ToDoAppRestStatus appRestStatus=new ToDoAppRestStatus(404, "Couldn't signout, no sessoion active");
			response = Response.status(Status.NOT_FOUND).entity(appRestStatus).build();
		}
		return response;
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
