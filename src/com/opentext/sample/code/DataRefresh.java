package com.opentext.sample.code;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Properties;

import java.sql.CallableStatement;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;

public class DataRefresh {
	private String properties_file = "database.properties";
	
	public DataRefresh() {
	}
	
	private String getProperty(String key) {
		Properties props = new Properties();
		
		try {
			props.load(new FileInputStream(properties_file));
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		return props.getProperty(key);
	}
	
	private Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/datarefresh", "root", "password");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public String getRefreshDate(String volume, String dataObject) {
		try {
			CallableStatement cStmt = getConnection().prepareCall("{CALL getDataRefreshDate(?, ?)}");
			
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
	
	
	
	//public static void main(String[] args) {
	//	System.out.println(new DataRefresh().getRefreshDate("volume0", "/Resources/myDataObject0.data"));
	//}
	
	
}
