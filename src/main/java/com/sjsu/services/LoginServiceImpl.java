package com.sjsu.services;

import com.sjsu.dao.login.LoginDAO;
import com.sjsu.login.service.ILoginService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class LoginServiceImpl implements ILoginService {

	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://54.191.43.78:3306/CMPE281";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "root";
	
	@Override
	public boolean authenticate() {
		// TODO Auto-generated method stub
		boolean isAuthentic=false;
		String user="admin";
		String password="admin";
		try {
		if(selectRecordFromDb(user,password)) {
			System.out.println("Log in is successful.\n");
			isAuthentic=true;
		}
		else {
			System.out.println("Log in failed.\n");
			isAuthentic=false;
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
		return isAuthentic;
	}

	@Override
	public boolean createUser() {
		// TODO Auto-generated method stub
		String user="admin";
		String password="admin";
		LoginDAO req = new LoginDAO();
		req.setName(user);
		req.setPassword(password);
		try {
			insertRecordIntoDbUserTable(req);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	private static Connection getDBConnection() {

		Connection dbConnection = null;

		try {

			Class.forName(DB_DRIVER);

		} catch (ClassNotFoundException e) {

			System.out.println(e.getMessage());

		}

		try {

			dbConnection = DriverManager.getConnection(
                               DB_CONNECTION, DB_USER,DB_PASSWORD);
			return dbConnection;

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		return dbConnection;

	}
	
	public static boolean insertRecordIntoDbUserTable(LoginDAO clientResDetails) throws SQLException {

		Connection dbConnection = null;
		Statement statement = null;
		boolean isInsert=false;
		
		
		String insertTableSQL = "Insert into login (fname,email,contact,name, password) values (\""+clientResDetails.getFname()+"\",\""+clientResDetails.getEmail()+"\",\""+clientResDetails.getContact()+"\",\""+ clientResDetails.getName() +"\",\"" + clientResDetails.getPassword() +"\")";
		try {
			if(!(isUsernameUnique(clientResDetails.getName()))) {
				isInsert=false;
				System.out.println("Record already available.\n");
				return isInsert;
			}
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();
			// execute insert SQL statement
			statement.executeUpdate(insertTableSQL);
			isInsert=true;
			
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return isInsert;

	}
	
	public static boolean isUsernameUnique(String uname) throws SQLException {
		boolean isUnique=false;
		Connection dbConnection = null;
		Statement statement = null;

		String query = "SELECT * FROM login where name ='"+uname +"';";
		try {
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();

			ResultSet rs = statement.executeQuery(query);
			/*System.out.format("Checking is User name unique?\n");*/
			if (rs.next())
		      {
		        String name = rs.getString("name");
		        String password = rs.getString("password");
		        /*System.out.format("Value of records is: %s, %s\n", name, password);*/
		        isUnique=false;
		      }
			else {
				System.out.format("No records found.\n");
				isUnique=true;
			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return isUnique;

	}
	
	public static boolean selectRecordFromDb(String uname, String pass) throws SQLException {

		
		Connection dbConnection = null;
		Statement statement = null;
		boolean isAuthenicate=false;

		String query = "SELECT * FROM login where name ='"+uname +"'and password='"+pass+"';";
		try {
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();

			ResultSet rs = statement.executeQuery(query);
			/*System.out.format("Checking the records.\n");*/
			if (rs.next())
		      {
		        String name = rs.getString("name");
		        String password = rs.getString("password");
		         /*System.out.format("Value of records is: %s, %s\n", name, password);*/
		        isAuthenicate=true;
		      }
			else {
				System.out.format("No records found.\n");
			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return isAuthenicate;

	}

	
	

}