package org.einclusion.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.einclusion.GUI.AppFrame;
import org.einclusion.GUI.EditDatabasePanel;
/**
 * 	Main class
 * 	@author student
 */
public class Einclusion {
	public static DBManager dbManager;
	public static void main(String[] args) throws SQLException {
		AppFrame gui = new AppFrame();
		gui.setVisible(true);
		
		// Connects to database and gets topics after the gui has been loaded
		Connection conn = null;									// connection with a database
		Statement stmt = null;									// object used for executing sql statement
		// Pool manager to handle concurrent connections (NOTE database still uses 1 connection
		// but the poolmanager allows to run queries through it.
		// !!! ALL queries MUST use the connection pool object.
		Einclusion.dbManager = new DBManager();
		conn = dbManager.cp.getConnection();
		//use database like this
		//Einclusion.dbManager.cp.getConnection().prepareStatement("CREATE TABLE " + "asd " + " (PersonID int, LastName varchar(255))").execute();
		EditDatabasePanel.getTopics(conn, stmt);
		for( String topic : EditDatabasePanel.treeSetTopics ){
			EditDatabasePanel.boxForTopics.addItem(topic);
		}
	}
}
