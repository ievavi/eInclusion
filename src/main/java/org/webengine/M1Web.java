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

public class M1Web extends WebTable {
	private static final long serialVersionUID = 1001L;
	// Logger for M1Table
	private static final Logger LOG = Logger.getLogger(M1Web.class);
	// list for student data with COLUMNS
	public static ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
	static final String[] COLUMNS = { "PHONE", "TOPIC", "NAME", "SWL", "DS", "SAL", "ELM", "IWS", "ELE", "PU",
			"SUBMITDATE", "M1" };
	static File path;
	Connection conn = null;
	PreparedStatement pStmt = null;

	public M1Web() {
		LOG.info("M1Table has been initialized");
	}

	/**
	 * Reads specific filtered data from database.
	 * 
	 * @param colName
	 *            - column name
	 * @param value
	 *            - row value in selected column
	 */
	public void readDBfiltered(String colName, String value) {

		try {
			Class.forName(JDBC_DRIVER);
			LOG.info("Generating filter from database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			conn.setAutoCommit(false);

			colName = getShortForColumn(colName);
			String sql;
			sql = "SELECT * FROM " + DB_TABLE_NAME + " ORDER BY NAME";
			pStmt = conn.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();
			conn.commit();

			list.clear(); // clears the arraylist

			while (rs.next()) {

				String phone = rs.getString("PHONE");
				String topic = rs.getString("TOPIC");
				String name = rs.getString("NAME");
				String swl = String.format(Locale.US, "%.2f", rs.getDouble("SWL"));
				String ds = String.format(Locale.US, "%.2f", rs.getDouble("DS"));
				String sal = String.format(Locale.US, "%.2f", rs.getDouble("SAL"));
				String elm = String.format(Locale.US, "%.2f", rs.getDouble("ELM"));
				String iws = String.format(Locale.US, "%.2f", rs.getDouble("IWS"));
				String ele = String.format(Locale.US, "%.2f", rs.getDouble("ELE"));
				String pu = String.format(Locale.US, "%.2f", rs.getDouble("PU"));
				Date dateStamp = new Date(rs.getTimestamp("SUBMITDATE").getTime());
				String date = dateStamp.toString();
				String m1 = rs.getString("M1");

				ArrayList<String> row = new ArrayList<String>();
				row.add(phone);
				row.add(topic);
				row.add(name);
				row.add(swl);
				row.add(ds);
				row.add(sal);
				row.add(elm);
				row.add(iws);
				row.add(ele);
				row.add(pu);
				row.add(date);
				row.add(m1);

				if (row.size() > 0)
					list.add(row);

			}
			LOG.info("Filter generated successfully");

		} catch (SQLException sqle) { // Handle errors for JDBC
			LOG.error(sqle.getMessage() + " " + sqle.getCause());
			sqle.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			LOG.error(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
		} finally {
			try {
				if (pStmt != null)
					conn.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOG.error(sqle.getMessage() + " " + sqle.getCause());
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
		labels.add("Motivation");
		labels.add("Digital skills");
		labels.add("Learning ability");
		labels.add("E-materials");
		labels.add("Instructor");
		labels.add("E-environment");
		labels.add("Predicted usage");
		labels.add("Submit date");
		labels.add("M1");
		return labels;
	}

}
