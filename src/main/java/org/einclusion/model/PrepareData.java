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
 * This class is used to add data from the converted .csv file to database
 * 
 * @author student
 */

public class PrepareData {
	private static final Logger LOG = Logger.getLogger(PrepareData.class);
	static final String DB_URL = "jdbc:h2:data/Student;AUTO_SERVER=TRUE";
	static final String USER = "sa"; // username for database
	static final String PASS = ""; // password for database
	static final String DB_TABLE_NAME = "STUDENT"; // default table name
	public static Connection conn = null; // connection with a database
	static final String PERSISTENCE_SET = "test"; // persistence set for
	// connecting to database

	/**
	 * Function that reads a given .csv file and writes students to database
	 * 
	 * @param file
	 *            - path to a .csv file
	 */
	public static void csv2db(File file) {
		try {
			// EditDatabasePanel.log.append("Reading from file:
			// "+file.getName()+"\n");
			conn = DriverManager.getConnection(DB_URL, USER, PASS); // establsih
			// connection to database
			conn.setAutoCommit(false); // sets autocommit to false
			// reads the file using buffered reader
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line; // a row from the file
			while ((line = br.readLine()) != null) { // while rows aren't empty
				String[] value = line.split(","); // splits the line by ,
				String PHONE = value[0];
				String NAME = value[1];
				String TOPIC = value[2];
				Double IWS = Double.parseDouble(value[11]);
				Double KLAL = Double.parseDouble(value[12]);
				Double KLBL = Double.parseDouble(value[13]);
				Double PU = Double.parseDouble(value[14]);
				String SUBMITDATE = value[15];
				Double SWL = (Double.parseDouble(value[3]) + Double.parseDouble(value[4])) / 2; // calculates
																								// SWL
				Double DS = (Double.parseDouble(value[5]) + Double.parseDouble(value[6])) / 2; // calculates
																								// DS
				Double ELM = (Double.parseDouble(value[7]) + Double.parseDouble(value[8])) / 2; // calculates
																								// ELM
				Double ELE = (Double.parseDouble(value[9]) + Double.parseDouble(value[10])) / 2; // calculates
																									// ELE
				Double SAL = (double) 0; // creates SAL
				if (5 - KLBL == 0) { // if student cant learning anything new
										// (MAX - KLBL)
					SAL = (double) 0; // SAL is 0
				} else { // else if student is able to learn smth new
					if (KLAL > KLBL)
						SAL = (KLAL - KLBL) * 4 / (5 - KLBL);
					else
						SAL = (KLBL - KLAL) * 4 / (5 - KLAL);
				}
				SAL++; // so SAL max value is 5 and lowest value is 1
				Double KFA = ((IWS + ELE + ELM) * KLBL) / 3;

				String sql = "UPDATE STUDENT" + " SET NAME=?, IWS=?, KLAL=?, KLBL=?, PU =?,"
						+ " SUBMITDATE=?, SWL=?, DS=?, ELM=?, ELE=?,"
						+ " SAL=?, PUOU=?, M1=?, M2=?, KFA=?, M3=?, RELIABILITY=?, OU=?" + " WHERE PHONE=? and TOPIC=?";
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
				// LOG.debug(pst.);
				pst.executeUpdate();
				conn.commit();

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

			}
			br.close(); // closes buffered reader
			conn.close();
		} catch (FileNotFoundException fnfe) {
			LOG.error(fnfe.getMessage() + " " + fnfe.getCause());
			fnfe.printStackTrace();
			String errorText = "File not found choose a different path!";
			EditDatabasePanel.log.append(errorText + "\n");
			EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
		} catch (IOException ioe) {
			LOG.error(ioe.getMessage() + " " + ioe.getCause());
			ioe.printStackTrace();
			String errorText = "Exception while writing to file!";
			EditDatabasePanel.log.append(errorText + "\n");
			EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	public static void updatePrediction() {
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			conn.setAutoCommit(false); // sets autocommit to false

			String sql = "SELECT DISTINCT TOPIC FROM STUDENT where OU IS NULL";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			conn.commit();

			while (rs.next()) { // while table has contents

				String topic = rs.getString("TOPIC");
				// Prediction should be called for topics which have OU values
				// with NULL.
				// FIXME : Should update SQL to exclude topics which don't have
				// coefficients
				try {
					Prediction.getPrediction(topic);
				} catch (Throwable t) {
					LOG.error(t.getMessage() + " " + t.getCause());
					t.printStackTrace();
				}

			}
			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // establsih

	}

	public static void generateModels() {
		try {

			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			conn.setAutoCommit(false); // sets autocommit to false
			LOG.info("Generating models\n");

			String sql = "SELECT DISTINCT TOPIC FROM STUDENT where OU IS NOT NULL";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			conn.commit();
			// TOFIX: There were some memory leaks when testing from virtualbox
			// if model manager was initialized here.
			// Model manager should be initialized for Instance manager to work
			// properly, necessary for model generation

			// ModelManager.initModelManager(PERSISTENCE_SET); // loads

			while (rs.next()) { // while table has contents

				String topic = rs.getString("TOPIC");
				// If one type of model generation fails, next one should still
				// be generated
				try {
					M1.getCluster(topic, "M1-clusters-" + topic, "M1-centroids-" + topic);
				} catch (Throwable t) {
					LOG.error(t.getMessage() + " " + t.getCause());
					t.printStackTrace();
				}
				try {
					M2.getRegression(topic, "M2-" + topic);
				} catch (Throwable t) {
					LOG.error(t.getMessage() + " " + t.getCause());
					t.printStackTrace();
				}
				try {
					M3.getRegression(topic, "M3-" + topic);
				} catch (Throwable t) {
					LOG.error(t.getMessage() + " " + t.getCause());
					t.printStackTrace();
				}

			}
			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// ModelManager.closeModelManager(); // closes
			// connection
			LOG.info("Finished generating models\n");
		} //

	}

}
