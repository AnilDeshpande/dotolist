package db;

import java.util.HashMap;
import java.util.Map;

import com.codetutor.dotolist.model.Author;
import com.codetutor.dotolist.model.ToDoList;

public class InMemoryDB {
	
	private static  long AUTHOR_ID=0;
	private static  long TODO_ID=0;
	
	public static long getAuthorId() {
		return ++AUTHOR_ID;
	}
	
	public static long getToDoId() {
		return ++TODO_ID;
	}

}
