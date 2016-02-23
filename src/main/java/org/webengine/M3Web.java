package org.webengine;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.einclusion.GUI.M3Table;

public class M3Web extends WebTable {
	private static final long serialVersionUID = 1003L;
	// Logger for M3Table
	private static final Logger LOG = Logger.getLogger(M3Table.class);	
	public static ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
	static final String[] COLUMNS = {"PHONE","NAME","TOPIC","IWS","ELE","ELM","KLBL","SUBMITDATE","M3"};
	static File path;
	Connection conn = null;
    PreparedStatement pStmt = null;
    
    public M3Web () {
    	LOG.info("M3Table has been intialized");
    }
    
    public void readDBfiltered(String colName, String value) {
		try { 
            Class.forName(JDBC_DRIVER);
            LOG.info("Generating filter from database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
            
            colName = getShortForColumn(colName);
            String sql;
            if (colName.equals("*") || value.equals("All")) {
            	sql = "SELECT * FROM " + DB_TABLE_NAME + " ORDER BY NAME";
            	pStmt = conn.prepareStatement(sql);
            } else if (colName.equals("M3") && (value.equals("Green") || value.equals("Orange") || value.equals("Red") )) {
            	sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE "+colName+" > ";
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
            	sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE "+colName+" = ? ORDER BY NAME";
            	pStmt = conn.prepareStatement(sql);
            	if (colName.equals("NAME") || colName.equals("TOPIC") || colName.equals("SUBMITDATE"))
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
            	String name = rs.getString("NAME");
            	String iws = String.format(Locale.US, "%.2f", rs.getDouble("IWS"));
            	String ele = String.format(Locale.US, "%.2f", rs.getDouble("ELE"));
            	String elm = String.format(Locale.US, "%.2f", rs.getDouble("ELM"));
            	String klbl = String.format(Locale.US, "%.2f", rs.getDouble("KLBL"));
            	Date dateStamp = new Date(rs.getTimestamp("SUBMITDATE").getTime());
            	String date = dateStamp.toString();
            	String m3 = rs.getString("M3");
            	
            	ArrayList<String> row = new ArrayList<String>();
        		row.add(phone); 		
        		row.add(topic);		
        		row.add(name);	
        		row.add(iws);
        		row.add(ele); 
        		row.add(elm);	
        		row.add(klbl);	
        		row.add(date);
        		row.add(round(Double.parseDouble(m3),2)+"");
        		
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
		labels.add("Instructor");
		labels.add("E-environment");
		labels.add("E-materials");
		labels.add("Before learning");
		labels.add("Submit date");
		labels.add("M3");
		return labels;
	}
}
