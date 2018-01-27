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
	
	String CREATE_TABLE_AUTHOR = "CREATE TABLE IF NOT EXISTS "+TABLE_AUTHOR+
												"("+AUTHOR_COL_ID+" INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"+
												", "+AUTHOR_COL_NAME+ " varchar(40) not null"+
												", "+AUTHOR_COL_EMAIL+" varchar(40) not null"+
												", "+AUTHOR_COL_PASSWORD+" varchar(40) not null, CONSTRAINT primary key ("+AUTHOR_COL_ID+"))";
	
	String CREATE_TABLE_TODOS = "CREATE TABLE IF NOT EXISTS "+TABLE_TODOS+
												"( "+TODO_COL_ID+" int not null generated always as identity"+
												", "+TODO_COL_TODOVALUE+" varchar(40) not null"+
												", "+TODO_COL_DATE+" BIGINT not null"+
												", "+TODO_COL_PLACE+" varchar(40) not null"+
												", "+TODO_COL_AUTHOD_ID+"INTEGER NOT NULL, CONSTRAINT primary key ("+TODO_COL_ID+")"+
												", "+"CONSTRAINT TODO_AUTHOR_KEY FOREIGN KEY ("+TODO_COL_AUTHOD_ID+")"+ 
												 " REFERENCES "+TABLE_AUTHOR+"("+AUTHOR_COL_ID+")  )";
	

}
