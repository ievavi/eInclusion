package org.einclusion.GUI;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.einclusion.model.M1;
import org.einclusion.model.M2;
import org.einclusion.model.M3;
import org.einclusion.model.ModelManager;
import org.einclusion.model.Prediction;
import org.einclusion.model.PrepareData;

/**
 *	Panel for importing, exporting and updating database using xlsx and csv files
 *	@author student
 */
public class EditDatabasePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1000L;

	static final String PERSISTENCE_SET = "test";			// persistence set for connecting to database
	private static final Logger LOG = Logger.getLogger(EditDatabasePanel.class);	// Logger for EditDatabasePanel
	JFileChooser fileChooser;							// a file chooser for choosing paths to files
	FileNameExtensionFilter filterxlsx, filterxls;		// filter for jfilechooser
	JButton chooseFile, createTemplateXlsx, openTemplateXlsx, updateDatabase, exampleFile, eraseFromDatabase, openFile, exportAllData;
	static JButton openExported;
	public static JComboBox<String> boxForTopics;		// choose a topic of students to delete
	JComboBox<String> boxForSpecifics;					// choose specific student to delete
	JScrollPane scrollPane;								// scrollpane for log
	public static JTextArea log;						// textarea for log (what the application is doing)
	static final Color colorForHighlight = new Color(0xff0033);	// highlight for error messages
	static final String JDBC_DRIVER = "org.h2.Driver";		// JDBC driver name
	static final String DB_URL = "jdbc:h2:data/Student;AUTO_SERVER=TRUE";	// databse URL (location of database)
	static final String USER = "sa";						// username for database
	static final String PASS = "";							// password for database
	static final String DB_TABLE_NAME = "STUDENT";			// default table name
	public static Connection conn = null;									// connection with a database
	public static Statement stmt = null;									// object used for executing sql statement
	public static TreeSet<String> treeSetTopics = new TreeSet<String>();
	public static ArrayList<String> ous = new ArrayList<String>();	// list of OUs from database
	static ArrayList<ArrayList<String>> deleteList = new ArrayList<ArrayList<String>>();	// list of students to be deleted from database
	public static File FILE;
	/**
	 *	Constructor for initializing EditDatabasePanel
	 */
	public EditDatabasePanel(){
		setPreferredSize(new Dimension(1000, 501));
		this.setLayout(null);										// EditDatabasePanel will have no layout (for specific adjustment of components)

		JPanel panel = new JPanel();								// creates a new jpanel that will contain log
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));	// sets top-down box layout to jpanel
		log = new JTextArea();										// creates new jtextarea
		log.setFont(new Font("Arial", Font.PLAIN, 12));				// sets font for jtextarea
		log.setEditable(false);										// jtextarea is not editable
		log.setOpaque(false);										// background of jtextarea will be opaque
		log.setAlignmentX(JTextArea.LEFT_ALIGNMENT);				// text will be aligned to the left
		panel.setBorder(new EmptyBorder(10,10,10,10));				// creates an empty border for spacing
		panel.add(log);												// adds jtextarea to jpanel

		scrollPane = new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);	// create a scrollpane with visible scrollbars
		scrollPane.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);		// sets left aligment to components
		scrollPane.setBounds(370,11,590,290);						// sets location and size of jscrollpane
		this.add(scrollPane);										// adds jscrollpane to jpanel

		fileChooser = new JFileChooser();							// creates a new JfileChooser
		fileChooser.setDialogTitle("Choose a file");				// sets jfilehoosers name
		fileChooser.setPreferredSize(new Dimension(600, 500));		// sets jfilechoosers size
		filterxlsx = new FileNameExtensionFilter("xlsx files", "xlsx");	// creates a new filter for .xlsx files
		filterxls = new FileNameExtensionFilter("xls files", "xls");	// creates a new filter for .xls files
		fileChooser.addChoosableFileFilter(filterxlsx);					// adds filter to dropdown
		fileChooser.addChoosableFileFilter(filterxls);					// adds filter to dropdown
		fileChooser.setFileFilter(filterxlsx);							// sets filter as default
		try{
			File defaultDirectory = new File(new File(System.getProperty("user.home") + 
					System.getProperty("file.separator")).getCanonicalPath() );	// creates a new path to directory
			fileChooser.setCurrentDirectory(defaultDirectory);		// sets jfilechooser starting directory
		}catch(IOException ioe){									// catches exception while creating path to directory
			LOG.error(ioe.getMessage()+" "+ioe.getCause());
			ioe.printStackTrace();
			String errorText = "Exception while creating JfileChooser default directory path!";
			log.append(errorText+"\n");
			highlight(log, errorText);
		}
		fileChooser.setVisible(false);								// fileChooser is not visible
		this.add(fileChooser);										// adds filechooser to jpanel

		chooseFile = new JButton("Choose a file");					// creates a new button for choosing file path
		chooseFile.setFont(new Font("Arial", Font.BOLD, 12));		// sets button font
		chooseFile.addActionListener(this);							// adds actionlistener to button
		chooseFile.setToolTipText("Choose a valid .xlsx file");		// adds tooltip to button
		chooseFile.setBounds(50, 10, 190, 30);						// set location and size of button
		this.add(chooseFile);										// add button to jpanel

		openFile = new JButton("Open");								// creates a  new button for opening a file
		openFile.setToolTipText("<html>Opens the created .csv file with its default program<br>"+
				"(to change this program right click on any .csv file<br>and choose"+
				" the program you want to open it with)</html>"); 	// sets tooltip for jbutton
		openFile.setFont(new Font("Arial", Font.BOLD, 12));			// sets font for jbutton
		openFile.setBounds(250,10,110,30);							// sets location and size of jbutton
		openFile.addActionListener(this);							// adds actionlistener to jbutton
		openFile.setVisible(false);									// sets jbutton to not visible
		this.add(openFile);											// adds jbutton to jpanel

		createTemplateXlsx = new JButton("Create template");		// creates a  new Jbutton for opening a file
		createTemplateXlsx.setToolTipText("Creates Update_Database.xls file");  // sets tooltip for jbutton
		createTemplateXlsx.setFont(new Font("Arial", Font.BOLD, 12));			// sets font for jbutton
		createTemplateXlsx.setBounds(50,60,190,30);					// sets location and size of jbutton
		createTemplateXlsx.addActionListener(this);					// adds actionlistener to jbutton
		this.add(createTemplateXlsx);								// adds jbutton to jpanel

		openTemplateXlsx = new JButton("Open");						// creates a  new jbutton for opening a file
		openTemplateXlsx.setToolTipText("Opens the created Update_Databse.xls file");	// sets tooltip for jbutton
		openTemplateXlsx.setFont(new Font("Arial", Font.BOLD, 12));	// sets font for jbutton
		openTemplateXlsx.setBounds(250,60,110,30);					// sets location and size of jbutton
		openTemplateXlsx.setVisible(false);							// this jbutton is not visible
		openTemplateXlsx.addActionListener(this);					// adds actionlistener to jbutton
		this.add(openTemplateXlsx);									// adds jbutton to jpanel

		updateDatabase = new JButton("Update database");			// creates a jbutton for updating database
		updateDatabase.setToolTipText("<html>Choose a .xls file that will update the database<br>"+
				" (see example first)</html>");	// sets tooltip for jbutton
		updateDatabase.setFont(new Font("Arial", Font.BOLD, 12));   // sets font for jbutton
		updateDatabase.setBounds(50,110,190,30);					// sets location and size of jbutton
		updateDatabase.addActionListener(this);						// adds actionlistener to jbutton
		this.add(updateDatabase);									// adds jbutton to jpanel

		exampleFile = new JButton("Example");						// creates a jbutton for seeing example .xlsx file
		exampleFile.setToolTipText("Opens example.xls file with its default program");	// sets tooltip for jbutton
		exampleFile.setFont(new Font("Arial", Font.BOLD, 12));		// sets font for jbutton
		exampleFile.setBounds(250,110,110,30);						// sets location and size of jbutton
		exampleFile.addActionListener(this);						// adds actionlistener to jbutton
		this.add(exampleFile);										// adds jbutton to janel

		eraseFromDatabase = new JButton("Delete from database");						// creates a jbutton for deleting database
		eraseFromDatabase.setToolTipText("<html>Erases selected entries from database<br>"+
				"(if no entries are selected deletes all entries)</html>");	// sets tooltip for jbutton
		eraseFromDatabase.setFont(new Font("Arial", Font.BOLD, 12));			// sets font for jbutton
		eraseFromDatabase.setBounds(50,350,190,30);								// sets location and size of jbutton
		eraseFromDatabase.addActionListener(this);								// adds actionlistener to jbutton
		this.add(eraseFromDatabase);											// adds jbutton to janel
		
		boxForTopics = new JComboBox<String>();								// creates new JComboBox for choosing topics
		boxForTopics.addItem("ALL");
		boxForTopics.setSelectedIndex(0);									// sets selected value to first
		boxForTopics.setBounds(270,350,190,30);								// sets location and size of jcombobox
		boxForTopics.addActionListener(this);								// adds actionlistener to jcombobox
		this.add(boxForTopics);												// adds jcombobox to JPanel

		boxForSpecifics = new JComboBox<String>();							// creates a new Jcombobox for choosing specific students
		boxForSpecifics.addItem("ALL");
		boxForSpecifics.setSelectedIndex(0);								// sets selected value to first
		boxForSpecifics.setBounds(490, 350, 190, 30);						// sets location and size of jcombobox
		this.add(boxForSpecifics);											// adds jcombobox to jpanel


		exportAllData = new JButton("Export");						// creates a jbutton for exporting data specifically for weka
		exportAllData.setToolTipText("Exports all database contents to Exported.xls file");	// sets tooltip for jbutton
		exportAllData.setFont(new Font("Arial", Font.BOLD, 12));	// sets font for jbutton
		exportAllData.setBounds(50,160,190,30);						// sets location and size of jbutton
		exportAllData.addActionListener(this);						// adds actionlistener to jbutton
		this.add(exportAllData);									// adds jbutton to janel

		openExported = new JButton("Open");							// creates a jbutton for opening file
		openExported.setToolTipText("Opens Exported.xls file");	// sets tooltip for jbutton
		openExported.setFont(new Font("Arial", Font.BOLD, 12));		// sets font for jbutton
		openExported.setBounds(250,160,110,30);						// sets location and size of jbutton
		openExported.addActionListener(this);						// adds actionlistener to jbutton
		openExported.setVisible(false);								// sets button to not visible
		this.add(openExported);										// adds jbutton to janel

		LOG.info("EditDatabasePanel intialized");
	}
	
	/**
	 * 	Funtion that adds all the topics from database to TreeSet
	 * 	@param conn - a connection (session) with a database
	 * 	@param stmt - Statement object
	 */
	public static void getTopics(Connection conn, Statement stmt){
		try
		{
			Class.forName(JDBC_DRIVER);		// initializes JDBC driver							
			conn = DriverManager.getConnection(DB_URL, USER, PASS);	// establsih connection to database
			conn.setAutoCommit(false);		// sets autocommit to false

			String sql = "SELECT TOPIC, OU FROM STUDENT";	// make sql statement

			stmt = conn.createStatement();				// creates a new statement object
			ResultSet rs = stmt.executeQuery(sql);		// a table of data that is obtained by executing a sql statement
			conn.commit();								// makes changes to database permanent
			ous.clear();
			while( rs.next() ){											// while table has contents
				String ou = rs.getString("OU");
				if( ou != null  ){
				ous.add(ou);
				}
				String topic = rs.getString("TOPIC");
				treeSetTopics.add(topic);
			}
		} catch ( ClassNotFoundException cnfe ){
			LOG.error(cnfe.getMessage()+" "+cnfe.getCause());
			cnfe.printStackTrace();
		} catch ( SQLException sqle ){
			LOG.error(sqle.getMessage()+" "+sqle.getCause());
			sqle.printStackTrace();
		} finally {
			try {
				if(stmt!=null){
					stmt.close();
				}
				if (conn!=null)
					conn.close();
			} catch (SQLException sqle) {
				LOG.error(sqle.getMessage()+" "+sqle.getCause());
				sqle.printStackTrace();
			}
		}
	}
	/**
	 *	Function that opens a file with its default program
	 *	@param file - path to file
	 */
	public static void openFile(File file){
		try
		{
			if(Desktop.isDesktopSupported()){			// if desktop class is suported on this platform
				Desktop desktop = Desktop.getDesktop(); // gets desktop instance of current broswer context
				if(file.exists()){
					desktop.open(file);						// if file exists open it with its default program
				}
			}
		} catch( IOException ioe){
			LOG.error(ioe.getMessage()+" "+ioe.getCause());
			ioe.printStackTrace();
			String errorText = "Exception while opening file";
			log.append(errorText+"\n");
			highlight(log, errorText);
		}
	}
	/**
	 *	Function for highlighting text in JTextArea
	 *	@param area - JTextArea
	 *	@param token - string to be highlighted within the JTextArea
	 */
	public static void highlight(JTextArea area, String token){
		try
		{
			Highlighter highlighter = area.getHighlighter();
			HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(colorForHighlight);
			int start = area.getText().indexOf(token);		// finds starting index of string
			int end = token.length() + start;				// finds end index of string

			highlighter.addHighlight(start, end, painter );	// highlights a string with indexes start and end
			LOG.info(""+token+" has been highlighted");
		} catch( BadLocationException ble ){			// catches outofbounds exceptions for JTextArea
			LOG.error(ble.getMessage()+" "+ble.getCause());
			ble.printStackTrace();
		}
	}
	/**
	 *	Function for creating a .csv file from a .xlsx file
	 *	@param inputFile - file path to an xlsx file
	 *	@param outputFile - file path to a csv file
	 */
	public static void xlsxToCsv(File inputFile, File outputFile) throws Exception {
		// For storing data into CSV files
		log.append("Converting ...\n");
		StringBuffer data = new StringBuffer();
		try {			
			@SuppressWarnings("resource")
			XSSFWorkbook wBook = new XSSFWorkbook(new FileInputStream(inputFile));
			// Get first sheet from the workbook
			XSSFSheet sheet = null;
			// "Iterate" through each rows from first sheet
			// for each sheet in the workbook
			boolean found = false;
			for (int i = 0; i < wBook.getNumberOfSheets(); i++) {
				if(wBook.getSheetName(i).equals("detailed")){
					sheet = wBook.getSheetAt(i);
					found = true;
					break;
				}
			}
			if( found == false ){
				log.append("\"detailed\" sheet not found\n");
				LOG.warn("No detailed sheet found in: "+inputFile.getName());
				throw new Exception("No sheet found exception");
			}
			else{
				FileOutputStream fos = new FileOutputStream(outputFile);
				// Values to skip and not write to the CSV file which will then be sent to database
				int skipLietotajvards = 1;	//Ignores Lietotajvards column
				int skipComments = 16;	//Jusu komentars column number set manually so it can be skipped
				int skipKursaID = 18;	//Kursa ID column number set manually so it can be skipped
				int skipKurss = 19;		//Kurss column number set manually so it can be skipped
				int date = skipKursaID -1;	//Assumes date column is the column just before Kursa ID column
				short emptyCellCount = -1;

				String dateString; //will hold the date cell as written in the excel file

				for(Row row : sheet){	 //while there are rows
					if(row.getRowNum()!=1){ 	//don't need to look at the second row
						for(int cn = 0; cn < row.getLastCellNum(); cn++) {		//while there are cells in the row
							if(cn != skipKursaID && cn != skipKurss && cn != skipComments && cn != skipLietotajvards){		//if the current cell is one we need
								Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK); 		//takes the cell even if it's empty

								switch (cell.getCellType()) { //determines the type of the cell
								case Cell.CELL_TYPE_BOOLEAN:
									data.append(cell.getBooleanCellValue() + ",");
									break;
								case Cell.CELL_TYPE_NUMERIC:
									data.append(cell.getNumericCellValue() + ",");
									break;
								case Cell.CELL_TYPE_STRING:
									if(cn == date){
										dateString = cell.getStringCellValue();
										String dateFinal = new String();
										int dotCount = 0;
										String day = new String();
										for (int i = 0; i < dateString.length(); i++){
											char c = dateString.charAt(i);  
											if(dotCount < 2){
												if(c >= 48 && c <=57 && dotCount != 1){
													dateFinal += c;
												}
												else if(c >= 48 && c <=57){
													day += c;
												}
												else if(c == 46){
													dotCount++;
												}
											}
											else if(dateString.contains("January")){
												dateFinal += "-1-";
												dateFinal += day;
												break;
											}
											else if(dateString.contains("February")){
												dateFinal += "-2-";
												dateFinal += day;
												break;
											}
											else if(dateString.contains("March")){
												dateFinal += "-3-";
												dateFinal += day;
												break;
											}
											else if(dateString.contains("April")){
												dateFinal += "-4-";
												dateFinal += day;
												break;
											}
											else if(dateString.contains("May")){
												dateFinal += "-5-";
												dateFinal += day;
												break;
											}
											else if(dateString.contains("June")){
												dateFinal += "-6-";
												dateFinal += day;
												break;
											}
											else if(dateString.contains("July")){
												dateFinal += "-7-";
												dateFinal += day;
												break;
											}
											else if(dateString.contains("August")){
												dateFinal += "-8-";
												dateFinal += day;
												break;
											}
											else if(dateString.contains("September")){
												dateFinal += "-9-";
												dateFinal += day;
												break;
											}
											else if(dateString.contains("October")){
												dateFinal += "-10-";
												dateFinal += day;
												break;
											}
											else if(dateString.contains("November")){
												dateFinal += "-11-";
												dateFinal += day;
												break;
											}
											else if(dateString.contains("December")){
												dateFinal += "-12-";
												dateFinal += day;
												break;
											}
											else{
												dateFinal += "-00-";
												dateFinal += day;
												break;
											}
										}
										dotCount = 0;
										if(row.getRowNum() != 0)
											data.append(dateFinal + ",");
									}
									else if (row.getRowNum() != 0)
										data.append(cell.getStringCellValue() + ",");
									break;
								case Cell.CELL_TYPE_BLANK:
									if(row.getRowNum() > 1 ){
										if(cn == 2 || cn == 3 || cn == date) //these are the cells that contain text
											data.append("-No data-" + ",");	//and will be written to the CSV file
										else{
											data.append(emptyCellCount + ",");		//all other columns are expected to be numbers
											emptyCellCount--;
										}
										break;
									}
									else
										break;
								default:
									data.append(cell + ",");
								}
							}
						}
						if(row.getRowNum() != 0)
							data.append("\r\n"); //EXCEL EOL
					}
				}
				fos.write(data.toString().getBytes());
				fos.close();
				LOG.info("Created file: "+outputFile.getName());
				log.append("Created file: "+outputFile.getName()+"\n");
			}
		} catch (Exception ioe) {
			LOG.error(ioe.getMessage()+" "+ioe.getCause());
			ioe.printStackTrace();
			String errorText = "Exception while creating .csv file";
			log.append(errorText+"\n");
			highlight(log, errorText);
		}
	}
	/**
	 *	Function for reading an excel file and saving its contents
	 *	@param readFrom - path to a xls file
	 * 	@param excelData - ArrayList(ArrayList(String)) an ArrayList that contains ArrayLists of String values (like a 2D array of strings)
	 */
	public static void readExcelFile( File readFrom, ArrayList<ArrayList<String>> excelData ){
		try {
			FileInputStream fis = new FileInputStream(readFrom);	// create the input stream from the xls file
			@SuppressWarnings("resource")
			Workbook workbook = new HSSFWorkbook(fis);				// create Workbook instance for xls file input stream
			Sheet sheet = workbook.getSheetAt(0);					// get the 1st sheet from the workbook
			Iterator<Row> rowIterator = sheet.iterator();			// every sheet has rows, iterate over them
			boolean firstIteration = true;							// for reading columns names
			while (rowIterator.hasNext()) {							// while there are rows to read from
				ArrayList<String> rows = new ArrayList<String>();	// arraylist for saving one row
				String columnName = null;
				String cellValue = null;

				Row row = rowIterator.next();						// get the row object
				Iterator<Cell> cellIterator = row.cellIterator();	//every row has columns, get the column iterator and iterate over them
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();			// get the Cell object
					switch(cell.getCellType()){					// check the cell type
					case Cell.CELL_TYPE_STRING:					// if celltype is string
					{
						if( firstIteration == true){						// if firstiteration
							columnName = cell.getStringCellValue().trim();	// get columnname
							rows.add(columnName);							// add to arraylist
						}
						else{
							cellValue = cell.getStringCellValue().trim();	// get cellValue
							rows.add(cellValue);							// add to arraylist
						}
						break;
					}
					case Cell.CELL_TYPE_NUMERIC:				// if celltype is numeric
					{
						long number = (long) cell.getNumericCellValue();	// cast value to long
						cellValue = String.valueOf(number);					// create string from long
						rows.add(cellValue);								// add to arraylist
						break;
					}
					}
				} //end of cell iterator
				firstIteration = false;							// after firstiteration is false
				if(rows.size() > 0){							// if arraylist of one row is not empty
					excelData.add(rows);						// add it to arraylist that contains all rows
				}
			} //end of rows iterator
			LOG.info("File: "+readFrom.getName()+" read successfully");
		}
		catch (IOException ioe) {
			LOG.error(ioe.getMessage()+" "+ioe.getCause());
			ioe.printStackTrace();
			String errorText = "Exception while reading file";
			log.append(errorText+"\n");
			highlight(log, errorText);
		}
	}
	/**
	 * 	Funtion that exports all student data to a .xls file
	 *	@param path - path to a .xls file ( or a new file )
	 *	@param conn - Connection object
	 *	@param stmt - Statement object
	 */
	public static void exportAllData( File path, Connection conn, Statement stmt ){
		try
		{
			log.setText("");
			Class.forName(JDBC_DRIVER);		// initializes JDBC driver						
			log.append("Connecting to database... \n");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);	// establsih connection to database
			conn.setAutoCommit(false);		// sets autocommit to false
			log.append("Connected to database successfully \n");
			log.append("Reading from database ...\n");
			String sql = "SELECT * FROM STUDENT";	// make sql statement

			stmt = conn.createStatement();				// creates a new statement object
			ResultSet rs = stmt.executeQuery(sql);		// a table of data that is obtained by executing a sql statement
			conn.commit();		

			ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

			while( rs.next() ){
				ArrayList<String> data = new ArrayList<String>();
				String phone = rs.getString("PHONE");
				String topic = rs.getString("TOPIC");
				String name = rs.getString("NAME");
				String ds = rs.getString("DS");
				String ele = rs.getString("ELE");
				String elm = rs.getString("ELM");
				String iws = rs.getString("IWS");
				String sal = rs.getString("SAL");
				String swl = rs.getString("SWL");
				String klal = rs.getString("KLAL");
				String klbl = rs.getString("KLBL");
				String ou = rs.getString("OU");
				String pu = rs.getString("PU");
				String puou = rs.getString("PUOU");
				String m1 = rs.getString("M1");
				String m2 = rs.getString("M2");
				String kfa = rs.getString("KFA");
				String m3 = rs.getString("M3");
				String reliability = rs.getString("RELIABILITY");
				Date dateStamp = new Date(rs.getTimestamp("SUBMITDATE").getTime());
				String submitDate = dateStamp.toString();

				data.add(phone);
				data.add(topic);
				data.add(name);
				data.add(ds);
				data.add(ele);
				data.add(elm);
				data.add(iws);
				data.add(sal);
				data.add(swl);
				data.add(klal);
				data.add(klbl);
				data.add(ou);
				data.add(pu);
				data.add(puou);
				if( m1 != null )
					data.add( M2Table.round(Double.parseDouble(m1), 2)+"" );
				else
					data.add(m1);
				if( m2 != null )
					data.add( M2Table.round(Double.parseDouble(m2), 2)+"" );
				else 
					data.add(m2);
				data.add(kfa);
				if( m3 != null )
					data.add( M2Table.round(Double.parseDouble(m3), 2)+"" );
				else
					data.add(m3);
				data.add(reliability);
				data.add(submitDate);

				list.add(data);
			}
			log.append("Reading complete\n");
			if( !list.isEmpty() ){
				log.append("Writing to file...\n");
				@SuppressWarnings("resource")
				HSSFWorkbook wb = new HSSFWorkbook();			// create Workbook instance for xls file
				HSSFSheet sheet = wb.createSheet("detailed");	// create Sheet for xls file
				HSSFRow row = sheet.createRow(0);				// creates a new row in the file
				HSSFCell cell;									// cell object
				String columnNames[] = {"PHONE", "TOPIC", "NAME", "DS", "ELE", "ELM", "IWS", "SAL", "SWL", "KLAL", "KLBL", "OU", "PU", "PUOU", "M1", "M2", "KFA", "M3", "RELIABILITY", "SUBMITDATE"};

				for(int i = 0; i < list.get(0).size(); i++){
					cell = row.createCell(i);
					cell.setCellValue(columnNames[i]);
				}
				
				for(int i = 0; i < list.get(0).size(); i++){
					if( (i > 2 && i <=13) || i == 16 )
						sheet.autoSizeColumn(i);
				}

				int counter = 0;
				for(int i = 1; i < list.size()+1; i++){					// iterates students times
					row = sheet.createRow(i);							// creates a new row 

					for(int j = 0; j < list.get(0).size(); j++){		// iterates columns times
						cell = row.createCell(j);						// creates a new cell
						cell.setCellValue(list.get(counter).get(j));	// sets cell value
					}
					counter++;
				}

				for(int i = 0; i < list.get(0).size(); i++){			// resizes columns
					if( (i <= 2 || i > 13) && i != 16  )
					sheet.autoSizeColumn(i);
				}
				
				try
				{
					FileOutputStream fileOut = new FileOutputStream(path);
					wb.write(fileOut);	// write to file
					fileOut.flush();	// clears bytes from output stream
					fileOut.close();	// closes outputstream
					log.append("File created: "+path+"\n");
					openExported.setVisible(true);

				}
				catch( IOException ioe ){
					LOG.error(ioe.getMessage()+" "+ioe.getCause());
					ioe.printStackTrace();
					String errorText = "Close Exported.xlsx file";
					log.append(errorText+"\n");
					highlight(log, errorText);
				}
			} else {
				String errorText = "Database is empty";
				log.append(errorText+"\n");
				highlight(log, errorText);
			}
		} catch ( ClassNotFoundException cnfe ){
			LOG.error(cnfe.getMessage()+" "+cnfe.getCause());
			cnfe.printStackTrace();
			String errorText = "Exception while connecting to database";
			log.append(errorText+"\n");
			highlight(log, errorText);
		} catch ( SQLException sqle ){
			LOG.error(sqle.getMessage()+" "+sqle.getCause());
			sqle.printStackTrace();
			String errorText = "Exception while reading from database";
			log.append(errorText+"\n");
			highlight(log, errorText);
		} finally{
			try{
				if( conn != null ){
					conn.close();
				}
				if( stmt != null ){
					stmt.close();
				}
			} catch( SQLException sqle ){
				LOG.error(sqle.getMessage()+" "+sqle.getCause());
				sqle.printStackTrace();
				String errorText = "Exception while closing connection to database";
				log.append(errorText+"\n");
				highlight(log, errorText);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if( e.getSource().equals(chooseFile) ){							// if choosefile button is pressed
			fileChooser.setFileFilter(filterxlsx);						// sets .xlsx filter as default
			fileChooser.setVisible(true);								// sets jfilechooser to visible
			int status = fileChooser.showDialog(null, "Choose File");	// gets state of jfilechooser
			if( status == JFileChooser.APPROVE_OPTION ){				// if open is pressed in jfilechooser
				new Thread() { 											// creates a new thread so processes execute consecutively
					public void run() {									// creates run method for thread
						long start = System.nanoTime();					// gets System time in nanoseconds
						  FILE = fileChooser.getSelectedFile();		// gets selected files path
						String extension = FILE.getName().substring(FILE.getName().indexOf("."), FILE.getName().length());	// gets file extension
						if( extension.equals(".xlsx") ){						// if file is .xlsx
							log.setText("Selected file: " + FILE.getName()+"\n");
							String fileName = FILE.getName();									// gets files name
							fileName = fileName.substring(0, fileName.indexOf(".")) + ".csv";	// changes extension to .csv
							File currentDirectory = new File(fileName);			// sets files location to current directory (directory project is in)

							try{
								xlsxToCsv(FILE, currentDirectory);				// converts .xlsx file to .csv file
								openFile.setVisible(true);
							}
							catch( Exception ex ){
								LOG.error(ex.getMessage()+" "+ex.getCause());
								ex.printStackTrace();
							}

							try {
								ModelManager.initModelManager(PERSISTENCE_SET);	// loads a persistence set (connects to h2 database)
								log.append("Writing to database ...\n");
								PrepareData.csv2db(currentDirectory);			// reads data from .csv file
								for( String topic : treeSetTopics ){
									boxForTopics.removeItem(topic);				// removes all boxForTopics items except for "ALL"
								}
								getTopics(conn, stmt);							// writes topics from database to treeset
								for( String topic : treeSetTopics ){			// adds topics to Jcombobox
									boxForTopics.addItem(topic);
								}
								if( !(ous.isEmpty()) ){							// if there are ous in database calculate models
									log.append("Generating models...\n");
									LOG.info("\n\n///////////////////////////////////\tM1-Clusters\t////////////////////////////////////\n");
									for( String topic : treeSetTopics ){			// generates M1 model for all topics
										try{
											M1.getCluster(topic, "M1-clusters-"+topic, "M1-centroids-"+topic);
										} catch(Throwable t){
											LOG.error(t.getMessage() + " " + t.getCause());
											t.printStackTrace();
										}
									}
									LOG.info("\n\n////////////////////////////////////\tM2-Regression\t////////////////////////////////////\n");
									for( String topic : treeSetTopics ){			// generates M2 model for all topics
										try{
											M2.getRegression(topic, "M2-"+topic);
										} catch(Throwable t){
											LOG.error(t.getMessage() + " " + t.getCause());
											t.printStackTrace();
										}
									}
									LOG.info("\n\n////////////////////////////////////\tM3-Regression\t////////////////////////////////////\n");
									for( String topic : treeSetTopics ){			// generates M3 model for all topics
										try{
											M3.getRegression(topic, "M3-"+topic);
										} catch(Throwable t){
											LOG.error(t.getMessage() + " " + t.getCause());
											t.printStackTrace();
										}
									}
									LOG.info("\n\n////////////////////////////////////\tPREDICTION\t////////////////////////////////////\n");
									for( String topic : treeSetTopics ){			// generates Prediction model for all topics
										try{
											Prediction.getPrediction(topic);
										} catch(Throwable t){
											LOG.error(t.getMessage() + " " + t.getCause());
											t.printStackTrace();
										}
									}
								}
							} catch (Throwable t) {
								LOG.error(t.getMessage() + " " + t.getCause());
								t.printStackTrace();
							}
							finally {
								ModelManager.closeModelManager();				// closes connection to database
								log.append("Finished writing to database\n");
							}
							
							System.out.println(treeSetTopics.toString());		// shows in console topics from treeSet
							
							long end = System.nanoTime();						// gets system time
							long elapsedTime = end - start;						// gets execution time of function
							double seconds = (double)elapsedTime / 1000000000.0;// converts nanoseconds to seconds
							seconds = M2Table.round(seconds, 3);				// rounds seconds to 3 digits
							log.append("Execution time: "+seconds+" sec\n");
						}
						else{	// if not .xlsx file shows warning
							LOG.warn("Invalid file type: "+extension);
							JOptionPane.showMessageDialog( null, "Invalid file type: "+extension, "Warning !", JOptionPane.INFORMATION_MESSAGE );
						} 
					}
				}.start();	// starts thread
			}
			else if( status == JFileChooser.ERROR_OPTION ){	// if JFileChooser error occurs
				LOG.warn("File chooser error");
				JOptionPane.showMessageDialog( null, "File chooser error", "Warning !", JOptionPane.INFORMATION_MESSAGE );
			}
		}
		else if( e.getSource().equals(openFile) ){		// if openfile button is pressed
			File file = fileChooser.getSelectedFile();	// gets selected file path
			String fileName = file.getName();			// gets selected file name
			fileName = fileName.substring(0, fileName.indexOf(".")) + ".csv";	// changes files extension to .csv
			file = new File(fileName);					// creates new path in projects directory
			openFile(file);								// function that opens file with its default program
		}
		else if( e.getSource().equals(createTemplateXlsx) ){ // if createTemplateXlsx button is pressed
			new Thread(){
				public void run(){
					try
					{
						long start = System.nanoTime();						// gets system time
						log.setText("Creating template file...\n");
						@SuppressWarnings("resource")
						HSSFWorkbook workBook = new HSSFWorkbook();			// create Workbook instance for xlsx file
						HSSFSheet sheet = workBook.createSheet("detailed");	// creates "detailed" Sheet for xlsx file
						HSSFRow row = sheet.createRow(0);					// creates a new row
						Cell cell = row.createCell(0);						// creates a new cell
						cell.setCellValue("PHONE");							// sets cell value
						cell = row.createCell(1);							// creates a new cell
						cell.setCellValue("TOPIC");							// sets cell value
						cell = row.createCell(2);							// creates a new cell
						cell.setCellValue("OU");							// sets cell value

						row = sheet.createRow(1);

						cell = row.createCell(0);
						cell.setCellValue("enter phone number");
						cell = row.createCell(1);
						cell.setCellValue("enter topic");
						cell = row.createCell(2);
						cell.setCellValue("enter value");

						sheet.autoSizeColumn(0); //adjust width of the first column
						sheet.autoSizeColumn(1); //adjust width of the second column
						sheet.autoSizeColumn(2); //adjust width of the third column

						File file = new File("Update_Database.xls");			// creates a file path
						FileOutputStream fileOut = new FileOutputStream(file);	// creates outputstream
						workBook.write(fileOut);								// writes to xlsx file
						fileOut.flush();										// clears outputstream
						fileOut.close();										// closes outputstream
						log.append("Template file created: "+file.getPath()+"\n");
						long end = System.nanoTime();							// gets sopenFileystem time
						long elapsedTime = end - start;							// calculates execution time
						double seconds = (double)elapsedTime / 1000000000.0; 	// converts nanoseconds to seconds
						seconds = M2Table.round(seconds, 3);					// rounds seconds to 3 digits
						log.append("Execution time: "+seconds+" sec\n");
						openTemplateXlsx.setVisible(true);						// sets open template button to visible
						LOG.info("File: "+file.getName()+" created successfully");
					}
					catch( IOException ioe){
						LOG.error(ioe.getMessage()+ioe.getCause());
						ioe.printStackTrace();
						String errorText = "Exception while creating Update_Database.xls file";
						log.append(errorText+"\n");
						highlight(log, errorText);
					}
				}
			}.start();
		}
		else if( e.getSource().equals(openTemplateXlsx) ){	// if openTemplateXlsx button is pressed
			File file = new File("Update_Database.xls");	// creates a new file path
			openFile(file);									// opens file with its default program
		}
		else if( e.getSource().equals(updateDatabase) ){	// if updatedatabase button is pressed
			fileChooser.setFileFilter(filterxls);			// sets .xls filter as default
			fileChooser.setVisible(true);					// set jfilechooser to visible
			int status = fileChooser.showDialog(null, "Choose File");	// gets state of jfilechooser
			if( status == JFileChooser.APPROVE_OPTION ){				// if open is pressed in jfilechooser
				final File file = fileChooser.getSelectedFile();		// gets selected files path
				new Thread() { 				// creates a new thread so processes execute consecutively
					public void run() {		// creates run method for thread
						String extension = file.getName().substring(file.getName().indexOf("."), file.getName().length());

						if( extension.equals(".xls") ){							// if file extension is xlsx
							log.setText("Selected file: "+file.getName()+"\n");
							try {
								long start = System.nanoTime();	// gets system time
								if( treeSetTopics.isEmpty() )	// if update database button is the 1st button pressed when opening programm
									getTopics(conn, stmt);		// writes topics to treeSet
								// creates an arraylist of all rows, that contains arraylists of one row (like 2d array)
								ArrayList<ArrayList<String>> excelData = new ArrayList<ArrayList<String>>();
								readExcelFile(file, excelData);	// reads excelfile and saves it to arraylist
								Class.forName(JDBC_DRIVER);		// initializes JDBC driver					
								log.append("Connecting to database...\n");
								conn = DriverManager.getConnection(DB_URL, USER, PASS);	// establsih connection to database
								conn.setAutoCommit(false);		// sets autocommit to false
								log.append("Connected to database successfully\n");
								log.append("Updating database...\n");
								StringBuilder sb = new StringBuilder();				// creates a stringbuilder for column names
								for(int i = 0; i < excelData.get(0).size(); i++){	// repeats first row size times
									if( i == excelData.get(0).size()-1){			// if last iteration
										sb.append(excelData.get(0).get(i)+" ");		// add column names to string builder
									}
									else{											// if not last iteration
										sb.append(excelData.get(0).get(i)+", ");	// add column names to string builder
									}
								}
								String columnNames = new String(sb);				// make string with stringbuilder contents
								String sql = "SELECT "+ columnNames +"FROM "+DB_TABLE_NAME;	// make sql statement

								stmt = conn.createStatement();				// creates a new statement object
								ResultSet rs = stmt.executeQuery(sql);		// a table of data that is obtained by executing a sql statement
								conn.commit();								// makes changes to database permanent
								while( rs.next() ){											// while table has contents
									String number = rs.getString(excelData.get(0).get(0));  // columnname ( excelData.get(0).get(i) )
									String topic = rs.getString(excelData.get(0).get(1));	// gets topic from database
									if(number != null && topic != null){										// if number is initialized
										for(int i = 0; i < excelData.size(); i++){			// amount of rows times
											if( number.equals(excelData.get(i).get(0)) && topic.equals(excelData.get(i).get(1)) ){	// number ( excelData.get(j).get(0)	)					            			
												for(int j = 2; j < excelData.get(0).size(); j++ ){	// (amount of columns - 1) times
													switch(excelData.get(0).get(j)){
													case "ELE": case "ELM": case "IWS": case "SAL": case "SWL": case "KLAL": case "KLBL": case "DS":
														if( Double.parseDouble(excelData.get(i).get(j)) >= 1 && Double.parseDouble(excelData.get(i).get(j)) <= 5 ){
															// creates sql statement for inserting value into database
															String statement = "UPDATE "+DB_TABLE_NAME+" SET "+excelData.get(0).get(j)+
																	"='"+excelData.get(i).get(j)+"' WHERE PHONE='"+number+"' AND TOPIC ='"+topic+"'";
															stmt = conn.createStatement();				// creates a new statement object
															stmt.executeUpdate(statement);				// executes statement
															conn.commit();								// commits changes
														}
														else{
															log.append("At line:"+(i+1)+": ["+number+"] value: ["+excelData.get(i).get(j)+"] Not in interval [1-5]\n");
														}
														break;
													case "M1":
														log.append("At line:"+(i+1)+": ["+number+"] value: ["+excelData.get(i).get(j)+"] M1 shouldn't be changed it will be generated\n");
														break;
													case "M2":
														log.append("At line:"+(i+1)+": ["+number+"] value: ["+excelData.get(i).get(j)+"] M2 shouldn't be changed it will be generated\n");
														break;
													case "M3":
														log.append("At line:"+(i+1)+": ["+number+"] value: ["+excelData.get(i).get(j)+"] M3 shouldn't be changed it will be generated\n");
														break;
													case "PUOU":
														log.append("At line:"+(i+1)+": ["+number+"] value: ["+excelData.get(i).get(j)+"] PUOU shouldn't be changed it will be generated\n");
														break;
													case "KFA":
														log.append("At line:"+(i+1)+": ["+number+"] value: ["+excelData.get(i).get(j)+"] KFA shouldn't be changed it will be generated\n");
														break;
													case "NAME":
														if( excelData.get(i).get(j).length() > 1 ){
															String statement = "UPDATE "+DB_TABLE_NAME+" SET "+excelData.get(0).get(j)+
																	"='"+excelData.get(i).get(j)+"' WHERE PHONE='"+number+"' AND TOPIC ='"+topic+"'";
															stmt = conn.createStatement();				// creates a new statement object
															stmt.executeUpdate(statement);				// executes statement
															conn.commit();								// commits changes
														}
														else
															log.append("At line:"+(i+1)+": ["+number+"] value: ["+excelData.get(i).get(j)+"] Name needs to be atleast 2 characters long\n");
														break;
													case "PHONE":
														if( excelData.get(i).get(j).matches("[0-9]+") ){
															String statement = "UPDATE "+DB_TABLE_NAME+" SET "+excelData.get(0).get(j)+
																	"='"+excelData.get(i).get(j)+"' WHERE PHONE='"+number+"' AND TOPIC ='"+topic+"'";
															stmt = conn.createStatement();				// creates a new statement object
															stmt.executeUpdate(statement);				// executes statement
															conn.commit();								// commits changes
														}
														else
															log.append("At line:"+(i+1)+": ["+number+"] value: ["+excelData.get(i).get(j)+"] Phone number can only contain digits\n");
														break;
													case "TOPIC":
														boolean isTopic = false;
														for( String validTopic : treeSetTopics ){
															if( excelData.get(i).get(j).equals(validTopic) ){
																isTopic = true;
																break;
															}
														}
														if( isTopic ){
															String statement = "UPDATE "+DB_TABLE_NAME+" SET "+excelData.get(0).get(j)+
																	"='"+excelData.get(i).get(j)+"' WHERE PHONE='"+number+"' AND TOPIC ='"+topic+"'";
															stmt = conn.createStatement();				// creates a new statement object
															stmt.executeUpdate(statement);				// executes statement
															conn.commit();								// commits changes
														}
														else{
															log.append("At line:"+(i+1)+": ["+number+"] value: ["+excelData.get(i).get(j)+"] Must be a TOPIC:\n");
															int counter = 0;
															for( String validTopic : treeSetTopics ){
																if( counter == treeSetTopics.size() )
																	log.append(validTopic+"]\n");
																else if( counter == 0 )
																	log.append("["+validTopic+", ");
																else
																	log.append(validTopic+", ");
																counter++;
															}
														}
														break;
													case "SUBMITDATE":
														log.append("At line:"+(i+1)+": ["+number+"] value: ["+excelData.get(i).get(j)+"] Submit date shouldn't be changed\n");
														break;
													case "OU":
														if( Double.parseDouble(excelData.get(i).get(j)) >= 0 && Double.parseDouble(excelData.get(i).get(j)) <= 2 ){
															// creates sql statement for inserting value into database
															String statement = "UPDATE "+DB_TABLE_NAME+" SET "+excelData.get(0).get(j)+
																	"='"+excelData.get(i).get(j)+"' WHERE PHONE='"+number+"' AND TOPIC ='"+topic+"'";
															stmt = conn.createStatement();				// creates a new statement object
															stmt.executeUpdate(statement);				// executes statement
															conn.commit();								// commits changes

															statement = "SELECT PU FROM STUDENT WHERE PHONE='"+number+"' AND TOPIC='"+topic+"'";
															stmt = conn.createStatement();
															ResultSet rs1 = stmt.executeQuery(statement);
															conn.commit();
															while(rs1.next()){
																Double pu = rs1.getDouble("PU");
																Double ou = Double.parseDouble(excelData.get(i).get(j));
																statement = "UPDATE "+DB_TABLE_NAME+" SET PUOU='"+(pu+ou)+"' WHERE PHONE='"+number+"' AND TOPIC='"+topic+"'";
																stmt = conn.createStatement();				// creates a new statement object
																stmt.executeUpdate(statement);				// executes statement
																conn.commit();								// commits changes
															}
														}
														break;
													case "PU":
														if( Double.parseDouble(excelData.get(i).get(j)) >= 1 && Double.parseDouble(excelData.get(i).get(j)) <= 5 ){
															// creates sql statement for inserting value into database
															String statement = "UPDATE "+DB_TABLE_NAME+" SET "+excelData.get(0).get(j)+
																	"='"+excelData.get(i).get(j)+"' WHERE PHONE='"+number+"' AND TOPIC ='"+topic+"'";
															stmt = conn.createStatement();				// creates a new statement object
															stmt.executeUpdate(statement);				// executes statement
															conn.commit();								// commits changes

															statement = "SELECT OU FROM STUDENT WHERE PHONE='"+number+"' AND TOPIC='"+topic+"'";
															stmt = conn.createStatement();
															ResultSet rs1 = stmt.executeQuery(statement);
															conn.commit();
															while(rs1.next()){
																Double ou = rs1.getDouble("OU");
																Double pu = Double.parseDouble(excelData.get(i).get(j));
																statement = "UPDATE "+DB_TABLE_NAME+" SET PUOU='"+(ou+pu)+"' WHERE PHONE='"+number+"' AND TOPIC='"+topic+"'";
																stmt = conn.createStatement();				// creates a new statement object
																stmt.executeUpdate(statement);				// executes statement
																conn.commit();								// commits changes
															}
														}
														break;
													}
												}// closes for statement ( amount of columns - 1 ) times
											}// closes if statment ( number equals )
										}// closes for statement ( amount of row ) times
									}// closes if statement ( number is not null )
								}// closes while statement ( table has contents )
								
								try {
									ModelManager.initModelManager(PERSISTENCE_SET);		// loads a persistence set (connects to h2 database)
									
									log.append("Generating models...\n");
									LOG.info("\n\n///////////////////////////////////\tM1-Clusters\t////////////////////////////////////\n");
									for( String topic : treeSetTopics ){			// generates M1 model for all topics
										try{
											M1.getCluster(topic, "M1-clusters-"+topic, "M1-centroids-"+topic);
										} catch(Throwable t){
											LOG.error(t.getMessage() + " " + t.getCause());
											t.printStackTrace();
										}
									}
									LOG.info("\n\n////////////////////////////////////\tM2-Regression\t////////////////////////////////////\n");
									for( String topic : treeSetTopics ){			// generates M2 model for all topics
										try{
											M2.getRegression(topic, "M2-"+topic);
										} catch(Throwable t){
											LOG.error(t.getMessage() + " " + t.getCause());
											t.printStackTrace();
										}
									}
									LOG.info("\n\n////////////////////////////////////\tM3-Regression\t////////////////////////////////////\n");
									for( String topic : treeSetTopics ){			// generates M3 model for all topics
										try{
											M3.getRegression(topic, "M3-"+topic);
										} catch(Throwable t){
											LOG.error(t.getMessage() + " " + t.getCause());
											t.printStackTrace();
										}
									}
									LOG.info("\n\n////////////////////////////////////\tPREDICTION\t////////////////////////////////////\n");
									for( String topic : treeSetTopics ){			// generates Prediction model for all topics
										try{
											Prediction.getPrediction(topic);
										} catch(Throwable t){
											LOG.error(t.getMessage() + " " + t.getCause());
											t.printStackTrace();
										}
									}
								} catch (Throwable t) {
									LOG.error(t.getMessage() + " " + t.getCause());
									t.printStackTrace();
								}
								finally {
									ModelManager.closeModelManager();			// closes connection to database
									log.append("Finished generating models\n");
								}
								
								log.append("Finished updating database\n");
								long end = System.nanoTime(); 							// gets system time
								long elapsedTime = end - start; 						// gets elapsed time in nanoseconds
								double seconds = (double)elapsedTime / 1000000000.0; 	// converts nanoseconds to seconds
								seconds = M2Table.round(seconds, 3);					// rounds to 3 digits
								log.append("Execution time: "+seconds+" sec\n");
							}catch (SQLException sqle) { //Handle errors for JDBC
								LOG.error(sqle.getMessage()+" "+sqle.getCause());
								sqle.printStackTrace();
							} catch (Exception e) { 	//Handle errors for Class.forName
								LOG.error(e.getMessage()+" "+e.getCause());
								e.printStackTrace();
							}  catch (Throwable t) {
								LOG.error(t.getMessage() + " " + t.getCause());
								t.printStackTrace();
							} finally {
								try {
									if(stmt!=null){
										stmt.close();
									}
									if (conn!=null)
										conn.close();
								} catch (SQLException sqle) {
									LOG.error(sqle.getMessage()+" "+sqle.getCause());
									sqle.printStackTrace();
								}
							}
						}
						else{
							JOptionPane.showMessageDialog( null, "Invalid file type", "Warning !", JOptionPane.INFORMATION_MESSAGE );
						}
					}
				}.start(); // starts thread
			}
			else if( status == JFileChooser.ERROR_OPTION ){
				JOptionPane.showMessageDialog( null, "File chooser error", "Warning !", JOptionPane.INFORMATION_MESSAGE );
			}
		} else if( e.getSource().equals(eraseFromDatabase) ){
			int confirm = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete entries from database?","Warning",JOptionPane.YES_NO_OPTION);
			if(confirm == JOptionPane.YES_OPTION){
				new Thread() { 				// creates a new thread so processes execute consecutively
					public void run() {		// creates run method for thread
						try
						{
							long start = System.nanoTime(); 	// get system time
							log.setText("");
							Class.forName(JDBC_DRIVER);			// initializes JDBC driver							
							log.append("Connecting to database... \n");
							conn = DriverManager.getConnection(DB_URL, USER, PASS);	// establsih connection to database
							conn.setAutoCommit(false);			// sets autocommit to false
							log.append("Connected to database successfully \n");

							if( boxForSpecifics.getSelectedIndex() == 0 && boxForTopics.getSelectedIndex() == 0 ){
								String sql = "TRUNCATE TABLE STUDENT";	// make sql statement
								stmt = conn.createStatement();			// creates a new statement object
								stmt.executeUpdate(sql);
								sql = "ALTER TABLE STUDENT ALTER COLUMN ID RESTART WITH 1";	// make sql statement
								stmt = conn.createStatement();				// creates a new statement object
								stmt.executeUpdate(sql);
								conn.commit();
								sql = "TRUNCATE TABLE MODELMANAGER";
								stmt = conn.createStatement();				// creates a new statement object
								stmt.executeUpdate(sql);
								conn.commit();
								for( String topic : treeSetTopics ){			// adds topics to Jcombobox
									boxForTopics.removeItem(topic);
								}
								treeSetTopics.clear();
								log.append("All students have been deleted\n");
								long end = System.nanoTime(); 				// gets system time
								long elapsedTime = end - start; 			// gets elapsed time in nanoseconds
								double seconds = (double)elapsedTime / 1000000000.0; // converts nanoseconds to seconds
								seconds = M2Table.round(seconds, 3);		// rounds to 3 digits
								log.append("Execution time: "+seconds+" sec\n");
							}
							else if( boxForSpecifics.getSelectedIndex() == 0 ){
//								System.out.println(" compasdasd count: " + boxForTopics.getItemCount());
//								System.out.println(" comp count: " + boxForSpecifics.getItemCount());
								for( ArrayList<String> data : deleteList ){
									String name = data.get(0);
									String phone = data.get(1);
									String topic = data.get(2);
									String sql = "DELETE FROM STUDENT WHERE PHONE='"+phone+"' AND TOPIC='"+topic+"'";
									stmt = conn.createStatement();
									stmt.executeUpdate(sql);
									conn.commit();
									boxForSpecifics.removeItem(name);
									if( boxForSpecifics.getItemCount() == 1 ){
										sql = "DELETE FROM MODELMANAGER WHERE KEY LIKE '%"+topic+"'";
										stmt = conn.createStatement();				// creates a new statement object
										stmt.executeUpdate(sql);
										conn.commit();
										boxForTopics.removeItem(topic);
									}
									log.append("Name: "+name+" Topic: "+topic+" Phone: "+phone+" has been deleted\n");
								}
								long end = System.nanoTime(); 				// gets system time
								long elapsedTime = end - start; 			// gets elapsed time in nanoseconds
								double seconds = (double)elapsedTime / 1000000000.0; // converts nanoseconds to seconds
								seconds = M2Table.round(seconds, 3);		// rounds to 3 digits
								log.append("Execution time: "+seconds+" sec\n");
							}
							else{
								int id = (boxForSpecifics.getSelectedIndex()-1);
								String name = deleteList.get(id).get(0);
								String phone = deleteList.get(id).get(1);
								String topic = deleteList.get(id).get(2);
								String sql = "DELETE FROM STUDENT WHERE PHONE='"+phone+"' AND TOPIC='"+topic+"'";
								stmt = conn.createStatement();
								stmt.executeUpdate(sql);
								conn.commit();
								boxForSpecifics.removeItem(name);
								if( boxForSpecifics.getItemCount() == 1 ){
									sql = "DELETE FROM MODELMANAGER WHERE KEY LIKE '%"+topic+"'";
									stmt = conn.createStatement();				// creates a new statement object
									stmt.executeUpdate(sql);
									conn.commit();
									boxForTopics.removeItem(topic);
								}
								log.append("Name: "+name+" Topic: "+topic+" Phone: "+phone+" has been deleted\n");
								long end = System.nanoTime(); 				// gets system time
								long elapsedTime = end - start; 			// gets elapsed time in nanoseconds
								double seconds = (double)elapsedTime / 1000000000.0; // converts nanoseconds to seconds
								seconds = M2Table.round(seconds, 3);		// rounds to 3 digits
								log.append("Execution time: "+seconds+" sec\n");
							}
						} catch ( ClassNotFoundException cnfe ){
							LOG.error(cnfe.getMessage()+" "+cnfe.getCause());
							cnfe.printStackTrace();
						} catch ( SQLException sqle ){
							LOG.error(sqle.getMessage()+" "+sqle.getCause());
							sqle.printStackTrace();
						} finally{
							try{
								if( conn != null ){
									conn.close();
								}
								if( stmt != null ){
									stmt.close();
								}
							} catch(SQLException sqle){
								LOG.error(sqle.getMessage()+" "+sqle.getCause());
								sqle.printStackTrace();
							}
						}
					}
				}.start();
			} else {
				try{
					if( conn != null ){
						conn.close();
					}
					if( stmt != null ){
						stmt.close();
					}
				} catch(SQLException sqle){
					LOG.error(sqle.getMessage()+" "+sqle.getCause());
					sqle.printStackTrace();
				}
			}
		}//////

		else if(e.getSource().equals(exampleFile)){			// if exampleFile button is pressed
			File file = new File("data/example.xls");		// creates file path to example.xls file
			openFile(file);									// function that opens file with its default program
		}
		else if( e.getSource().equals(exportAllData) ){		// if exportAllData button is pressed
			new Thread() { 				// creates a new thread so processes execute consecutively
				public void run() {		// creates run method for thread
					long start = System.nanoTime(); 	// gets system time
					File pathToFile = new File("Exported.xls");
					exportAllData(pathToFile, conn, stmt);
					long end = System.nanoTime(); 			// gets system time
					long elapsedTime = end - start; 		// gets elapsed time in nanoseconds
					double seconds = (double)elapsedTime / 1000000000.0; // converts nanoseconds to seconds
					seconds = M2Table.round(seconds, 3);	// rounds to 3 digits
					log.append("Execution time: "+seconds+" sec\n");
				}
			}.start();
		}
		else if( e.getSource().equals(openExported) ){		// if openExportd button is pressed
			File pathToFile = new File("Exported.xls");		// creates new path to file
			openFile(pathToFile);							// opens file with its default programm
		}

		else if( e.getSource().equals(boxForTopics) ){
			new Thread() { 				// creates a new thread so processes execute consecutively
				public void run() {		// creates run method for thread
					if( !((String)boxForTopics.getSelectedItem()).equals("ALL") ){
						boxForSpecifics.removeAllItems();				// removes all elements from JComboBox
						boxForSpecifics.addItem("All");					// adds ALL to JComboBox
						deleteList.removeAll(deleteList);				// removes all elements from ArrayList
						try
						{
							Class.forName(JDBC_DRIVER);		// initializes JDBC driver		
							log.setText("");
							log.append("Connecting to database... \n");
							conn = DriverManager.getConnection(DB_URL, USER, PASS);	// establsih connection to database
							conn.setAutoCommit(false);		// sets autocommit to false
							log.append("Connected to database successfully \n");
							log.append("Reading from database...\n");
							String sql = null;
							ResultSet rs;
							for( String treeTopic : treeSetTopics ){
								if( ((String)boxForTopics.getSelectedItem()).equals(treeTopic) ){

									sql = "SELECT PHONE, TOPIC, NAME FROM STUDENT WHERE TOPIC='"+treeTopic+"'";
									stmt = conn.createStatement();				// creates a new statement object
									rs = stmt.executeQuery(sql);				// a table of data that is obtained by executing a sql statement
									conn.commit();		
									while( rs.next() ){
										ArrayList<String> data = new ArrayList<String>();	// creates an arrayList of strings
										String name = rs.getString("NAME");		// gets name from database
										String phone = rs.getString("PHONE");	// gets phone number from database
										String topic = rs.getString("TOPIC");	// gets topic from database
										boxForSpecifics.addItem(name);	// adds name to boxForSpecifics

										data.add(name);		// adds name to arrayList
										data.add(phone);	// adds phone to arrayList
										data.add(topic);	// adds topic to arrayList

										deleteList.add(data);	// adds arrayList to arrayList
									}
									log.append("Finished reading!\n");
								}
							}
						} catch ( ClassNotFoundException cnfe ){
							LOG.error(cnfe.getMessage()+" "+cnfe.getCause());
							cnfe.printStackTrace();
						} catch ( SQLException sqle ){
							LOG.error(sqle.getMessage()+" "+sqle.getCause());
							sqle.printStackTrace();
						}
					}
				}
			}.start();
		} ////
	}
}