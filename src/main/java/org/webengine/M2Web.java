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
import org.einclusion.frontend.RegressionModel;

public class M2Web extends WebTable {
	private static final long serialVersionUID = 1002L;
	// Logger for M2Table
	private static final Logger LOG = Logger.getLogger(M2Web.class);
	// list of students with specific values
	public static ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
	static final String[] COLUMNS = { "PHONE", "TOPIC", "NAME", "SWL", "SAL", "ELM", "ELE", "IWS", "SUBMITDATE", "M2" };
	static File path; // path to exported file
	Connection conn = null;
	PreparedStatement pStmt = null;
	ArrayList<RegressionModel> regressionModels = new ArrayList<>();

	public M2Web() {
		LOG.info("M2Table has been initialized");
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

			prepareRegressionModels(); // connects to DB, reads MODELMANAGER
										// table.

			colName = getShortForColumn(colName);
			String sql;
			if (colName.equals("*") || value.equals("All")) {
				sql = "SELECT * FROM " + DB_TABLE_NAME + " ORDER BY NAME";
				pStmt = conn.prepareStatement(sql);
			} else if (colName.equals("M2")
					&& (value.equals("Green") || value.equals("Orange") || value.equals("Red"))) {
				sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + colName + " > ";
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
				sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + colName + " = ? ORDER BY NAME";
				pStmt = conn.prepareStatement(sql);
				if (colName.equals("NAME") || colName.equals("TOPIC") || colName.equals("SUBMITDATE"))
					pStmt.setString(1, value);
				else
					pStmt.setDouble(1, Double.parseDouble(value));
			}
			ResultSet rs = pStmt.executeQuery();
			conn.commit();

			list.clear(); // clears the arraylist
			while (rs.next()) {
				String phone = rs.getString("PHONE");
				String topic = rs.getString("TOPIC");
				String name = rs.getString("NAME");
				Date dateStamp = new Date(rs.getTimestamp("SUBMITDATE").getTime());
				String date = dateStamp.toString();
				String swl = String.format(Locale.US, "%.2f", rs.getDouble("SWL"));
				String sal = String.format(Locale.US, "%.2f", rs.getDouble("SAL"));
				String elm = String.format(Locale.US, "%.2f", rs.getDouble("ELM"));
				String ele = String.format(Locale.US, "%.2f", rs.getDouble("ELE"));
				String iws = String.format(Locale.US, "%.2f", rs.getDouble("IWS"));
				String m2 = rs.getString("M2");

				ArrayList<String> row = new ArrayList<String>();
				row.add(phone);
				row.add(topic);
				row.add(name);
				row.add(swl);
				row.add(sal);
				row.add(elm);
				row.add(ele);
				row.add(iws);
				row.add(date);
				row.add(round(Double.parseDouble(m2), 2) + "");

				if (row.size() > 0)
					list.add(row);

				String regressionModel = getRegressionModel(topic);
				for (RegressionModel rm : regressionModels) {
					if (rm.key.equals(regressionModel)) {
						swl = rm.hasCoefficientValue("SWL") ? swl : "";
						elm = rm.hasCoefficientValue("ELM") ? elm : "";
						ele = rm.hasCoefficientValue("ELE") ? ele : "";
						iws = rm.hasCoefficientValue("IWS") ? iws : "";
						sal = rm.hasCoefficientValue("SAL") ? sal : "";
					}
				}
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

	/**
	 * Reads MODELMANAGER table from database and saves, formats its elements in
	 * static variable RegressionModel ArrayList. Database connection must be
	 * opened beforehand.
	 */
	public void prepareRegressionModels() {
		try {
			String sql = "SELECT * FROM " + DB_REGRESSION_TABLE + " WHERE KEY LIKE 'M2-%'";
			pStmt = conn.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();
			conn.commit();

			regressionModels.clear();
			while (rs.next()) {
				String key = rs.getString("key");
				String value = rs.getString("value");
				regressionModels.add(new RegressionModel(key, value));
			}

			LOG.info("Regression models prepared");

		} catch (SQLException sqle) { // Handle errors for JDBC
			LOG.error(sqle.getMessage() + " " + sqle.getCause());
			sqle.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			LOG.error(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
		}
	}

	/**
	 * Returns the correct regression model name of the given course.
	 * 
	 * @param course
	 *            (String) - name of course
	 * @return (String) if model for given course found, NULL if course not
	 *         recognized.
	 */
	public String getRegressionModel(String course) {
		String regressionModel = null;
		regressionModel = "M2-" + course;
		return regressionModel;
	}
	
	public ArrayList<String> returnLabels() {
		ArrayList<String> labels = new ArrayList<>();
		labels.add("Nr");
		labels.add("Phone");
		labels.add("Topic");
		labels.add("Name");
		labels.add("Motivation");
		labels.add("Learning ability");
		labels.add("E-materials");
		labels.add("E-environment");
		labels.add("Instructor");
		labels.add("Submit date");
		labels.add("M2");
		return labels;
	}
}
