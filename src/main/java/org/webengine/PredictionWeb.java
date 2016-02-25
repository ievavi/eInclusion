package org.webengine;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.einclusion.frontend.RegressionModel;

public class PredictionWeb extends WebTable {
	private static final long serialVersionUID = 1004L;
	// Logger for PredictionWeb
	private static final Logger LOG = Logger.getLogger(PredictionWeb.class);	
	public static ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
	static final String[] COLUMNS = {"PHONE","TOPIC","NAME","M1","M2","M3","RELIABILITY"};
	static File path;				// path to exported file
	Connection conn = null;
    PreparedStatement pStmt = null;
    Statement stmt = null;
    ArrayList<RegressionModel> regressionModels = new ArrayList<>();
    
    public PredictionWeb () {
    	LOG.info("PredictionTable has been initialized");
    }
    
public void readDBfiltered(String colName, String value) {
		
		try { 
            Class.forName(JDBC_DRIVER);
            LOG.info("Generating filter from database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
            
            String sql;
            colName = getShortForColumn(colName);
            if (colName.equals("*") || value.equals("All")) {
            	sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE OU IS null ORDER BY NAME";
            	pStmt = conn.prepareStatement(sql);
            } else if ( (colName.equals("M3") && (value.equals("Green") || value.equals("Orange") || value.equals("Red") )) || 
            		(colName.equals("M2") && (value.equals("Green") || value.equals("Orange") || value.equals("Red") )) ) {
            	sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE OU is null AND "+colName+" > ";
            	switch (value) {
	            	case "Green":
	            		sql += "60";
	            		break;
	            	case "Orange":
	            		sql += "25" + " AND " + colName + " <= " + "60";
	            		break;
	            	case "Red":
	            		sql += "-0.1" + " AND " + colName + " <= " + "25";
	            		break;
            	}
            	sql += " ORDER BY NAME";
            	pStmt = conn.prepareStatement(sql);
            } else {
            	sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE "+colName+" = ? AND OU IS null ORDER BY NAME";
            	pStmt = conn.prepareStatement(sql);
            	if (colName.equals("NAME") || colName.equals("TOPIC") || colName.equals("PHONE") || colName.equals("RELIABILITY") ) 
            		pStmt.setString(1, value);
            	else
            		pStmt.setDouble(1, Double.parseDouble(value));
            }
            ResultSet rs = pStmt.executeQuery();
            conn.commit();
            
            list.clear(); 				// clears the arraylist
            while (rs.next()) {
            	String phone = rs.getString("PHONE");
            	String topic = rs.getString("TOPIC");
                sql ="SELECT COEFFICIENT FROM MODELMANAGER WHERE COEFFICIENT IS NOT null AND KEY LIKE 'M2-"+topic+"' OR KEY LIKE 'M3-"+topic+"'";
                stmt = conn.createStatement();
                ResultSet rs1 = stmt.executeQuery(sql);
               	ArrayList<String> m2m3coef = new ArrayList<String>();
                while(rs1.next()){
                	String coef = rs1.getString("COEFFICIENT");
                	m2m3coef.add(coef);
                }
                conn.commit();
            	
            	String name = rs.getString("NAME");
            	String m1 = String.format(Locale.US, "%.2f", rs.getDouble("M1"));
            	String m2 = String.format(Locale.US, "%.2f", rs.getDouble("M2"));
            	String m3 = String.format(Locale.US, "%.2f", rs.getDouble("M3"));
            	String reliability = rs.getString("RELIABILITY");
            	Date dateStamp = new Date(rs.getTimestamp("SUBMITDATE").getTime());
				String date = dateStamp.toString();
            	System.out.println(reliability);
            	
            	if( m2m3coef.size()>1 ){
            		if( (Double.parseDouble(m2m3coef.get(0)) >= Double.parseDouble(m2m3coef.get(1))) ){
            			m3 = "-1.0";
            		} else {
            			m2 = "-1.0";
            		}
            	}
            	
            	ArrayList<String> row = new ArrayList<String>();
    			row.add(phone);
    			row.add(topic);	
    			row.add(name);
    			row.add(date);
    			row.add(m1);
    			row.add(m2);
    			row.add(m3);
    			row.add(reliability);
    			if( row.size() > 0)
    				list.add(row);
            }
            LOG.info("Filter generated successfully");
            
        } catch (SQLException sqle) { //Handle errors for JDBC
        	LOG.error(sqle.getMessage()+" "+sqle.getCause());
            sqle.printStackTrace();
        } catch (Exception e) { 	//Handle errors for Class.forName
        	LOG.error(e.getMessage()+" "+e.getCause());
            e.printStackTrace();
        } finally {
            try {
                if (pStmt!=null)
                    conn.close();
                if (stmt!=null)
                	stmt.close();
                if (conn!=null)
                    conn.close();
            } catch (SQLException sqle) {
            	LOG.error(sqle.getMessage()+" "+sqle.getCause());
                sqle.printStackTrace();
            }
        }
	}

public ArrayList<String> returnLabels() {
	ArrayList<String> labels = new ArrayList<>();
	labels.add("Nr");
	labels.add("Phone");
	labels.add("Topic");
	labels.add("Name");
	labels.add("M1");
	labels.add("M2");
	labels.add("M3");
	labels.add("Reliability");
	return labels;
}
}
