package org.webengine;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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

	private ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

	protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JFreeChart chart = null;
		String from = req.getParameter("from");
		String to = req.getParameter("to");
		String topic = req.getParameter("topic");
		String type = req.getParameter("type");
		ChartDrawer drawer = null;
		OutputStream out = resp.getOutputStream();
		list = getList();
		drawer = new ChartDrawer(list, type, from, to, topic);
		chart = drawer.getChart();
		resp.setContentType("image/png");
		ChartUtilities.writeChartAsPNG(out, chart, 640, 480);
		out.close();
	}

	private ArrayList<ArrayList<String>> getList() {
		WebTable table = new PredictionWeb();
		table.readDBfiltered("All", "All");
		ArrayList<ArrayList<String>> list = PredictionWeb.list;
		System.out.println("List created...");
		return list;
	}

}
