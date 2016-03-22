package org.einclusion.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.einclusion.GUI.EditDatabasePanel;

import static org.einclusion.model.ModelManager.*;

/**
 * 	This class is used to add data from the converted .csv file to database
 * 	@author student
 */

public class PrepareData {
	private static final Logger LOG = Logger.getLogger(PrepareData.class);
	static final String DB_URL = "jdbc:h2:data/Student;AUTO_SERVER=TRUE";
	static final String USER = "sa"; // username for database
	static final String PASS = ""; // password for database
	static final String DB_TABLE_NAME = "STUDENT"; // default table name
	public static Connection conn = null; // connection with a database

	/**
	 * 	Function that reads a given .csv file and writes students to database
	 * 	@param file - path to a .csv file
	 */
	public static void csv2db(File file) {
		try
		{
			//EditDatabasePanel.log.append("Reading from file: "+file.getName()+"\n");
			conn = DriverManager.getConnection(DB_URL, USER, PASS); // establsih
			// connection
			// to
			// database
			conn.setAutoCommit(false); // sets autocommit to false

			//Query q;
			//transaction.begin();	// starts the transaction
			BufferedReader br = new BufferedReader(new FileReader(file));					// reads the file using buffered reader
			String line;																	// a row from the file
			while ((line = br.readLine()) != null) {										// while rows aren't empty
				String[] value = line.split(",");											// splits the line by ,
				String PHONE = value[0];
				String NAME = value[1];
				String TOPIC = value[2];
				Double IWS = Double.parseDouble(value[11]);
				Double KLAL = Double.parseDouble(value[12]);
				Double KLBL = Double.parseDouble(value[13]);
				Double PU = Double.parseDouble(value[14]);
				String SUBMITDATE = value[15];
				Double SWL = (Double.parseDouble(value[3]) + Double.parseDouble(value[4])) / 2;	// calculates SWL
				Double DS = (Double.parseDouble(value[5]) + Double.parseDouble(value[6])) / 2;	// calculates DS
				Double ELM = (Double.parseDouble(value[7]) + Double.parseDouble(value[8])) / 2;	// calculates ELM
				Double ELE = (Double.parseDouble(value[9]) + Double.parseDouble(value[10])) / 2;	// calculates ELE
				Double SAL = (double) 0;													    // creates SAL
				if (5 - KLBL == 0) {						// if student cant learning anything new (MAX - KLBL)
					SAL = (double) 0;						// SAL is 0
				} else {									// else if student is able to learn smth new
					if(KLAL > KLBL)
						SAL = (KLAL - KLBL) * 4 / (5 - KLBL);
					else
						SAL = (KLBL - KLAL) * 4 / (5 - KLAL);
				}
				SAL++;										// so SAL max value is 5 and lowest value is 1
				Double KFA = ((IWS + ELE + ELM) * KLBL) / 3;
				
				// creates a statement that will add one student with the selected values into the database
				//String used with entity manager
				
				//String sql = "UPDATE STUDENT"
				//		+ " SET NAME=:name, IWS=:iws, KLAL=:klal, KLBL=:klbl, PU = :pu,"
				//		+ " SUBMITDATE=:submitdate, SWL=:swl, DS=:ds, ELM=:elm, ELE=:ele,"
				//		+ " SAL=:sal, PUOU=:puou, M1=:m1, M2=:m2, KFA=:kfa, M3=:m3, RELIABILITY=:reliability"
				//		+ " WHERE PHONE=:phone and TOPIC=:topic";
				String sql = "UPDATE STUDENT"
						+ " SET NAME=?, IWS=?, KLAL=?, KLBL=?, PU =?,"
						+ " SUBMITDATE=?, SWL=?, DS=?, ELM=?, ELE=?,"
						+ " SAL=?, PUOU=?, M1=?, M2=?, KFA=?, M3=?, RELIABILITY=?, OU=?"
						+ " WHERE PHONE=? and TOPIC=?";
				PreparedStatement pst = conn.prepareStatement(sql);
				pst.setString(1, NAME);
				pst.setDouble(2, IWS);
				pst.setDouble(3, KLAL);
				pst.setDouble(4, KLBL);
				pst.setDouble(5, PU);
				pst.setDate(6, Date.valueOf(SUBMITDATE));
				pst.setDouble(7, SWL);
				pst.setDouble(8, DS);
				pst.setDouble(9, ELM);
				pst.setDouble(10, ELE);
				pst.setDouble(11, SAL);
				pst.setDouble(12, 0);
				pst.setInt(13, -1);
				pst.setDouble(14, -1);
				pst.setDouble(15, KFA);
				pst.setDouble(16, -1);
				pst.setString(17, "not available");
				pst.setNull(18, java.sql.Types.INTEGER);
				pst.setString(19, PHONE);
				pst.setString(20, TOPIC);
				//LOG.debug(pst.);
				pst.executeUpdate();
				conn.commit();
				//q = entityManager.createNativeQuery(sql);
				//q.setParameter("name", NAME);
				//q.setParameter("iws", IWS);
				//q.setParameter("klal", KLAL);
				//q.setParameter("klbl", KLBL);
				//q.setParameter("pu", PU);
				//q.setParameter("submitdate", submitDate);
				//q.setParameter("swl", SWL);
				//q.setParameter("ds", DS);
				//q.setParameter("elm", ELM);
				//q.setParameter("ele", ELE);
				//q.setParameter("sal", SAL);
				//q.setParameter("puou", 0);
				//q.setParameter("m1", -1);
				//q.setParameter("m2", -1);
				//q.setParameter("kfa", KFA);
				//q.setParameter("m3", -1);
				//q.setParameter("reliability", "not available");
				//q.setParameter("phone", PHONE);
				//q.setParameter("topic", TOPIC);
				//q.executeUpdate();	// executes q statement
				

				//sql = "INSERT INTO STUDENT (Phone,Name,Topic,IWS,KLAL,KLBL,PU,SubmitDate,SWL,DS,"
				//		+ "ELM,ELE,SAL,PUOU,M1,M2,KFA, M3, RELIABILITY)"
				//		+ " SELECT :phone, :name, :topic, :iws, :klal, :klbl, :pu, :submitdate,"
				//		+ " :swl, :ds, :elm, :ele, :sal, :puou, :m1, :m2, :kfa, :m3, :reliability"
				//		+ " WHERE NOT EXISTS "
				//		+ " (SELECT PHONE, TOPIC from STUDENT WHERE PHONE=:phone and TOPIC=:topic)";
				
				sql = "INSERT INTO STUDENT (Phone,Name,Topic,IWS,KLAL,KLBL,PU,SubmitDate,SWL,DS,"
						+ "ELM,ELE,SAL,PUOU,M1,M2,KFA, M3, RELIABILITY)"
						+ " SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
						+ " WHERE NOT EXISTS (SELECT PHONE, TOPIC from STUDENT WHERE PHONE=? and TOPIC=?)";
				pst = conn.prepareStatement(sql);
				pst.setString(1, PHONE);
				pst.setString(2, NAME);
				pst.setString(3, TOPIC);
				pst.setDouble(4, IWS);
				pst.setDouble(5, KLAL);
				pst.setDouble(6, KLBL);
				pst.setDouble(7, PU);
				pst.setDate(8, Date.valueOf(SUBMITDATE));
				pst.setDouble(9, SWL);
				pst.setDouble(10, DS);
				pst.setDouble(11, ELM);
				pst.setDouble(12, ELE);
				pst.setDouble(13, SAL);
				pst.setDouble(14, 0);
				pst.setInt(15, -1);
				pst.setDouble(16, -1);
				pst.setDouble(17, KFA);
				pst.setDouble(18, -1);
				pst.setString(19, "not available");
				pst.setString(20, PHONE);
				pst.setString(21, TOPIC);
				pst.executeUpdate();
				conn.commit();
				
				//q = entityManager.createNativeQuery(sql);
				//q.setParameter("name", NAME);
				//q.setParameter("iws", IWS);
				//q.setParameter("klal", KLAL);
				//q.setParameter("klbl", KLBL);
				//q.setParameter("pu", PU);
				//q.setParameter("submitdate", submitDate);
				//q.setParameter("swl", SWL);
				//q.setParameter("ds", DS);
				//q.setParameter("elm", ELM);
				//q.setParameter("ele", ELE);
				//q.setParameter("sal", SAL);
				//q.setParameter("puou", 0);
				//q.setParameter("m1", -1);
				//q.setParameter("m2", -1);
				//q.setParameter("kfa", KFA);
				//q.setParameter("m3", -1);
				//q.setParameter("reliability", "not available");
				//q.setParameter("phone", PHONE);
				//q.setParameter("topic", TOPIC);
				//q.executeUpdate();	// executes q statement
				
				
				
				
			//  sql = "INSERT INTO STUDENT (Phone,Name,Topic,IWS,KLAL,KLBL,PU,SubmitDate,SWL,DS,ELM,ELE,SAL,PUOU,M1,M2,KFA, M3, RELIABILITY)"
			//  		+ " SELECT '" + PHONE + "','" + NAME + "','" + TOPIC + "','" + IWS + "','" + KLAL + "','"
			//  		+ KLBL + "','" + PU + "','" + submitDate + "','" + SWL + "','" + DS + "','" + ELM + "','"
			//  		+ ELE + "','" + SAL + "', 0,-1,-1,'"+KFA+"', -1, 'not available' WHERE NOT EXISTS (SELECT PHONE,TOPIC FROM STUDENT WHERE PHONE='" + PHONE
			//  		+ "' " + "AND TOPIC=" + " '" + TOPIC + "')";
			//  q = entityManager.createNativeQuery(sql);
			//  q.executeUpdate();	// executes q statement
			}
			br.close();				// closes buffered reader
			//transaction.commit();	// ends the transaction
		} catch( FileNotFoundException fnfe ){
			LOG.error(fnfe.getMessage()+" "+fnfe.getCause());
			fnfe.printStackTrace();
			String errorText = "File not found choose a different path!";
			EditDatabasePanel.log.append(errorText+"\n");
			EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
		} catch( IOException ioe ){
			LOG.error(ioe.getMessage()+" "+ioe.getCause());
			ioe.printStackTrace();
			String errorText = "Exception while writing to file!";
			EditDatabasePanel.log.append(errorText+"\n");
			EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void calculateStudentCoeficients(){
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			conn.setAutoCommit(false); // sets autocommit to false

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // establsih
	
	}
	
	public static void updateModels(){
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			conn.setAutoCommit(false); // sets autocommit to false

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // establsih
	
	}

	
}
