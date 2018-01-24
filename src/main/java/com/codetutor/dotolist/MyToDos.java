package com.codetutor.dotolist;

import java.util.List;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codetutor.dotolist.model.Status;
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
	public List<ToDoItem> getAllToDosFromAnAuthor(@PathParam("authorEmailId") String authorEmailId){
		return toDoService.getMessages(authorEmailId);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ToDoItem addToDoByAuthor(ToDoItem toDoItem){
		return toDoService.addToDoItem(toDoItem);
	}
	
	@PUT
	@Path("/{toDoString}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ToDoItem updateToDoItem(ToDoItem currentToDoItem ,@PathParam("toDoString") String toDoString) {
		return toDoService.updateToDoItem(currentToDoItem, toDoString);
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ToDoItem updateToDoItem(UpdateToDoItem updateToDoItem) {
		return toDoService.updateToDoItem(updateToDoItem.getCurrentToDoItem(), updateToDoItem.getProposedToDoItem());
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Status deleteMessageByAuthor(ToDoItem doItem){
		return toDoService.deleteToDoItem(doItem);
	}
}
