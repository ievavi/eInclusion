package org.einclusion.model;

import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;
public class DBManager {
	public static JdbcConnectionPool cp = JdbcConnectionPool.create(
            "jdbc:h2:data/Student;AUTO_SERVER=TRUE", "sa", "");
	public DBManager() throws SQLException{

       // cp.dispose();
    }
	

        
}
