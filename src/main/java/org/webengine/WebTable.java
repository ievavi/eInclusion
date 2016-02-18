package org.webengine;

//import org.apache.log4j.Logger;

public abstract class WebTable {
	// private static final Logger LOG = Logger.getLogger(M2Table.class); //
	// Logger for M2Table
	static final String JDBC_DRIVER = "org.h2.Driver"; // JDBC driver name
	static final String DB_URL = "jdbc:h2:data/Student"; // databse URL
															// (location of
															// database)
	static final String USER = "sa"; // username for database
	static final String PASS = ""; // password for database
	static final String DB_TABLE_NAME = "STUDENT"; // database for student data
	static final String DB_REGRESSION_TABLE = "MODELMANAGER"; // database for
																// function data
																// // path to
																// exported file

	public abstract void readDBfiltered(String colName, String value);

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
	// public String getRegressionModel(String course);
	// public void prepareRegressionModels();

}
