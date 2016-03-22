package org.webengine;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

@WebServlet("/report.jsp")
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = -5564406414973334671L;

	static final String JDBC_DRIVER = "org.h2.Driver"; // JDBC driver name
	// database URL (location of database)
	static final String DB_URL = "jdbc:h2:data/Student;AUTO_SERVER=TRUE";
	static final String USER = "sa"; // user name for database
	static final String PASS = ""; // password for database
	static final String DB_TABLE_NAME = "STUDENT"; // database for student data
	// database for function data path to exported file
	static final String DB_REGRESSION_TABLE = "MODELMANAGER";
	Connection conn = null;
	PreparedStatement pStmt = null;
	private ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
	

	protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JFreeChart chart = null;
		ChartDrawer drawer = null;
		OutputStream out = resp.getOutputStream();
        list = getList();
            if(req.getParameter("type").equals("image1")) {
            	drawer = new ChartDrawer(list);
            	chart = drawer.getFirstChart();
            }
            if(req.getParameter("type").equals("image2")) {
            	drawer = new ChartDrawer(list);
            	chart = drawer.getSecondChart();
                }
		resp.setContentType("image/png");
		ChartUtilities.writeChartAsPNG(out, chart, 640, 480);
	}

	
	private ArrayList<ArrayList<String>> getList() {
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			conn.setAutoCommit(false);
			String sql = "SELECT * FROM " + DB_TABLE_NAME + " ORDER BY NAME";
			pStmt = conn.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();
            conn.commit();
            list.clear();
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
		} catch (SQLException e) {
				e.printStackTrace();
		}
		System.out.println("List created...");
		return list;
	}

}
