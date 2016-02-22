package org.einclusion.model;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;
public class DBManager {
	public static JdbcConnectionPool cp = JdbcConnectionPool.create(
            "jdbc:h2:data/Student", "sa", "");
	public DBManager() throws SQLException{
		String[] args = new String[2]; 
        args[0] = "CREATE TABLE " + "asd "  + " (PersonID int, LastName varchar(255))";
        args[1] = "DROP TABLE " + "asd";
        for (int i = 0; i < args.length ; i++) {
        	
        	Connection conn = cp.getConnection();

            conn.createStatement().execute(args[i]);
            //System.out.println("Executed: " + args[i]);
            System.out.println("Active connections: " + cp.getActiveConnections());
            conn.close();
        }
       // cp.dispose();
    }
	
    public static void main(String[] arguments) throws Exception {
    	
       
    }
        
}
