package org.einclusion.GUI;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import java.awt.Font;

/**
 *	This class creates a unique JFrame.
 *	@author student
 */

public class AppFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 999L;

	CardLayout cardLayout = new CardLayout();
	JPanel cardPanel;
	JMenuBar menuBar;
	JButton editDatabase, m1, m2, m3, prediction;
	JPanel panel;
	
	/**
	 *	Constructor for initializing JFrame
	 */
	public AppFrame() {
		setTitle("Einclusion");							// sets title of the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// sets defaultclose opration
		getContentPane().setLayout(new BorderLayout()); // adds layout to JFrame
		setMinimumSize(new Dimension(1000, 500)); 		// sets the minimum size of the frame
		setLocationRelativeTo(this);					// sets JFrame location middle of screen

		editDatabase = new JButton("Edit Database"); 	// creates a JButton for switching between JPanels
		editDatabase.setFont(new Font("Arial", Font.PLAIN, 11)); // sets Font for JButton
		editDatabase.addActionListener(this);			// adds an action listener to JButton
		m1 = new JButton("   M1   "); 					// creates a JButton for switching between JPanels
		m1.setFont(new Font("Arial", Font.PLAIN, 11));	// sets Font for JButton
		m1.setToolTipText("General evaluation of student"); 	// sets tooltip text for JButton
		m1.addActionListener(this);						// adds an action listener to JButton
		m2 = new JButton("   M2   "); 					// creates a JButton for switching between JPanels
		m2.setFont(new Font("Arial", Font.PLAIN, 11));	// sets Font for JButton
		m2.setToolTipText("Evaluation of ability to learn and knowledge sharing"); 	// sets tooltip text for JButton
		m2.addActionListener(this);						// adds an action listener to JButton
		m3 = new JButton("   M3   "); 					// creates a JButton for switching between JPanels
		m3.setFont(new Font("Arial", Font.PLAIN, 11));	// sets Font for JButton
		m3.setToolTipText("Evaluation of knowledge flow"); 	// sets tooltip text for JButton
		m3.addActionListener(this);						// adds an action listener to JButton
		prediction = new JButton("Prediction"); 		// creates a JButton for switching between JPanels
		prediction.setFont(new Font("Arial", Font.PLAIN, 11));	// sets Font for JButton
		prediction.addActionListener(this);						// adds an action listener to JButton

		menuBar = new JMenuBar(); 						// creates a new JMenuBar for JFrame
		menuBar.add(editDatabase);						// adds a JButton to JMenuBar
		menuBar.add(m1);								// adds a JButton to JMenuBar
		menuBar.add(m2);								// adds a JButton to JMenuBar
		menuBar.add(m3);								// adds a JButton to JMenuBar
		menuBar.add(prediction);						// adds a JButton to JMenuBar
		this.setJMenuBar(menuBar);						// sets JFrames JMenuBar

		cardPanel = new JPanel(); 						// creates a JPanel with cardLayout (to switch between panels)
		cardPanel.setLayout(cardLayout);				// sets JPanels layout to CardLayout
		EditDatabasePanel editDatabase = new EditDatabasePanel(); 		// creates new EditDatabasePanel
		M1Table m1Table = new M1Table();
		M2Table m2Table = new M2Table();								// creates new M3Table
		M3Table m3Table = new M3Table();								// creates new M3Table
		PredictionTable predictionTable = new PredictionTable();		// creates new PredictionTable
		cardPanel.add(editDatabase, "database");		// adds EditDatabasePanel to cardpanel with id "database"
		cardPanel.add(m1Table, "m1");					// adds m1Table to cardPanel with id "m1"
		cardPanel.add(m2Table, "m2");					// adds m2Table to cardpanel with id "m2"
		cardPanel.add(m3Table, "m3");					// adds m3Table to cardpanel with id "m3"
		cardPanel.add(predictionTable, "prediction");	// adds predictionTable to cardpanel with id "prediction"
		cardPanel.setVisible(false);					// JPanel is not visible
		this.add(cardPanel);							// adds JPanel to JFrame

		pack();											// packs all of JFrames components
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(editDatabase)) { 		// if editDatabase button is pressed
			cardPanel.setVisible(true);
			cardLayout.show(cardPanel, "database");		// shows EditDatabasePanel
		} else if( e.getSource().equals(m1) ){
			cardPanel.setVisible(true);
			cardLayout.show(cardPanel, "m1"); 			// shows m1Table
		} else if ( e.getSource().equals(m2) ){
			cardPanel.setVisible(true);
			cardLayout.show(cardPanel, "m2"); 			// shows m2Table
		} else if( e.getSource().equals(m3) ){
			cardPanel.setVisible(true);
			cardLayout.show(cardPanel, "m3"); 			// shows m3Table
		} else if ( e.getSource().equals(prediction) ){
			cardPanel.setVisible(true);
			cardLayout.show(cardPanel, "prediction");	// shows PredictionTable
		}
	}
}