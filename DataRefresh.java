package com.opentext.sample;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.Properties;

import java.sql.CallableStatement;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;

public class DataRefresh {
	private static final String PROPERTIES_FILE = "database.properties";
	
	public DataRefresh() {
		//System.out.println(getRefreshDate("volume0", "/Resources/myDataObject0.data"));
	}
	
	private String getProperty(String key) {
		Properties props = new Properties();
		
		try {
			props.load(new FileInputStream(PROPERTIES_FILE));
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		return props.getProperty(key);
	}
	
	private Connection getConnection() {
		try {
			Class.forName(getProperty("driver"));
			return DriverManager.getConnection(getProperty("connectionString"), getProperty("username"), getProperty("password"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public String getRefreshDate(String volume, String dataObject) {
		try {
			CallableStatement cStmt = getConnection().prepareCall(getProperty("query"));
			
			cStmt.setString(1, volume);
			cStmt.setString(2, dataObject);
			cStmt.execute();
			
			ResultSet rs1 = cStmt.getResultSet();
			
			while(rs1.next()) {
				return rs1.getString("data_refresh").toString();
			}
			
			rs1.close();
			
			return "";
		} catch (SQLException e) {
			return "";
		}
	}
	
	/*
	public static void main(String[] args) {
		new DataRefresh();
	}
	*/
}
