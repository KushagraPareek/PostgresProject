/****************************************************************************
CSE532 -- Project 2
File name: PostgresConnector.java
Author: KUSHAGRA PAREEK (112551443)
Brief description: This file provides an object to connect to database
****************************************************************************/

/**
I pledge my honor that all parts
of this project were done by me alone and without collaboration with
anybody else.
**/


package com.kush.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnector {

	private String pserver = "127.0.0.1";
	private String admin = "postgres";
	private String pass = "root";
	private String port = "5432";
	private String db = "woco1";
	private Connection conn = null;
	
	public Connection connect()  {
	
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		  try {
			  
			  conn = DriverManager.getConnection(
	                "jdbc:postgresql://"+pserver+":"+port+"/"+db, admin, pass);

	            if (conn != null) {
	                System.out.println("Connected to the database!");
	            } else {
	                System.out.println("Failed to make connection!");
	            }

		  }catch(SQLException e){
        	 System.out.println(e.getMessage());
         }catch(Exception e) {
        	 System.out.println(e.getMessage());
         }
          
         return conn;
	}
	
	public void disconnect() {
		try {
			if(conn != null) {
				conn.close();
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
