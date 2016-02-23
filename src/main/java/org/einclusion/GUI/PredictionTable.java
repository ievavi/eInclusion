package org.einclusion.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.einclusion.frontend.RegressionModel;

public class PredictionTable extends JPanel implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1004L;
	private static final Logger LOG = Logger.getLogger(PredictionTable.class);	// Logger for PredictionTable
	static final String JDBC_DRIVER = "org.h2.Driver";		// JDBC driver name
	static final String DB_URL = "jdbc:h2:data/Student";	// databse URL (location of database)
	static final String USER = "sa";						// username for database
	static final String PASS = "";							// password for database
    static final String DB_TABLE_NAME = "STUDENT";			// database for student data
    static final String DB_REGRESSION_TABLE = "MODELMANAGER";	// database for function data
    public static ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
	static final String[] COLUMNS = {"PHONE","TOPIC","NAME","M1","M2","M3","RELIABILITY"};
	static File path;				// path to exported file
	JButton writeToXls, openFile;
	JTextField fieldForInput;
    
	JLabel entryLabel;
	
	JTable table;
	DefaultTableModel tableModel;
	JComboBox<String> comboBox_1;
	JComboBox<String> comboBox_2;
	JButton btnApply;
	Connection conn = null;
    PreparedStatement pStmt = null;
    Statement stmt = null;
    ArrayList<RegressionModel> regressionModels = new ArrayList<>();
    
    static int entries = 1;
	
	public PredictionTable(){
		setVisible(true);
		setLayout(new BorderLayout(0, 0));				// sets JPanel layout to BorderLayout
		
		JPanel panel = new JPanel();					// creates new JPanel for User interaction
		GridBagLayout gbl_panel = new GridBagLayout();	// creates new gridbaglayout
		gbl_panel.columnWidths = new int[] { 120, 100, 100, 100, 150, 140, 100 };	// sets column widths
		gbl_panel.rowHeights = new int[] { 30, 30, 0 };								// sets row heights
		gbl_panel.columnWeights = new double[] { 0.5, 0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };	// sets column weights
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };			// sets row wights
		panel.setLayout(gbl_panel);						// adds gridbaglayout to JPanel
		
		JLabel predictionLabel = new JLabel("Prediction of student e-inclusion");
		predictionLabel.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_predictionLabel = new GridBagConstraints();	// creates gridbagconstraints
		gbc_predictionLabel.anchor = GridBagConstraints.WEST;
		gbc_predictionLabel.insets = new Insets(0, 10, 0, 0);				// sets insets for gridbagcontraints
		gbc_predictionLabel.gridwidth = 160;
		gbc_predictionLabel.gridx = 0;										// sets the row of gridbagcontraints
		gbc_predictionLabel.gridy = 0;										// sets the column for gridbagconraints
		panel.add(predictionLabel, gbc_predictionLabel);
		
		entryLabel = new JLabel("Students: 0");
		entryLabel.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_EntryLabel =	new GridBagConstraints();	// creates gridbagconstraints
		gbc_EntryLabel.anchor = GridBagConstraints.CENTER;
		gbc_EntryLabel.gridx = 3;										// sets the row of gridbagcontraints
		gbc_EntryLabel.gridy = 0;										// sets the column for gridbagconraints
		panel.add(entryLabel, gbc_EntryLabel);
		
		JLabel colorLabel = new JLabel("<html><font color='rgb(103,235,103)'><b>Green</b></font> - included, <font color='rgb(255,201,14)'><b>Orange</b></font> - partly included, <font color='rgb(235,69,69)'><b>Red</b></font> - not included</html>");
		colorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		GridBagConstraints gbc_colorLabel = new GridBagConstraints();	// creates gridbagconstraints
		gbc_colorLabel.anchor = GridBagConstraints.WEST;
		gbc_colorLabel.insets = new Insets(0, 10, 0, 0);				// sets insets for gridbagcontraints
		gbc_colorLabel.gridwidth = 160;
		gbc_colorLabel.gridx = 4;										// sets the row of gridbagcontraints
		gbc_colorLabel.gridy = 0;										// sets the column for gridbagconraints
		panel.add(colorLabel, gbc_colorLabel);
		
		fieldForInput = new JTextField("Students");					// creates a JTextField for user inpu
		fieldForInput.setToolTipText("Enter a file name without the extension");	// sets tooltip
		fieldForInput.setFont(new Font("Arial", Font.BOLD, 12));	// sets font
		GridBagConstraints gbc_input =	new GridBagConstraints();	// creates gridbagconstraints
		gbc_input.fill = GridBagConstraints.HORIZONTAL;				// resizes the component to fit horizontally
		gbc_input.insets = new Insets(0, 10, 0, 10);				// sets insets for gridbagcontraints
		gbc_input.gridx = 0;										// sets the row of gridbagcontraints
		gbc_input.gridy = 1;										// sets the column for gridbagconraints
		gbc_input.ipady = 6;										// sets vertical padding for component
		fieldForInput.addKeyListener(this); 						// add keylistener to jtextfield
		panel.add(fieldForInput, gbc_input);						// adds JTextArea to JPanel with gridbagcontraints
		
		writeToXls = new JButton("Export to xls");				// creates a JButton for exporting data in table to .xls file
		GridBagConstraints gbc_export =	new GridBagConstraints();	// creates new gridbagcontraints
		writeToXls.setToolTipText("Exports filtered data to .xls file");	// sets tooltip for jbutton
		writeToXls.setFont(new Font("Arial", Font.BOLD, 12));		// sets font for jbutton
		gbc_export.anchor = GridBagConstraints.WEST;				// places the component to the left side
		gbc_export.insets = new Insets(0, 0, 0, 5);					// sets insets for gridbagcontraints
		gbc_export.gridx = 1;										// sets the row of gridbagcontraints
		gbc_export.gridy = 1;										// sets the column for gridbagconraints
		writeToXls.addActionListener(this);						// adds actionlistener to jbutton
		panel.add(writeToXls, gbc_export);							// adds jbutton to jpanel with gridbagcontraints
		
		openFile = new JButton("Open file");					// creates a new jbutton for opening file
		GridBagConstraints gbc_open =	new GridBagConstraints();	// creates new gridbagcontraints
		openFile.setToolTipText("Opens a .xls file with its default program");	// sets tooltip for jbutton
		openFile.setFont(new Font("Arial", Font.BOLD, 12));		// sets font for jbutton
		gbc_open.fill = GridBagConstraints.HORIZONTAL;		// resizes the component to fit horizontally
		gbc_open.insets = new Insets(0, 0, 0, 5);				// sets insets for gridbagcontraints
		gbc_open.gridx = 2;									// sets the row of gridbagcontraints
		gbc_open.gridy = 1;									// sets the column for gridbagconraints
		openFile.setVisible(false);								// jbutton is set to not visible
		openFile.addActionListener(this);						// adds actionlistener to jbutton
		panel.add(openFile, gbc_open);							// adds jbutton to jpanel with gridbagcontraints
		
		JLabel lblNewLabel = new JLabel("Filter:");					// creates a new jlabel
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();	// creates new gridbagcontraints
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;			// places the component to be on the right side
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);			// sets insets for gridbagcontraints
		gbc_lblNewLabel.gridx = 3;									// sets the row for gridbagcontraints
		gbc_lblNewLabel.gridy = 1;									// sets the column for gridbagcontraints
		panel.add(lblNewLabel, gbc_lblNewLabel);					// adds jlabel to jpanel with gridbag contraints

		comboBox_1 = new JComboBox<String>();						// creates a JComboBox for choosing columns names (to sort by column names)
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();	// creates gridbagconstraints
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;		// resizes the component to fit horizontally
		gbc_comboBox_1.insets = new Insets(0, 0, 0, 5);				// sets insets for gridbagcontraints
		gbc_comboBox_1.gridx = 4;									// sets row for gribagcontraints
		gbc_comboBox_1.gridy = 1;									// sets column for gridbagconstraints
		panel.add(comboBox_1, gbc_comboBox_1);						// adds JComboBox to JPanel with gridbagcontstraints

		comboBox_2 = new JComboBox<>();								// creates a JComboBox for choosing how to sort table (colors, specific values)
		GridBagConstraints gbc_comboBox_2 = new GridBagConstraints();	// creates gridbagconstraints
		gbc_comboBox_2.insets = new Insets(0, 0, 0, 5);				// sets insets for gridbagconstraints
		gbc_comboBox_2.fill = GridBagConstraints.HORIZONTAL;		// resizes the component to fit horizontally
		gbc_comboBox_2.gridx = 5;									// sets the row for gridbagcontraints
		gbc_comboBox_2.gridy = 1;									// sets the column for gridbagconstraints
		panel.add(comboBox_2, gbc_comboBox_2);						// adds JComboBox to JPanel with gridbagconstraints

		btnApply = new JButton("Apply");							// creates a JButton to apply filters
		btnApply.addActionListener(this);							// adds actionlistenr to JButton
		GridBagConstraints gbc_btnApply = new GridBagConstraints();	// creates new gridbagcontraints
		gbc_btnApply.insets = new Insets(0, 0, 0, 10);				// sets insets for gridbagcontraints
		gbc_btnApply.fill = GridBagConstraints.HORIZONTAL;			// resizes the component to fir horizontally
		gbc_btnApply.gridx = 6;										// sets the row for gridbagconstraints
		gbc_btnApply.gridy = 1;										// sets the column for gridbagconstraints
		panel.add(btnApply, gbc_btnApply);							// adds JPanel to JPanel
		
		add(panel, BorderLayout.NORTH);								// adds JPanel to JPanel
		
		tableModel = new DefaultTableModel() {						// creates a new DefaultTableModel
			private static final long serialVersionUID = 2L;
			public boolean isCellEditable(int row, int column) {	// sets all cells to not be editable
		      return false;
		    }
			@Override
		    public Class<?> getColumnClass(int columnIndex) {		// returns columns class
				if (tableModel.getRowCount()==0)
					return Object.class;
				else if (columnIndex == 0)
					return Double.class;
				else if ( columnIndex == table.getColumnCount()-1 )
					return Double.class;
				else if ( columnIndex == table.getColumnCount()-2 )
					return Double.class;
				else if ( columnIndex == table.getColumnCount()-3 )
					return Double.class;
				else if ( columnIndex == table.getColumnCount()-4 )
					return Double.class;
				else
					return getValueAt(0, columnIndex).getClass();
		    }
		};
		
		JScrollPane scrollPane = new JScrollPane();		// creates JScrollPane for JTable
		add(scrollPane, BorderLayout.CENTER);			// adds JSctrollpane to JPanel
		
		table = new JTable();										// creates a new JTable
		prepareTable();												// initializes JTable with dedault values
		table.setAutoCreateRowSorter(true);							// automatic row sorter enabled
		table.getTableHeader().setReorderingAllowed(false);			// orders cannot be reordered (dragged by user)
		table.setDefaultRenderer(Double.class, new MyRenderer());	// sets the JTables default renderer
		scrollPane.setViewportView(table);							// adds JTable to JScrollPane
		
		LOG.info("PredictionTable has been initialized");
	}
	
	/**
	 * 	Adds a line to the GUI table which represents a single survey
	 * 	@param phone unique identifier (phone number) of the Student
	 *	@param topic - name of the Topic the survey was performed on
	 * 	@param name name of the Student
	 * 	@param m1 - m1 value
	 * 	@param m2 - m2 value
	 * 	@param m3 - m3 value
	 * 	@param Reliability - realiability of generated values
	 */
	public void addTableLine(int number, String phone, String topic, String name, String m1, String m2,
			String m3, String Reliability) {
		Vector<Object> v = new Vector<>();
		v.add(number);
		v.add(phone);
		v.add(topic);
		v.add(name);
		if( (m1.equals("-1")) || Reliability.equals("Reliability")){
    		m1 = "-";
    		v.add(m1);
    	} else {
    		v.add(m1);
    	}
    	if( m2.equals("-1.0") ){
    		m2 = "-";
    		v.add(m2);
    	} else {
    		v.add(round(Double.parseDouble(m2),2));
    	}
    	if( m3.equals("-1.0") ){
    		m3 = "-";
    		v.add(m3);
    	} else {
    		v.add(round(Double.parseDouble(m3),2));
    	}
    	v.add(Reliability);
		
		if (v.size() == tableModel.getColumnCount()){
			tableModel.addRow(v);
			entries++;
		}
	}
	
	/**
	 *	Sets up table and initial filter field values. Prepares table model and sets column header. 
	 */
	public void prepareTable() {
		tableModel.addColumn("Nr");
		tableModel.addColumn("Phone");
		tableModel.addColumn("Topic");
		tableModel.addColumn("Name");
		tableModel.addColumn("M1");
		tableModel.addColumn("M2");
		tableModel.addColumn("M3");
		tableModel.addColumn("Reliability");
		
		table.setModel(tableModel);
		
		table.getColumn("Nr").setPreferredWidth(10);
		table.getColumn("Phone").setPreferredWidth(50);
		table.getColumn("Topic").setPreferredWidth(50);
		table.getColumn("Name").setPreferredWidth(50);
		table.getColumn("M1").setPreferredWidth(20);
		table.getColumn("M2").setPreferredWidth(50);
		table.getColumn("M3").setPreferredWidth(50);
		table.getColumn("Reliability").setPreferredWidth(50);
		
		comboBox_1.removeAllItems();
		comboBox_1.addItem("All");
		comboBox_1.setSelectedItem("All");
		for (int i=0; i<tableModel.getColumnCount(); i++)
			comboBox_1.addItem(tableModel.getColumnName(i));
		comboBox_1.addActionListener(this);
		
		comboBox2Generate();
	}
	
	/**
	 *	Reads specific filtered data from database.
	 *	@param colName - column name
	 *	@param value - row value in selected column
	 */
	public void readDBfiltered(String colName, String value) {
		
		try { 
            Class.forName(JDBC_DRIVER);
            LOG.info("Generating filter from database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
            
            String sql;
            colName = getShortForColumn(colName);
            if (colName.equals("*") || value.equals("All")) {
            	sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE OU IS null ORDER BY NAME";
            	pStmt = conn.prepareStatement(sql);
            } else if ( (colName.equals("M3") && (value.equals("Green") || value.equals("Orange") || value.equals("Red") )) || 
            		(colName.equals("M2") && (value.equals("Green") || value.equals("Orange") || value.equals("Red") )) ) {
            	sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE OU is null AND "+colName+" > ";
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
            	sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE "+colName+" = ? AND OU IS null ORDER BY NAME";
            	pStmt = conn.prepareStatement(sql);
            	if (colName.equals("NAME") || colName.equals("TOPIC") || colName.equals("PHONE") || colName.equals("RELIABILITY") ) 
            		pStmt.setString(1, value);
            	else
            		pStmt.setDouble(1, Double.parseDouble(value));
            }
            ResultSet rs = pStmt.executeQuery();
            conn.commit();
            
            list.clear(); 				// clears the arraylist
            
            tableModel.setRowCount(0); // clears table contents
            entries = 1;
            while (rs.next()) {
            	String phone = rs.getString("PHONE");
            	String topic = rs.getString("TOPIC");
                sql ="SELECT COEFFICIENT FROM MODELMANAGER WHERE COEFFICIENT IS NOT null AND KEY LIKE 'M2-"+topic+"' OR KEY LIKE 'M3-"+topic+"'";
                stmt = conn.createStatement();
                ResultSet rs1 = stmt.executeQuery(sql);
               	ArrayList<String> m2m3coef = new ArrayList<String>();
                while(rs1.next()){
                	String coef = rs1.getString("COEFFICIENT");
                	m2m3coef.add(coef);
                }
                conn.commit();
            	
            	String name = rs.getString("NAME");
            	String m1 = rs.getInt("M1") + "";
            	String m2 = rs.getDouble("M2") + "";
            	String m3 = rs.getDouble("M3") + "";
            	String reliability = rs.getString("RELIABILITY");
            	System.out.println(reliability);
//            	System.out.println(m2m3coef);
            	
            	if( m2m3coef.size()>1 ){
//            		System.out.println("m2 corr coef: "+m2m3coef.get(0)+" m3 corr coef: "+m2m3coef.get(1));
            		if( (Double.parseDouble(m2m3coef.get(0)) >= Double.parseDouble(m2m3coef.get(1))) ){
            			m3 = "-1.0";
            		} else {
            			m2 = "-1.0";
            		}
            	}
            	
            	ArrayList<String> row = new ArrayList<String>();
    			row.add(phone);
    			row.add(topic);	
    			row.add(name);		
    			row.add(m1);
    			row.add(m2);
    			row.add(m3);
    			row.add(reliability);
    			if( row.size() > 0)
    				list.add(row);
            	
            	addTableLine(entries, phone, topic, name, m1, m2, m3, reliability);
            }
            entryLabel.setText("Students: "+(entries-1) );
            LOG.info("Filter generated successfully");
            
        } catch (SQLException sqle) { //Handle errors for JDBC
        	LOG.error(sqle.getMessage()+" "+sqle.getCause());
            sqle.printStackTrace();
        } catch (Exception e) { 	//Handle errors for Class.forName
        	LOG.error(e.getMessage()+" "+e.getCause());
            e.printStackTrace();
        } finally {
            try {
                if (pStmt!=null)
                    conn.close();
                if (stmt!=null)
                	stmt.close();
                if (conn!=null)
                    conn.close();
            } catch (SQLException sqle) {
            	LOG.error(sqle.getMessage()+" "+sqle.getCause());
                sqle.printStackTrace();
            }
        }
	}
	
	/**
	 * 	Returns short form (abbreviation) of a column header to match with its database header name.
	 * 	@param colName - column name
	 * 	@return String - abbreviation of colName
	 */
	public String getShortForColumn(String colName) {
		String shortCol = null;
		switch (colName) {
			case "Phone":
				shortCol = "PHONE";
				break;
			case "Topic":
	    		shortCol = "TOPIC";
	    		break;
	    	case "Name":
	    		shortCol = "NAME";
	    		break;
	    	case "M1":
	    		shortCol = "M1";
	    		break;
	    	case "M2":
	    		shortCol = "M2";
	    		break;
	    	case "M3":
	    		shortCol = "M3";
	    		break;
	    	case "Reliability":
	    		shortCol = "RELIABILITY";
	    		break;
			default: // All
				shortCol = "*";
				break;
		}
		return shortCol;
	}
	
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp/factor;
	}
	
	/**
	 * 	Generates items for comboBox_2 by adding unique values selected column (selected item in comboBox_1)
	 */
	public void comboBox2Generate() {
		TreeSet<String> ts = new TreeSet<>( Collections.reverseOrder() );
		String colVal = null;
		colVal = comboBox_1.getSelectedItem().toString();
		colVal = getShortForColumn(colVal);
		
		if (!colVal.equals("*")) {
			try {
	            Class.forName(JDBC_DRIVER);
	            LOG.info("Connecting to a database...");
	            conn = DriverManager.getConnection(DB_URL, USER, PASS);
	            conn.setAutoCommit(false);
	            LOG.info("Connected to database successfully...");
	            String sql = "SELECT * FROM "+ DB_TABLE_NAME +" WHERE OU IS null";
	            pStmt = conn.prepareStatement(sql);
	            ResultSet rs = pStmt.executeQuery();
	            conn.commit();
	            
	            if (colVal.equals("NAME")||colVal.equals("TOPIC") || colVal.equals("RELIABILITY")) {
	            	while (rs.next()) {
	            		ts.add(rs.getString(colVal));
	            	}
	            } else if (colVal.equals("PHONE")) {
	            	while (rs.next())
	            		ts.add(rs.getString(colVal));
	            } else {
		            while (rs.next())
		            	ts.add(rs.getDouble(colVal)+"");
	            }
	            if( colVal.equals("M2") || colVal.equals("M3") ){
	            	ts.add("Green");
	            	ts.add("Orange");
	            	ts.add("Red");
	            }
	        } catch (SQLException sqle) { //Handle errors for JDBC
	        	LOG.error(sqle.getMessage()+" "+sqle.getCause());
	        	sqle.printStackTrace();
	        } catch (Exception e) { 	//Handle errors for Class.forName
	        	LOG.error(e.getMessage()+" "+e.getCause());
	            e.printStackTrace();
	        } finally {
	            try {
	                if (pStmt!=null)
	                    conn.close();
	            } catch (SQLException se) {}
	            try {
	                if (conn!=null)
	                    conn.close();
	            } catch (SQLException sqle) {
	            	LOG.error(sqle.getMessage()+" "+sqle.getCause());
	                sqle.printStackTrace();
	            }
	        }
		}
		comboBox_2.removeAllItems();
		comboBox_2.addItem("All");
		
		if (ts.size()>0) {
			for (String s: ts)
				comboBox_2.addItem(s);
		}
	}
	
	/**
	 * 	Creates custom CellRenderer class which changes cell background color for column 'M2'.
	 * 	Colors are: GREEN, ORANGE or RED, respectively 100-61, 60-26, 25-0% value in column M2.
	 * 	Compatible with column sorting.
	 * 	@author student
	 */
	@SuppressWarnings("serial")
	private class MyRenderer extends DefaultTableCellRenderer {
				
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {
			
			Component c = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			if (column == table.getColumnCount()-1 ) {
	        	TableModel model = table.getModel();
	        	int modelRow = table.getRowSorter().convertRowIndexToModel(row);
	        	String columnValue = (String)model.getValueAt(modelRow, column);
	        	if( columnValue.equals("High") )
	        		c.setBackground(new Color(103, 235, 103));//Color.green);
	        	else if( columnValue.equals("Medium") )
	        		c.setBackground(Color.orange);
	        	else if( columnValue.equals("Low") )
	        		c.setBackground(new Color(235, 69, 69));//Color.red);
	        	else
	        		c.setBackground(Color.gray);
			} else if( column == table.getColumnCount()-2 || column == table.getColumnCount()-3 ){
				TableModel model = table.getModel();
	        	int modelRow = table.getRowSorter().convertRowIndexToModel(row);
	        	if( model.getValueAt(modelRow, column) instanceof String ){
	        		c.setBackground(Color.white);
	        	} else {
	        		Double columnValue = (Double)model.getValueAt(modelRow, column);
	        		if (columnValue > 60)
	        			c.setBackground(new Color(103, 235, 103));//Color.green);
	        		else if (columnValue > 25)
	        			c.setBackground(Color.orange);
	        		else if (columnValue >= 0)
	        			c.setBackground(new Color(235, 69, 69));//Color.red);
	        	}
			} else if( column == table.getColumnCount()-4 ){
				TableModel model = table.getModel();
		        int modelRow = table.getRowSorter().convertRowIndexToModel(row);
		        String columnValue = (String)model.getValueAt(modelRow, column);
				if (columnValue.equals("2"))
					c.setBackground(new Color(103, 235, 103));//Color.green);
				else if (columnValue.equals("1"))
					c.setBackground(Color.orange);
				else if( columnValue.equals("0") )
					c.setBackground(new Color(235, 69, 69));//Color.red);
				else
					c.setBackground(Color.white);
			}
			else
				c.setBackground(new JButton().getBackground());
			this.setHorizontalAlignment( JLabel.CENTER );
			
			return c;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(comboBox_1)) {
			// Generates combobox_2 values; checks unique values in table where: column = combobox_1.selectedItem
			comboBox2Generate();
		} else if (e.getSource().equals(btnApply)) {
			// makes the table show only filter appropriate items.
			readDBfiltered(comboBox_1.getSelectedItem().toString(), comboBox_2.getSelectedItem().toString());
		} else if (e.getSource().equals(writeToXls)) {
			
			if( list.size() > 0 && fieldForInput.getText().length() > 0 ){	// is values are added to ArrayList and a name has been entered in JTextArea
				@SuppressWarnings("resource")
				HSSFWorkbook wb = new HSSFWorkbook();			// create Workbook instance for xlsx file
				HSSFSheet sheet = wb.createSheet("detailed");	// create Sheet for xlsx file
				
				HSSFRow row = sheet.createRow(0); 				// creates a new row in 1st row of the file
				HSSFCell cell;									// cell object
				
				for(int i = 0; i < COLUMNS.length; i++){
					cell = row.createCell(i);				// creates a new cell in i column
					cell.setCellValue(COLUMNS[i]);			// sets cell value to column name from database
				}
				int counter = 0;
				for(int i = 1; i < list.size()+1; i++){	// iterates rows times
					row = sheet.createRow(i);			// creates a new row 
					
					for(int j = 0; j < list.get(0).size(); j++){		// iterates columns times
						cell = row.createCell(j);						// creates a new cell
						cell.setCellValue(list.get(counter).get(j));	// sets cell value
					}
					counter++;
				}
				
				for(int i = 0; i < list.get(0).size(); i++){			// iterates column times
					sheet.autoSizeColumn(i);							// autosizes a column
				}
				
				try
				{
					path = new File(fieldForInput.getText() + ".xls");
					FileOutputStream fileOut = new FileOutputStream(path);
					wb.write(fileOut);	// write to file
					fileOut.flush();	// clears bytes from output stream
					fileOut.close();	// closes outputstream
					openFile.setVisible(true);
				}
				catch( IOException ioe ){
					LOG.error(ioe.getMessage()+" "+ioe.getCause());
					ioe.printStackTrace();
				}
			}
		} else if( e.getSource().equals(openFile) ){
			EditDatabasePanel.openFile(path);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {			// when a key is released
		if(e.getSource().equals(fieldForInput)){	// if source is JTextArea
			String line;							// a line of text from JTextArea
			if( e.getKeyCode()!=KeyEvent.VK_DELETE && e.getKeyCode()!=KeyEvent.VK_BACK_SPACE && e.getKeyCode()!=KeyEvent.VK_CONTROL
					&& e.getKeyCode()!=KeyEvent.VK_SHIFT && e.getKeyCode()!=KeyEvent.VK_CAPS_LOCK && e.getKeyCode()!=KeyEvent.VK_LEFT
					&& e.getKeyCode()!=KeyEvent.VK_UP && e.getKeyCode()!=KeyEvent.VK_RIGHT && e.getKeyCode()!=KeyEvent.VK_DOWN&& e.getKeyCode()!=KeyEvent.VK_KP_LEFT
					&& e.getKeyCode()!=KeyEvent.VK_KP_UP && e.getKeyCode()!=KeyEvent.VK_KP_RIGHT && e.getKeyCode()!=KeyEvent.VK_KP_DOWN){ 
				// ignores backspace, delete, shift, caps lock, control and arrows
				if( !((e.getKeyChar()<=122 && e.getKeyChar()>=97) || (e.getKeyChar()<=90 && e.getKeyChar()>=65) || (e.getKeyChar()<=57 && e.getKeyChar()>=48) 
						|| e.getKeyChar()==95) ){ 	// if char is not a-z or A-Z or 0-9 then delete it from JTextField
					if( fieldForInput.getText().contains(""+e.getKeyChar()) ){			// if textfield contains illegal char
						line = fieldForInput.getText().replaceAll("[\\"+e.getKeyChar()+"]+", "");// removes all illegal chars from string
						fieldForInput.setText(line);									// sets substring without illegal characters to jtextfield
					}
				}
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
