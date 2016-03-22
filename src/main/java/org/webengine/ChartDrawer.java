package org.webengine;

import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartDrawer {

	private ArrayList<ArrayList<String>> list;
	private static int[] studentCount = new int[4];
	private static JFreeChart firstChart;
	private static JFreeChart secondChart;
	
	
	public ChartDrawer(ArrayList<ArrayList<String>> list) {
		this.list = list;
		studentCount[0] = 0;
		studentCount[1] = 0;
		studentCount[2] = 0;
		studentCount[3] = 0;
	}
	

	private synchronized void drawFirstChart() {
		int field = 4;
		prepare(field);
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		final String columnKey = String.valueOf(field);
		System.out.println("Generating Chart...");
		dataset.addValue(studentCount[0], "25%", columnKey);
		dataset.addValue(studentCount[1], "50%", columnKey);
		dataset.addValue(studentCount[2], "75%", columnKey);
		dataset.addValue(studentCount[3], "100%", columnKey);
		firstChart = ChartFactory.createBarChart("Random "+field, "Y Axis", "Score", dataset,
				PlotOrientation.HORIZONTAL, true, true, false);
		System.out.println("Chart generated...");
	}
	
	private synchronized void drawSecondChart() {
		int field = 11;
		prepare(field);
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		final String columnKey = String.valueOf(field);
		System.out.println("Generating Chart...");
		dataset.addValue(studentCount[0], "25%", columnKey);
		dataset.addValue(studentCount[1], "50%", columnKey);
		dataset.addValue(studentCount[2], "75%", columnKey);
		dataset.addValue(studentCount[3], "100%", columnKey);
		secondChart = ChartFactory.createBarChart("Random "+field, "Y Axis", "Score", dataset,
				PlotOrientation.HORIZONTAL, true, true, false);
		System.out.println("Chart generated...");
	}
	
	public JFreeChart getFirstChart() {
		drawFirstChart();
		return firstChart;
	}
	
	public JFreeChart getSecondChart() {
		drawSecondChart();
		return secondChart;
	}
	
	public synchronized void prepare(int field) {
		float num;
		for (ArrayList<String> row : list) {
			num = Float.parseFloat(row.get(field));
			if (num <= 0) {
				studentCount[0]++;
			}
			if (num > 0 && num <= 1) {
				studentCount[1]++;
			}
			if (num > 1 && num <= 2) {
				studentCount[2]++;
			}
			if (num > 2 && num <= 100) {
				studentCount[3]++;
			}
		}
	}

}
