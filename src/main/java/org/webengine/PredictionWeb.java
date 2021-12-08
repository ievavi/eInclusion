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
import org.einclusion.frontend.Coefficient;
import org.einclusion.frontend.RegressionModel;
import org.jfree.util.Log;

import com.google.gson.Gson;


public class PredictionWeb extends WebTable {
	private static final long serialVersionUID = 1004L;
	// Logger for PredictionWeb
	private static final Logger LOG = Logger.getLogger(PredictionWeb.class);	
	public static ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
	static final String[] COLUMNS = {"PHONE","TOPIC","NAME","M1","M2","M3","RELIABILITY","VOTE"};
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
            	String vote = String.format(Locale.US, "%.2f", rs.getDouble("VOTE"));
            	Date dateStamp = new Date(rs.getTimestamp("SUBMITDATE").getTime());
				String date = dateStamp.toString();
            	
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
    			row.add(vote);
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
	labels.add("VOTE");
	labels.add("Reliability");
	return labels;
}

public static ArrayList<String> coef11() {
	ArrayList<ArrayList<String>> coef = new ArrayList<>();
	ArrayList<String> coef2 = new ArrayList<>();
	Connection conn = null;
	PreparedStatement pStmt = null;
	Gson gson = new Gson();
	int indOfArray=1;
	String jsonArray;

	try {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		conn.setAutoCommit(false);
		
		pStmt = conn.prepareStatement("SELECT * FROM " + DB_REGRESSION_TABLE + " ORDER BY KEY");
      //  sql ="SELECT COEFFICIENT FROM MODELMANAGER WHERE COEFFICIENT IS NOT null AND KEY LIKE 'M2-"+topic+"' OR KEY LIKE 'M3-"+topic+"'";

		
		ResultSet rs = pStmt.executeQuery();
	
		
		/////////////////////////////////////////////////////////
		PredictionWeb pw = new PredictionWeb();
		ArrayList<String> aaa = pw.readDBfiltered2("All","All");
		LOG.info("COEFF LIST SIZE: "+ aaa.size());
		/////////////////////////////////////////////////////////
		//rs.findColumn("SWL")
		//rs.
		int count = 0;
		//while (rs.next()){
		if (rs.next()) {
		   
		        while (rs.next()) {
		        	LOG.info("COUNT OF RESULTSET"+ count++);
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
		}
		
	//	LOG.info("ROW SIZE: "+row.size());
		LOG.info("COEF SIZE: "+coef.size());
		
		
		jsonArray = gson.toJson(coef);
		LOG.info("JSON: "+ gson.toJson(coef));
		int lenghtOfArray = 	jsonArray.length();
		for (int i = 0; i < jsonArray.length(); i++)
		{
		
	
		 indOfArray = jsonArray.indexOf("SWL");
		LOG.info("Index of SWL: "+ jsonArray.indexOf("SWL"));
		coef2.add(jsonArray.substring(jsonArray.indexOf("SWL")+6, jsonArray.indexOf("SWL")+24));
		jsonArray.replaceFirst("SWL", "---");
		LOG.info("JSON__: "+ jsonArray);
		}
		
	} catch (SQLException | ClassNotFoundException sqle) {
		sqle.printStackTrace();
	}
	return coef2;
}


public static ArrayList<String> coefSAL() {
	ArrayList<ArrayList<String>> coef = new ArrayList<>();
	ArrayList<String> coef2 = new ArrayList<>();
	Connection conn = null;
	PreparedStatement pStmt = null;
	Gson gson = new Gson();
	int indOfArray=1;
	String jsonArray = new String();

	try {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		conn.setAutoCommit(false);
		
		pStmt = conn.prepareStatement("SELECT * FROM " + DB_REGRESSION_TABLE + " ORDER BY KEY");
      //  sql ="SELECT COEFFICIENT FROM MODELMANAGER WHERE COEFFICIENT IS NOT null AND KEY LIKE 'M2-"+topic+"' OR KEY LIKE 'M3-"+topic+"'";

		
		ResultSet rs = pStmt.executeQuery();
	
		
		/////////////////////////////////////////////////////////
		PredictionWeb pw = new PredictionWeb();
		ArrayList<String> aaa = pw.readDBfiltered2("All","All");
		LOG.info("COEFF LIST SIZE: "+ aaa.size());
		/////////////////////////////////////////////////////////
		//rs.findColumn("SWL")
		//rs.
		int count = 0;
		//while (rs.next()){
		if (rs.next()) {
		   
		        while (rs.next()) {
		        	LOG.info("COUNT OF RESULTSET"+ count++);
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
		}
		
	//	LOG.info("ROW SIZE: "+row.size());
		LOG.info("COEF SIZE: "+coef.size());
		
		
		jsonArray = gson.toJson(coef);
		LOG.info("JSON: "+ gson.toJson(coef));
		int lenghtOfArray = 	jsonArray.length();
		for (int i = 0; i < jsonArray.length(); i++)
		{
		
	
		 indOfArray = jsonArray.indexOf("SAL");
		LOG.info("Index of SAL: "+ jsonArray.indexOf("SAL"));
		coef2.add(jsonArray.substring(jsonArray.indexOf("SAL")+6, jsonArray.indexOf("SAL")+24));
		LOG.info("----------------SAL"+jsonArray.substring(jsonArray.indexOf("SAL")+6, jsonArray.indexOf("SAL")+24));
		jsonArray.replaceFirst("SAL", "---");
		LOG.info("JSON__: "+ jsonArray);
		}
		
	} catch (SQLException | ClassNotFoundException sqle) {
		sqle.printStackTrace();
	}
	return coef2;
}

public static ArrayList<String> coefELM() {
	ArrayList<ArrayList<String>> coef = new ArrayList<>();
	ArrayList<String> coef2 = new ArrayList<>();
	Connection conn = null;
	PreparedStatement pStmt = null;
	Gson gson = new Gson();
	int indOfArray=1;
	String jsonArray;

	try {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		conn.setAutoCommit(false);
		
		pStmt = conn.prepareStatement("SELECT * FROM " + DB_REGRESSION_TABLE + " ORDER BY KEY");
      //  sql ="SELECT COEFFICIENT FROM MODELMANAGER WHERE COEFFICIENT IS NOT null AND KEY LIKE 'M2-"+topic+"' OR KEY LIKE 'M3-"+topic+"'";

		
		ResultSet rs = pStmt.executeQuery();
	
		
		/////////////////////////////////////////////////////////
		PredictionWeb pw = new PredictionWeb();
		ArrayList<String> aaa = pw.readDBfiltered2("All","All");
		LOG.info("COEFF LIST SIZE: "+ aaa.size());
		/////////////////////////////////////////////////////////
		//rs.findColumn("SWL")
		//rs.
		int count = 0;
		//while (rs.next()){
		if (rs.next()) {
		   
		        while (rs.next()) {
		        	LOG.info("COUNT OF RESULTSET"+ count++);
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
		}
		
	//	LOG.info("ROW SIZE: "+row.size());
		LOG.info("COEF SIZE: "+coef.size());
		
		
		jsonArray = gson.toJson(coef);
		LOG.info("JSON: "+ gson.toJson(coef));
		int lenghtOfArray = 	jsonArray.length();
		for (int i = 0; i < jsonArray.length(); i++)
		{
		
	
		 indOfArray = jsonArray.indexOf("ELM");
		LOG.info("Index of ELM: "+ jsonArray.indexOf("ELM"));
		coef2.add(jsonArray.substring(jsonArray.indexOf("ELM")+6, jsonArray.indexOf("ELM")+24));
		LOG.info("----------------ELM"+jsonArray.substring(jsonArray.indexOf("ELM")+6, jsonArray.indexOf("ELM")+24));
		jsonArray.replaceFirst("ELM", "---");
		LOG.info("JSON__: "+ jsonArray);
		}
		
	} catch (SQLException | ClassNotFoundException sqle) {
		sqle.printStackTrace();
	}
	return coef2;
}

public static ArrayList<String> coefELE() {
	ArrayList<ArrayList<String>> coef = new ArrayList<>();
	ArrayList<String> coef2 = new ArrayList<>();
	Connection conn = null;
	PreparedStatement pStmt = null;
	Gson gson = new Gson();
	int indOfArray=1;
	String jsonArray = new String();

	try {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		conn.setAutoCommit(false);
		
		pStmt = conn.prepareStatement("SELECT * FROM " + DB_REGRESSION_TABLE + " ORDER BY KEY");
      //  sql ="SELECT COEFFICIENT FROM MODELMANAGER WHERE COEFFICIENT IS NOT null AND KEY LIKE 'M2-"+topic+"' OR KEY LIKE 'M3-"+topic+"'";

		
		ResultSet rs = pStmt.executeQuery();
	
		
		/////////////////////////////////////////////////////////
		PredictionWeb pw = new PredictionWeb();
		ArrayList<String> aaa = pw.readDBfiltered2("All","All");
		LOG.info("COEFF LIST SIZE: "+ aaa.size());
		/////////////////////////////////////////////////////////
		//rs.findColumn("SWL")
		//rs.
		int count = 0;
		//while (rs.next()){
		if (rs.next()) {
		   
		        while (rs.next()) {
		        	LOG.info("COUNT OF RESULTSET"+ count++);
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
		}
		
	//	LOG.info("ROW SIZE: "+row.size());
		LOG.info("COEF SIZE: "+coef.size());
		
		
		jsonArray = gson.toJson(coef);
		LOG.info("JSON: "+ gson.toJson(coef));
		int lenghtOfArray = 	jsonArray.length();
		for (int i = 0; i < jsonArray.length(); i++)
		{
		
	
		 indOfArray = jsonArray.indexOf("ELE");
		LOG.info("Index of ELE: "+ jsonArray.indexOf("ELE"));
		coef2.add(jsonArray.substring(jsonArray.indexOf("ELE")+6, jsonArray.indexOf("ELE")+24));
		LOG.info("----------------ELE"+jsonArray.substring(jsonArray.indexOf("ELE")+6, jsonArray.indexOf("ELE")+24));
		jsonArray.replaceFirst("ELE", "---");
		LOG.info("JSON__: "+ jsonArray);
		}
		
	} catch (SQLException | ClassNotFoundException sqle) {
		sqle.printStackTrace();
	}
	return coef2;
}

public static ArrayList<String> coefM2() {
	ArrayList<ArrayList<String>> coef = new ArrayList<>();
	ArrayList<String> coef2 = new ArrayList<>();
	Connection conn = null;
	PreparedStatement pStmt = null;
	Gson gson = new Gson();
	int indOfArray=1;
	String jsonArray = new String();

	try {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		conn.setAutoCommit(false);
		
		pStmt = conn.prepareStatement("SELECT * FROM " + DB_REGRESSION_TABLE + " ORDER BY KEY");
      //  sql ="SELECT COEFFICIENT FROM MODELMANAGER WHERE COEFFICIENT IS NOT null AND KEY LIKE 'M2-"+topic+"' OR KEY LIKE 'M3-"+topic+"'";

		
		ResultSet rs = pStmt.executeQuery();
	
		
	//	/////////////////////////////////////////////////////////
		PredictionWeb pw = new PredictionWeb();
		//ArrayList<String> aaa = pw.readDBfiltered2("All","All");
		//LOG.info("COEFF LIST SIZE: "+ aaa.size());
		/////////////////////////////////////////////////////////
		//rs.findColumn("SWL")
		//rs.
		int count = 0;
		//while (rs.next()){
		if (rs.next()) {
		   
		        while (rs.next()) {
		        	LOG.info("COUNT OF RESULTSET"+ count++);
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
		}
		
	//	LOG.info("ROW SIZE: "+row.size());
		LOG.info("COEF SIZE: "+coef.size());
		
		
		jsonArray = gson.toJson(coef);
		LOG.info("JSON: "+ gson.toJson(coef));
		int lenghtOfArray = 	jsonArray.length();
		for (int i = 0; i < jsonArray.length(); i++)
		{
		
	
		 indOfArray = jsonArray.indexOf("ELE");
		LOG.info("Index of ELE: "+ jsonArray.indexOf("ELE"));
		coef2.add(jsonArray.substring(jsonArray.indexOf("ELE")+6, jsonArray.indexOf("ELE")+24));
		LOG.info("----------------ELE"+jsonArray.substring(jsonArray.indexOf("ELE")+6, jsonArray.indexOf("ELE")+24));
		jsonArray.replaceFirst("ELE", "---");
		LOG.info("JSON__: "+ jsonArray);
		//LOG.info("$$$$$$$$$$$$$"+coef.get(2).get(3)); //0>2
		
		//if (coef.get(2).get(0).equals("M1-centroids-STEM"))
		//{LOG.info("$$$$$$$$$$$$$ 23 "+coef.get(2).get(3));}
		
		if (coef.get(0).get(0).equals("M1-centroids-STEM"))
		{LOG.info("$$$$$$$$$$$$$ 03 "+coef.get(0).get(3));}
		
		if (coef.get(1).get(0).equals("M1-centroids-STEM"))
		{LOG.info("$$$$$$$$$$$$$ 13 "+coef.get(1).get(3));}
		LOG.info("$$$$$$$$$$$$$ COEF SIZE:  "+coef.size());
		
		//M1-centroids-STEM
		}
		
	} catch (SQLException | ClassNotFoundException sqle) {
		sqle.printStackTrace();
	}
	return coef2;
}



public ArrayList<String> readDBfiltered2(String colName, String value) {
	
	 ArrayList<String> m2m3coef = new ArrayList();
	 
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
           	m2m3coef = new ArrayList<String>();
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
        	String vote = String.format(Locale.US, "%.2f", rs.getDouble("VOTE"));
        	Date dateStamp = new Date(rs.getTimestamp("SUBMITDATE").getTime());
			String date = dateStamp.toString();
        	
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
			row.add(vote);
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
	return m2m3coef;
}


public static String coefM1() {
	PredictionWeb pw = new PredictionWeb();
	ArrayList<RegressionModel> rm = pw.regressionModels;
	ArrayList<ArrayList<String>> rr = PredictionWeb.list;
	Gson gson = new Gson();
	//gson.toJson(src);
	
	LOG.info("%%%_%%%1"+rr.get(0).toString());
	LOG.info("%%%_%%%1"+rr.get(1).toString());
	LOG.info("%%%_%%%1"+rr.get(2).toString());
	LOG.info("%%%_%%%1"+rr.get(3).toString());
	
	LOG.info("^^^_^^^1"+rm.toString());
	//LOG.error("^^^_^^^1"+rm.get(1).toString());
	//LOG.error("^^^_^^^1"+rm.get(2).toString());
	//LOG.error("^^^_^^^1"+rm.get(3).toString());
	ArrayList<ArrayList<String>> wt = WebTable.coef();
	LOG.info("%%%_%%%1"+wt.get(0).toString());
	LOG.info("%%%_%%%1"+wt.get(1).toString());
	LOG.info("%%%_%%%1"+wt.get(2).toString());
	//LOG.error("%%%_%%%1"+wt.get(3).toString());
	
	LOG.info("%%%_%%%1111111111"+wt.get(0).get(3).toString());
	LOG.info("%%%_%%%______: "+wt.get(0).get(3).toString().substring(0, 7));
	
	//LOG.error("%%%_%%%2"+rr.get(0).get(3));
	
	
	return wt.get(0).get(3).toString().substring(0, 7);
	////return rm.toString();//jsonArray.substring(jsonArray.lastIndexOf("M1-centroids-STEM"), jsonArray.lastIndexOf("M1-centroids-STEM")+4);
	//return jsonArray
}

public static String coefDS() {
	PredictionWeb pw = new PredictionWeb();
	ArrayList<RegressionModel> rm = pw.regressionModels;
	ArrayList<ArrayList<String>> rr = PredictionWeb.list;
	Gson gson = new Gson();
	//gson.toJson(src);
	
	//LOG.info("%%%_%%%1"+rr.get(0).toString());
	//LOG.info("%%%_%%%1"+rr.get(1).toString());
	//LOG.info("%%%_%%%1"+rr.get(2).toString());
	//LOG.info("%%%_%%%1"+rr.get(3).toString());
	
	//LOG.info("^^^_^^^1"+rm.toString());
	
	//LOG.error("^^^_^^^1"+rm.get(1).toString());
	//LOG.error("^^^_^^^1"+rm.get(2).toString());
	//LOG.error("^^^_^^^1"+rm.get(3).toString());
	ArrayList<ArrayList<String>> wt = WebTable.coef();
	LOG.info("%%%_%%%1"+wt.get(0).toString());
	ArrayList al1 = new ArrayList();
	al1 = wt.get(0);
	//LOG.info("%%%_%%%1"+al1.get(4).toString());
	
	//LOG.info("%%%_%%%1"+wt.get(1).toString());
	//LOG.info("%%%_%%%1"+wt.get(2).toString());
	//LOG.error("%%%_%%%1"+wt.get(3).toString());
	
	//LOG.info("%%%_%%%1111111111"+wt.get(0).get(4).toString());
	//LOG.info("%%%_%%%______: "+wt.get(0).get(4).toString().substring(0, 7));
	
	//LOG.error("%%%_%%%2"+rr.get(0).get(3));
	LOG.info("+++++"+al1.toString().substring(53, 60));
	
	return al1.toString().substring(53, 60);
	////return rm.toString();//jsonArray.substring(jsonArray.lastIndexOf("M1-centroids-STEM"), jsonArray.lastIndexOf("M1-centroids-STEM")+4);
	//return jsonArray
}


public static String coefLA() {
	PredictionWeb pw = new PredictionWeb();
	ArrayList<RegressionModel> rm = pw.regressionModels;
	ArrayList<ArrayList<String>> rr = PredictionWeb.list;
	Gson gson = new Gson();
	//gson.toJson(src);
	
	//LOG.info("%%%_%%%1"+rr.get(0).toString());
	//LOG.info("%%%_%%%1"+rr.get(1).toString());
	//LOG.info("%%%_%%%1"+rr.get(2).toString());
	//LOG.info("%%%_%%%1"+rr.get(3).toString());
	
	//LOG.info("^^^_^^^1"+rm.toString());
	
	//LOG.error("^^^_^^^1"+rm.get(1).toString());
	//LOG.error("^^^_^^^1"+rm.get(2).toString());
	//LOG.error("^^^_^^^1"+rm.get(3).toString());
	ArrayList<ArrayList<String>> wt = WebTable.coef();
	LOG.info("%%%_%%%1"+wt.get(0).toString());
	ArrayList al1 = new ArrayList();
	al1 = wt.get(0);
	//LOG.info("%%%_%%%1"+al1.get(4).toString());
	
	//LOG.info("%%%_%%%1"+wt.get(1).toString());
	//LOG.info("%%%_%%%1"+wt.get(2).toString());
	//LOG.error("%%%_%%%1"+wt.get(3).toString());
	
	//LOG.info("%%%_%%%1111111111"+wt.get(0).get(4).toString());
	//LOG.info("%%%_%%%______: "+wt.get(0).get(4).toString().substring(0, 7));
	
	//LOG.error("%%%_%%%2"+rr.get(0).get(3));
	LOG.info("+++++LA"+al1.toString().substring(53+8, 60+8));
	
	return al1.toString().substring(53+8, 60+8);
	////return rm.toString();//jsonArray.substring(jsonArray.lastIndexOf("M1-centroids-STEM"), jsonArray.lastIndexOf("M1-centroids-STEM")+4);
	//return jsonArray
}

public static String coefE() {
	PredictionWeb pw = new PredictionWeb();
	ArrayList<RegressionModel> rm = pw.regressionModels;
	ArrayList<ArrayList<String>> rr = PredictionWeb.list;
	Gson gson = new Gson();
	//gson.toJson(src);
	
	//LOG.info("%%%_%%%1"+rr.get(0).toString());
	//LOG.info("%%%_%%%1"+rr.get(1).toString());
	//LOG.info("%%%_%%%1"+rr.get(2).toString());
	//LOG.info("%%%_%%%1"+rr.get(3).toString());
	
	//LOG.info("^^^_^^^1"+rm.toString());
	
	//LOG.error("^^^_^^^1"+rm.get(1).toString());
	//LOG.error("^^^_^^^1"+rm.get(2).toString());
	//LOG.error("^^^_^^^1"+rm.get(3).toString());
	ArrayList<ArrayList<String>> wt = WebTable.coef();
	LOG.info("%%%_%%%1"+wt.get(0).toString());
	ArrayList al1 = new ArrayList();
	al1 = wt.get(0);
	//LOG.info("%%%_%%%1"+al1.get(4).toString());
	
	//LOG.info("%%%_%%%1"+wt.get(1).toString());
	//LOG.info("%%%_%%%1"+wt.get(2).toString());
	//LOG.error("%%%_%%%1"+wt.get(3).toString());
	
	//LOG.info("%%%_%%%1111111111"+wt.get(0).get(4).toString());
	//LOG.info("%%%_%%%______: "+wt.get(0).get(4).toString().substring(0, 7));
	
	//LOG.error("%%%_%%%2"+rr.get(0).get(3));
	LOG.info("+++++LA"+al1.toString().substring(53+8+8, 60+8+8));
	
	return al1.toString().substring(53+8+8, 60+8+8);
	////return rm.toString();//jsonArray.substring(jsonArray.lastIndexOf("M1-centroids-STEM"), jsonArray.lastIndexOf("M1-centroids-STEM")+4);
	//return jsonArray
}

public static String coefI() {
	PredictionWeb pw = new PredictionWeb();
	ArrayList<RegressionModel> rm = pw.regressionModels;
	ArrayList<ArrayList<String>> rr = PredictionWeb.list;
	Gson gson = new Gson();
	//gson.toJson(src);
	
	//LOG.info("%%%_%%%1"+rr.get(0).toString());
	//LOG.info("%%%_%%%1"+rr.get(1).toString());
	//LOG.info("%%%_%%%1"+rr.get(2).toString());
	//LOG.info("%%%_%%%1"+rr.get(3).toString());
	
	//LOG.info("^^^_^^^1"+rm.toString());
	
	//LOG.error("^^^_^^^1"+rm.get(1).toString());
	//LOG.error("^^^_^^^1"+rm.get(2).toString());
	//LOG.error("^^^_^^^1"+rm.get(3).toString());
	ArrayList<ArrayList<String>> wt = WebTable.coef();
	LOG.info("%%%_%%%1"+wt.get(0).toString());
	ArrayList al1 = new ArrayList();
	al1 = wt.get(0);
	//LOG.info("%%%_%%%1"+al1.get(4).toString());
	
	//LOG.info("%%%_%%%1"+wt.get(1).toString());
	//LOG.info("%%%_%%%1"+wt.get(2).toString());
	//LOG.error("%%%_%%%1"+wt.get(3).toString());
	
	//LOG.info("%%%_%%%1111111111"+wt.get(0).get(4).toString());
	//LOG.info("%%%_%%%______: "+wt.get(0).get(4).toString().substring(0, 7));
	
	//LOG.error("%%%_%%%2"+rr.get(0).get(3));
	LOG.info("+++++LA"+al1.toString().substring(53+8+16, 60+8+16));
	
	return al1.toString().substring(53+8+16, 60+8+16);
	////return rm.toString();//jsonArray.substring(jsonArray.lastIndexOf("M1-centroids-STEM"), jsonArray.lastIndexOf("M1-centroids-STEM")+4);
	//return jsonArray
}

public static String coefEE() {
	PredictionWeb pw = new PredictionWeb();
	ArrayList<RegressionModel> rm = pw.regressionModels;
	ArrayList<ArrayList<String>> rr = PredictionWeb.list;
	Gson gson = new Gson();
	//gson.toJson(src);
	
	//LOG.info("%%%_%%%1"+rr.get(0).toString());
	//LOG.info("%%%_%%%1"+rr.get(1).toString());
	//LOG.info("%%%_%%%1"+rr.get(2).toString());
	//LOG.info("%%%_%%%1"+rr.get(3).toString());
	
	//LOG.info("^^^_^^^1"+rm.toString());
	
	//LOG.error("^^^_^^^1"+rm.get(1).toString());
	//LOG.error("^^^_^^^1"+rm.get(2).toString());
	//LOG.error("^^^_^^^1"+rm.get(3).toString());
	ArrayList<ArrayList<String>> wt = WebTable.coef();
	LOG.info("%%%_%%%1"+wt.get(0).toString());
	ArrayList al1 = new ArrayList();
	al1 = wt.get(0);
	//LOG.info("%%%_%%%1"+al1.get(4).toString());
	
	//LOG.info("%%%_%%%1"+wt.get(1).toString());
	//LOG.info("%%%_%%%1"+wt.get(2).toString());
	//LOG.error("%%%_%%%1"+wt.get(3).toString());
	
	//LOG.info("%%%_%%%1111111111"+wt.get(0).get(4).toString());
	//LOG.info("%%%_%%%______: "+wt.get(0).get(4).toString().substring(0, 7));
	
	//LOG.error("%%%_%%%2"+rr.get(0).get(3));
	LOG.info("+++++LA"+al1.toString().substring(53+8+16+8+2, 60+8+16+8+2));
	
	return al1.toString().substring(53+8+16+8+2, 60+8+16+8+2);
	////return rm.toString();//jsonArray.substring(jsonArray.lastIndexOf("M1-centroids-STEM"), jsonArray.lastIndexOf("M1-centroids-STEM")+4);
	//return jsonArray
}

public static String coefP() {
	PredictionWeb pw = new PredictionWeb();
	ArrayList<RegressionModel> rm = pw.regressionModels;
	ArrayList<ArrayList<String>> rr = PredictionWeb.list;
	Gson gson = new Gson();
	//gson.toJson(src);
	
	//LOG.info("%%%_%%%1"+rr.get(0).toString());
	//LOG.info("%%%_%%%1"+rr.get(1).toString());
	//LOG.info("%%%_%%%1"+rr.get(2).toString());
	//LOG.info("%%%_%%%1"+rr.get(3).toString());
	
	//LOG.info("^^^_^^^1"+rm.toString());
	
	//LOG.error("^^^_^^^1"+rm.get(1).toString());
	//LOG.error("^^^_^^^1"+rm.get(2).toString());
	//LOG.error("^^^_^^^1"+rm.get(3).toString());
	ArrayList<ArrayList<String>> wt = WebTable.coef();
	LOG.info("%%%_%%%1"+wt.get(0).toString());
	ArrayList al1 = new ArrayList();
	al1 = wt.get(0);
	//LOG.info("%%%_%%%1"+al1.get(4).toString());
	
	//LOG.info("%%%_%%%1"+wt.get(1).toString());
	//LOG.info("%%%_%%%1"+wt.get(2).toString());
	//LOG.error("%%%_%%%1"+wt.get(3).toString());
	
	//LOG.info("%%%_%%%1111111111"+wt.get(0).get(4).toString());
	//LOG.info("%%%_%%%______: "+wt.get(0).get(4).toString().substring(0, 7));
	
	//LOG.error("%%%_%%%2"+rr.get(0).get(3));
	LOG.info("+++++LA"+al1.toString().substring(53+8+16+16+2+1, 60+8+16+16+2+1));
	
	return al1.toString().substring(53+8+16+16+2+1, 60+8+16+16+2+1);
	////return rm.toString();//jsonArray.substring(jsonArray.lastIndexOf("M1-centroids-STEM"), jsonArray.lastIndexOf("M1-centroids-STEM")+4);
	//return jsonArray
}


}
