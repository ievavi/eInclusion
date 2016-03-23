package org.webengine;


import java.awt.Font;
import java.util.ArrayList;
import java.util.TreeSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartDrawer {

	private ArrayList<ArrayList<String>> list;
	private static JFreeChart chart = null;
	private String from;
	private String to;
	private String type;
	private String chosenTopic;

	public ChartDrawer(ArrayList<ArrayList<String>> list, String type, String from, String to, String chosenTopic) {
		this.list = list;
		this.chosenTopic = chosenTopic;
		this.type = type;
		if (from.compareTo(to) > 0) {
			String temp = from;
			from = to;
			to = temp;
		}
		this.from = from;
		this.to = to;
	}

	public synchronized JFreeChart getChart() {
		if (type.equals("image1")) {
			drawFirstChart();
			return chart;
		}
		if (type.equals("image2")) {
			drawSecondChart();
			return chart;
		}
		return chart;
	}

	private synchronized void drawFirstChart() {
		int[] studentCount = { 0, 0, 0, 0 };
		double m;
		for (ArrayList<String> i : list) {
			double m1 = Double.parseDouble(i.get(4));
			double m2 = Double.parseDouble(i.get(5));
			double m3 = Double.parseDouble(i.get(6));
			double einclusion = m1;
			String date = i.get(3);
			String topic = i.get(1);
			boolean topicCheck = topic.equals(chosenTopic) || chosenTopic.equals("All");
			if (date.compareTo(from) >= 0 && date.compareTo(to) <= 0 && topicCheck) {
				if (m2 == -1.0 && m3 == -1.0) {
					continue;
				}
				if (m2 == -1.0 && m3 > 0) {
					m = m3 / 50;
					einclusion = m1 + m;
				}
				if (m3 == -1.0 && m2 > 0) {
					m = m2 / 50;
					einclusion = m + m1;
				}
				einclusion = einclusion * 25;
				if (einclusion <= 25) {
					studentCount[0]++;
				}
				if (einclusion > 25 && einclusion <= 50) {
					studentCount[1]++;
				}
				if (einclusion > 50 && einclusion <= 75) {
					studentCount[2]++;
				}
				if (einclusion > 75 && einclusion <= 1000) {
					studentCount[3]++;
				}
			}
		}
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		System.out.println("Generating Chart...");
		dataset.addValue(studentCount[0], "0-25%", "");
		dataset.addValue(studentCount[1], "26-50%", "");
		dataset.addValue(studentCount[2], "51-75%", "");
		dataset.addValue(studentCount[3], "76-100%", "");
		String description = "Report for dates: " + from + " to " + to + "\nand topic: " + chosenTopic;
		chart = ChartFactory.createBarChart(description, "e-inclusion %", "Number of students", dataset,
				PlotOrientation.HORIZONTAL, true, false, false);
		customize();
		System.out.println("Chart generated...");
	}

	private synchronized void drawSecondChart() {
		TreeSet<String> topicsSet = new TreeSet<String>();
		for (ArrayList<String> i : list) {
			topicsSet.add(i.get(1));
		}
		String[] topics = topicsSet.toArray(new String[topicsSet.size()]);
		int[] studentCount = new int[topicsSet.size()];
		for (int i = 0; i < studentCount.length; i++) {
			studentCount[i] = 0;
		}
		for (ArrayList<String> row : list) {
			for (int i = 0; i < topicsSet.size(); i++) {
				if (row.get(1).equals(topics[i])) {
					studentCount[i]++;
				}

			}
		}
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		System.out.println("Generating Chart...");
		for (int i = 0; i < topics.length; i++) {
			dataset.addValue(studentCount[i], topics[i], "");
		}
		chart = ChartFactory.createBarChart("*UNFINISHED*", "", "Students", dataset, PlotOrientation.VERTICAL, true,
				true, false);
		System.out.println("Chart generated...");
	}

	private void customize() {
		final CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        BarRenderer br = (BarRenderer) plot.getRenderer();
        br.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        br.setBaseItemLabelsVisible(true);
        Font font = new Font("Arial", Font.PLAIN, 16);
        br.setBaseItemLabelFont(font);
	}
}
