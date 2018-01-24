package com.codetutor.dotolist.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseHelper {
	
	Connection connection;
	
	public DataBaseHelper() {
		// TODO Auto-generated constructor stub
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			connection = DriverManager.getConnection(DBConstants.dbUrl);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
