package org.webengine;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.einclusion.frontend.Coefficient;
import org.einclusion.frontend.RegressionModel;
import org.einclusion.model.M1;
import org.einclusion.model.ModelManager;
import org.einclusion.model.Regression;

import com.google.gson.Gson;


import org.apache.log4j.Logger;

public abstract class WebTable {
	static final String JDBC_DRIVER = "org.h2.Driver"; // JDBC driver name
	// databse URL (location of database)
	static final String DB_URL = "jdbc:h2:data/Student;AUTO_SERVER=TRUE";
	static final String USER = "sa"; // username for database
	static final String PASS = ""; // password for database
	static final String DB_TABLE_NAME = "STUDENT"; // database for student data
	// database for function data path to exported file
	static final String DB_REGRESSION_TABLE = "MODELMANAGER";
	private static final Logger LOG = Logger.getLogger(WebTable.class); 

	public abstract void readDBfiltered(String colName, String value);

	public abstract ArrayList<String> returnLabels();

	public String getShortForColumn(String colName) {
		String shortCol = null;
		switch (colName) {
		case "Phone":
			shortCol = "PHONE";
			break;
		case "Topic":
			shortCol = "TOPIC";
			break;
		case "Name":
			shortCol = "NAME";
			break;
		case "Motivation":
			shortCol = "SWL";
			break;
		case "Digital skills":
			shortCol = "DS";
			break;
		case "Learning ability":
			shortCol = "SAL";
			break;
		case "E-materials":
			shortCol = "ELM";
			break;
		case "Instructor":
			shortCol = "IWS";
			break;
		case "E-environment":
			shortCol = "ELE";
			break;
		case "Predicted usage":
			shortCol = "PU";
			break;
		case "Submit date":
			shortCol = "SUBMITDATE";
			break;
		case "M1":
			shortCol = "M1";
			break;
		case "M2":
			shortCol = "M2";
			break;
		case "M3":
			shortCol = "M3";
			break;
		case "VOTE":
			shortCol = "VOTE";
			break;
		default: // All
			shortCol = "*";
			break;
		}
		return shortCol;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static ArrayList<ArrayList<String>> coef() {
		ArrayList<ArrayList<String>> coef = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pStmt = null;

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			conn.setAutoCommit(false);
			
			pStmt = conn.prepareStatement("SELECT * FROM " + DB_REGRESSION_TABLE + " ORDER BY KEY");
			ResultSet rs = pStmt.executeQuery();
			
			while (rs.next()){
				String s1 = rs.getString(1);
				String s2 = rs.getString(2);
				String s3 = rs.getString(3);
				String s4 = rs.getString(4);
				
				ArrayList<String> row = new ArrayList<String>();
				row.add(s1);
				row.add(s2);
				row.add(s3);
				row.add(s4);
				
				coef.add(row);
			}
		} catch (SQLException | ClassNotFoundException sqle) {
			sqle.printStackTrace();
		}
		return coef;
	}
	
	public static String coef2() {
		ArrayList<String> coef = new ArrayList<>();
		ArrayList<String> row =  new ArrayList<String>();
		Connection conn = null;
		PreparedStatement pStmt = null;
		String ELM = null;

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			conn.setAutoCommit(false);
			
			pStmt = conn.prepareStatement("SELECT * FROM " + DB_REGRESSION_TABLE + " ORDER BY KEY");
			ResultSet rs = pStmt.executeQuery();
			
			while (rs.next()){
				String s1 = rs.getString(1);
				String s2 = rs.getString(2);
				String s3 = rs.getString(3);
				String s4 = rs.getString(4);
				
				row = new ArrayList<String>();
				row.add(s1);
				row.add(s2);
				row.add(s3);
				row.add(s4);
				
				coef.addAll(row);
				Gson gson = new Gson();
				//RegressionModel rm = gson.fromJson("{"+s1+":"+s2+ ":"+s3+":"+s4+"}", RegressionModel.class);
				//ArrayList al = rm.coefficients;//.getCoefficients();
				//ELM = al.get(2).toString();
				
				//String json = new Gson().toJson(row);
				
				
			}
		} catch (SQLException | ClassNotFoundException sqle) {
			sqle.printStackTrace();
		}
		return "*";
	}
	
	
	//public static ArrayList<ArrayList<String>> coef3() {
	  public static String coef3() {
			
		ArrayList<ArrayList<String>> coef = new ArrayList<>();
		//ArrayList<Coefficient> al = new ArrayList<>();
		String aaa = null;
		
		Connection conn = null;
		PreparedStatement pStmt = null;

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			conn.setAutoCommit(false);
			
			pStmt = conn.prepareStatement("SELECT * FROM " + DB_REGRESSION_TABLE + " ORDER BY KEY");
			ResultSet rs = pStmt.executeQuery();
			
			//String s1;
			//String s2;
			//String s3;
			//String s4;
			
			
			while (rs.next()){
			String	s1 = rs.getString(1);
			String	s2 = rs.getString(2);
			String	s3 = rs.getString(3);
			String	s4 = rs.getString(4);
				
			//	int ind = s4.indexOf("ELM");
		//		String EMLvalue = s1+s2+s3+s4.substring(ind);
				
				ArrayList<String> row = new ArrayList<String>();
				row.add(s1);
				row.add(s2);
				row.add(s3);
				row.add(s4);
				
				coef.add(row);
				
			}
			
			//Gson g = new Gson(); 
			//RegressionModel p = g.fromJson("{"+coef+"}", RegressionModel.class);
			//p.getCoefficients();
		//RegressionModel rm = new RegressionModel(null,null);
		
		//	ModelManager mm = new ModelManager();
		//	ModelManager.initModelManager(coef.toString());
			
			//s4 analyse
			//s4.contains("EML")
			int ind = coef.get(0).get(0).indexOf("SWL");
			
			aaa = coef.get(0).get(0).substring(ind, ind+6);
			LOG.info(aaa+ "---***---");
			
					//Read more: https://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html#ixzz6ygpIMC9D
			
		} catch (SQLException | ClassNotFoundException sqle) {
			sqle.printStackTrace();
		}
		return aaa;
	}
	
		public static ArrayList<ArrayList<String>> coef4() {
			ArrayList<ArrayList<String>> coef = new ArrayList<>();
			Connection conn = null;
			PreparedStatement pStmt = null;

			try {
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				conn.setAutoCommit(false);
				
				pStmt = conn.prepareStatement("SELECT * FROM " + DB_REGRESSION_TABLE + " ORDER BY KEY");
				ResultSet rs = pStmt.executeQuery();
				
				while (rs.next()){
					
					String s1 = rs.getString(1);
					String s2 = rs.getString(2);
					String s3 = rs.getString(3);
					String s4 = rs.getString(4);
					
					ArrayList<String> row = new ArrayList<String>();
					row.add(s1);
					//row.add(s2);
					//row.add(s3);
					//row.add(s4);
					
					coef.add(row);
				}
			} catch (SQLException | ClassNotFoundException sqle) {
				sqle.printStackTrace();
			}
			return coef;

		
		
		
		}
	
		public static String coef5() {
			ArrayList<ArrayList<String>> al = WebTable.coef4();
			Gson gson = new Gson();
			gson.toJson(al);
			return gson.toJson(al).toString();

		
		
		
		}
	
		
		
}
