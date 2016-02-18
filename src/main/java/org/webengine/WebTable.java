package org.webengine;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

//import org.apache.log4j.Logger;

public abstract class WebTable {
	static final String JDBC_DRIVER = "org.h2.Driver"; // JDBC driver name
	 // databse URL (location of database)
	static final String DB_URL = "jdbc:h2:data/Student";
	static final String USER = "sa"; // username for database
	static final String PASS = ""; // password for database
	static final String DB_TABLE_NAME = "STUDENT"; // database for student data
	// database for function data path to exported file
	static final String DB_REGRESSION_TABLE = "MODELMANAGER";

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
	// public void actionPerformed(ActionEvent e) - jaaskataas vai nav noderiigs datu izvilkshanai
}
