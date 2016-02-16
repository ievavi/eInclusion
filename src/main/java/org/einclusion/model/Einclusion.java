package org.einclusion.model;

import java.sql.Connection;
import java.sql.Statement;

import org.einclusion.GUI.AppFrame;
import org.einclusion.GUI.EditDatabasePanel;
/**
 * 	Main class
 * 	@author student
 */
public class Einclusion {

	public static void main(String[] args) {
		AppFrame gui = new AppFrame();
		gui.setVisible(true);
		
		// Connects to database and gets topics after the gui has been loaded
		Connection conn = null;									// connection with a database
		Statement stmt = null;									// object used for executing sql statement
		EditDatabasePanel.getTopics(conn, stmt);
		for( String topic : EditDatabasePanel.treeSetTopics ){
			EditDatabasePanel.boxForTopics.addItem(topic);
		}
	}
}
