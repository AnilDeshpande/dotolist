package com.codetutor.dotolist.db;

public interface DBConstants {
	
	String dbUrl = "jdbc:derby://localhost:1527/TODODatabase;create=true;user=todoapp;password=todoapp";
	
	
	String TABLE_AUTHOR = "table_author";
	String AUTHOR_COL_ID="authorId";
	String AUTHOR_COL_NAME="authorName";
	String AUTHOR_COL_EMAIL="authorEmailId";
	String AUTHOR_COL_PASSWORD="authorPassword";
	
	String TABLE_TODOS="table_todos";
	String TODO_COL_ID="id";
	String TODO_COL_TODOVALUE="todoString";
	String TODO_COL_DATE="date";
	String TODO_COL_PLACE="place";
	String TODO_COL_AUTHOD_ID="author_id";
	

}
