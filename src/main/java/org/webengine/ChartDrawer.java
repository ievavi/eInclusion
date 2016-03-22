package org.webengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartDrawer {

	private ArrayList<ArrayList<String>> list;
	private static JFreeChart firstChart;
	private static JFreeChart secondChart;
	
	
	public ChartDrawer(ArrayList<ArrayList<String>> list) {
		this.list = list;
	}
	

	private synchronized void drawFirstChart() {
		int[] studentCount = {0,0,0,0};
		
		for(ArrayList<String> i: list) {
			double einclusion = Double.parseDouble(i.get(4));
			if(i.get(5).equals("-1") && i.get(6).equals("-1")) {
				continue;
			}
			else if(i.get(5).equals("-1")) {
				einclusion += (Double.parseDouble(i.get(6))/50);
			}
			else if(i.get(6).equals("-1")) {
				einclusion += (Double.parseDouble(i.get(5))/50);
			}
			einclusion = einclusion*25;
			if(einclusion > 0 && einclusion <=25) {
				studentCount[0]++;
			}
			else if(einclusion > 25 && einclusion <=50) {
				studentCount[1]++;
			}
			else if(einclusion > 50 && einclusion <=75) {
				studentCount[2]++;
			}
			else if(einclusion > 75 && einclusion <=100) {
				studentCount[3]++;
			}
		}
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		final String columnKey = "E-inclusion";
		System.out.println("Generating Chart...");
		dataset.addValue(studentCount[0], "25%", columnKey);
		dataset.addValue(studentCount[1], "50%", columnKey);
		dataset.addValue(studentCount[2], "75%", columnKey);
		dataset.addValue(studentCount[3], "100%", columnKey);
		firstChart = ChartFactory.createBarChart("", "e-inclusion", "Score", dataset,
				PlotOrientation.HORIZONTAL, true, true, false);
		System.out.println("Chart generated...");
	}
	
	private synchronized void drawSecondChart() {
		TreeSet <String> topicsSet = new TreeSet<String>();
		for(ArrayList<String> i: list) {
			topicsSet.add(i.get(1));
		}
		String[] topics = topicsSet.toArray(new String[topicsSet.size()]);
		int[] studentCount = new int[topicsSet.size()];
		for(int i=0; i<studentCount.length; i++) {
			studentCount[i] = 0;
		}
		for(ArrayList<String> row: list) {
			for(int i=0; i<topicsSet.size(); i++) {
				if(row.get(1).equals(topics[i])) {
					studentCount[i]++;
				}
				
			}
		}
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		final String columnKey = "Risk factor";
		System.out.println("Generating Chart...");
		for(int i=0; i<topics.length; i++) {
			dataset.addValue(studentCount[i], topics[i], "");
		}
		secondChart = ChartFactory.createBarChart("Risk factor", "", "Students", dataset,
				PlotOrientation.VERTICAL, true, true, false);
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

}
