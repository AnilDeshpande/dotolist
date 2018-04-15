package com.codetutor.dotolist.model;

public class UpdateToDoItem {
	
	ToDoItem currentToDoItem;
	ToDoItem proposedToDoItem;
	
	public UpdateToDoItem() {
		super();
	}
	
	public UpdateToDoItem(ToDoItem currentToDoItem, ToDoItem proposedToDoItem) {
		super();
		this.currentToDoItem = currentToDoItem;
		this.proposedToDoItem = proposedToDoItem;
	}
	public ToDoItem getCurrentToDoItem() {
		return currentToDoItem;
	}
	public void setCurrentToDoItem(ToDoItem currentToDoItem) {
		this.currentToDoItem = currentToDoItem;
	}
	public ToDoItem getProposedToDoItem() {
		return proposedToDoItem;
	}
	public void setProposedToDoItem(ToDoItem proposedToDoItem) {
		this.proposedToDoItem = proposedToDoItem;
	}

}
