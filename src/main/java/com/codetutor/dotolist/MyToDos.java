package com.codetutor.dotolist;

import java.util.List;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.codetutor.dotolist.exceptions.AuthorNotLoggedIn;
import com.codetutor.dotolist.exceptions.AuthorNotRegisredException;
import com.codetutor.dotolist.exceptions.InvalidOrForiddedException;
import com.codetutor.dotolist.exceptions.ToDoItemNotFoundException;
import com.codetutor.dotolist.model.ToDoAppRestStatus;
import com.codetutor.dotolist.model.ToDoItem;
import com.codetutor.dotolist.model.UpdateToDoItem;
import com.codetutor.dotolist.service.ToDoService;

@Path("todolists")
public class MyToDos {
	
	ToDoService toDoService;
	
	public MyToDos() {
		// TODO Auto-generated constructor stub
		super();
		toDoService = ToDoService.getInstance();
	}
	
	@GET
	@Path("/{authorEmailId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllToDosFromAnAuthor(@PathParam("authorEmailId") String authorEmailId, @HeaderParam("token") String token){
		Response response=null;
		try {
			List<ToDoItem> list = toDoService.getMessages(authorEmailId, token);
			response = Response.status(Status.OK).entity(list).build();
		} catch (ToDoItemNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).entity(new ToDoAppRestStatus(e.errorCode, e.getMessage())).build();
		}
		catch (AuthorNotLoggedIn e) {
			response = Response.status(Status.PRECONDITION_FAILED).entity(new ToDoAppRestStatus(e.errorCode, e.getMessage())).build();
		}catch (InvalidOrForiddedException e) {
			response = Response.status(Status.FORBIDDEN).entity(new ToDoAppRestStatus(e.errorCode, e.getMessage())).build();
		}catch (Exception e) {
			response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addToDoByAuthor(ToDoItem toDoItem, @HeaderParam("token") String token){
		Response response=null;
		try {
			ToDoItem doItem = toDoService.addToDoItem(toDoItem, token);
			response = Response.status(Status.OK).entity(doItem).build();
		}catch (AuthorNotRegisredException e) {
			response = Response.status(Status.FORBIDDEN).entity(new ToDoAppRestStatus(e.errorCode,e.getMessage())).build();
		}catch (InvalidOrForiddedException e) {
			response = Response.status(Status.FORBIDDEN).entity(new ToDoAppRestStatus(e.errorCode, e.getMessage())).build();
		}catch (Exception e) {
			response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateToDoItem(UpdateToDoItem updateToDoItem, @HeaderParam("token") String token) {
		Response response=null;
		try {
			ToDoItem doItem = toDoService.updateToDoItem(updateToDoItem.getCurrentToDoItem(), updateToDoItem.getProposedToDoItem(), token);
			response = Response.status(Status.ACCEPTED).entity(doItem).build();
		}catch (ToDoItemNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).entity(new ToDoAppRestStatus(e.errorCode,e.getMessage())).build();
		}catch (InvalidOrForiddedException e) {
			response = Response.status(Status.FORBIDDEN).entity(new ToDoAppRestStatus(e.errorCode, e.getMessage())).build();
		}catch (Exception e) {
			response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMessageByAuthor(ToDoItem doItem, @HeaderParam("token") String token){
		Response response=null;
		try {
			toDoService.deleteToDoItem(doItem,token);
			response = Response.status(Status.NO_CONTENT).build();
		}catch (ToDoItemNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).entity(new ToDoAppRestStatus(e.errorCode,e.getMessage())).build();
		}catch (InvalidOrForiddedException e) {
			response = Response.status(Status.FORBIDDEN).entity(new ToDoAppRestStatus(e.errorCode, e.getMessage())).build();
		}catch (Exception e) {
			response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}
	
	@DELETE
	@Path("/{toDoId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteToDo(@PathParam("toDoId") long toDoId, @HeaderParam("token") String token){
		Response response=null;
		try {
			toDoService.deleteToDoItem(toDoId, token);
			response = Response.status(Status.NO_CONTENT).build();
		} catch (ToDoItemNotFoundException e) {
			response = Response.status(Status.NOT_FOUND).entity(new ToDoAppRestStatus(e.errorCode, e.getMessage())).build();
		}catch (InvalidOrForiddedException e) {
			response = Response.status(Status.FORBIDDEN).entity(new ToDoAppRestStatus(e.errorCode, e.getMessage())).build();
		}catch (Exception e) {
			response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}
}
