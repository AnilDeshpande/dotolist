package com.codetutor.dotolist.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseHelper {
	
	private Connection connection;
	
	private static DataBaseHelper instance;
	
	private DataBaseHelper() {
		// TODO Auto-generated constructor stub
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			connection = DriverManager.getConnection(DBConstants.dbUrl);
			
			if(!tableExists(DBConstants.CREATE_TABLE_AUTHOR)) {
				Statement statement = connection.createStatement();
				statement.execute(DBConstants.CREATE_TABLE_AUTHOR);
			}
			
			if(!tableExists(DBConstants.CREATE_TABLE_TODOS)) {
				Statement statement = connection.createStatement();
				statement.execute(DBConstants.CREATE_TABLE_TODOS);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static DataBaseHelper getInstance() {
		if(instance==null) {
			instance = new DataBaseHelper();
		}
		return instance;
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	private boolean tableExists ( String table ) {
	    int numRows = 0;
	    try {
	      DatabaseMetaData dbmd = connection.getMetaData();
	      // Note the args to getTables are case-sensitive!
	      ResultSet rs = dbmd.getTables( null, "TODODatabase", table, null);
	      while( rs.next() ) ++numRows;
	      rs.close();
	    } catch ( SQLException e ) {
	        String theError = e.getSQLState();
	        System.out.println("Can't query DB metadata: " + theError );
	        System.exit(1);
	    }
	    
	    return numRows > 0;
	}

}
