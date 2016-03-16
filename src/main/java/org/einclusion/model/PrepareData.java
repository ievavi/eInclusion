package org.einclusion.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.einclusion.GUI.EditDatabasePanel;

import static org.einclusion.model.ModelManager.*;

/**
 * 	This class is used to add data from the converted .csv file to database
 * 	@author student
 */

public class PrepareData {
	private static final Logger LOG = Logger.getLogger(PrepareData.class);
	
	/**
	 * 	Function that reads a given .csv file and writes students to database
	 * 	@param file - path to a .csv file
	 */
	public static void csv2db(File file) {
		try
		{
			//EditDatabasePanel.log.append("Reading from file: "+file.getName()+"\n");
			Query q;
			transaction.begin();	// starts the transaction
			BufferedReader br = new BufferedReader(new FileReader(file));					// reads the file using buffered reader
			String line;																	// a row from the file
			while ((line = br.readLine()) != null) {										// while rows aren't empty
				String[] value = line.split(",");											// splits the line by ,
				String PHONE = value[0];
				String NAME = value[1];
				String TOPIC = value[2];
				String IWS = value[11];
				Double KLAL = Double.parseDouble(value[12]);
				Double KLBL = Double.parseDouble(value[13]);
				String PU = value[14];
				String submitDate = value[15];
				Double SWL = (Double.parseDouble(value[3]) + Double.parseDouble(value[4])) / 2;	// calculates SWL
				Double DS = (Double.parseDouble(value[5]) + Double.parseDouble(value[6])) / 2;	// calculates DS
				Double ELM = (Double.parseDouble(value[7]) + Double.parseDouble(value[8])) / 2;	// calculates ELM
				Double ELE = (Double.parseDouble(value[9]) + Double.parseDouble(value[10])) / 2;	// calculates ELE
				Double SAL = (double) 0;													    // creates SAL
				if (5 - KLBL == 0) {						// if student cant learning anything new (MAX - KLBL)
					SAL = (double) 0;						// SAL is 0
				} else {									// else if student is able to learn smth new
					if(KLAL > KLBL)
						SAL = (KLAL - KLBL) * 4 / (5 - KLBL);
					else
						SAL = (KLBL - KLAL) * 4 / (5 - KLAL);
				}
				SAL++;										// so SAL max value is 5 and lowest value is 1
				Double KFA = ((Double.parseDouble(IWS) + ELE + ELM) * KLBL) / 3;
				
				// creates a statement that will add one student with the selected values into the database
				//PreparedStatement pst = 
				
				String sql = "UPDATE STUDENT"
						+ " SET NAME=:name, IWS=:iws, KLAL=:klal, KLBL=:klbl, PU = :pu,"
						+ " SUBMITDATE=:submitdate, SWL=:swl, DS=:ds, ELM=:elm, ELE=:ele,"
						+ " SAL=:sal, PUOU=:puou, M1=:m1, M2=:m2, KFA=:kfa, M3=:m3, RELIABILITY=:reliability"
						+ " WHERE PHONE=:phone and TOPIC=:topic";
				q = entityManager.createNativeQuery(sql);
				q.setParameter("name", NAME);
				q.setParameter("iws", IWS);
				q.setParameter("klal", KLAL);
				q.setParameter("klbl", KLBL);
				q.setParameter("pu", PU);
				q.setParameter("submitdate", submitDate);
				q.setParameter("swl", SWL);
				q.setParameter("ds", DS);
				q.setParameter("elm", ELM);
				q.setParameter("ele", ELE);
				q.setParameter("sal", SAL);
				q.setParameter("puou", 0);
				q.setParameter("m1", -1);
				q.setParameter("m2", -1);
				q.setParameter("kfa", KFA);
				q.setParameter("m3", -1);
				q.setParameter("reliability", "not available");
				q.setParameter("phone", PHONE);
				q.setParameter("topic", TOPIC);
				q.executeUpdate();	// executes q statement


				sql = "INSERT INTO STUDENT (Phone,Name,Topic,IWS,KLAL,KLBL,PU,SubmitDate,SWL,DS,"
						+ "ELM,ELE,SAL,PUOU,M1,M2,KFA, M3, RELIABILITY)"
						+ " SELECT :phone, :name, :topic, :iws, :klal, :klbl, :pu, :submitdate,"
						+ " :swl, :ds, :elm, :ele, :sal, :puou, :m1, :m2, :kfa, :m3, :reliability"
						+ " WHERE NOT EXISTS "
						+ " (SELECT PHONE, TOPIC from STUDENT WHERE PHONE=:phone and TOPIC=:topic)";
				
				q = entityManager.createNativeQuery(sql);
				q.setParameter("name", NAME);
				q.setParameter("iws", IWS);
				q.setParameter("klal", KLAL);
				q.setParameter("klbl", KLBL);
				q.setParameter("pu", PU);
				q.setParameter("submitdate", submitDate);
				q.setParameter("swl", SWL);
				q.setParameter("ds", DS);
				q.setParameter("elm", ELM);
				q.setParameter("ele", ELE);
				q.setParameter("sal", SAL);
				q.setParameter("puou", 0);
				q.setParameter("m1", -1);
				q.setParameter("m2", -1);
				q.setParameter("kfa", KFA);
				q.setParameter("m3", -1);
				q.setParameter("reliability", "not available");
				q.setParameter("phone", PHONE);
				q.setParameter("topic", TOPIC);
				q.executeUpdate();	// executes q statement
				
				
				
			//  sql = "INSERT INTO STUDENT (Phone,Name,Topic,IWS,KLAL,KLBL,PU,SubmitDate,SWL,DS,ELM,ELE,SAL,PUOU,M1,M2,KFA, M3, RELIABILITY)"
			//  		+ " SELECT '" + PHONE + "','" + NAME + "','" + TOPIC + "','" + IWS + "','" + KLAL + "','"
			//  		+ KLBL + "','" + PU + "','" + submitDate + "','" + SWL + "','" + DS + "','" + ELM + "','"
			//  		+ ELE + "','" + SAL + "', 0,-1,-1,'"+KFA+"', -1, 'not available' WHERE NOT EXISTS (SELECT PHONE,TOPIC FROM STUDENT WHERE PHONE='" + PHONE
			//  		+ "' " + "AND TOPIC=" + " '" + TOPIC + "')";
			//  q = entityManager.createNativeQuery(sql);
			//  q.executeUpdate();	// executes q statement
			}
			br.close();				// closes buffered reader
			transaction.commit();	// ends the transaction
		} catch( FileNotFoundException fnfe ){
			LOG.error(fnfe.getMessage()+" "+fnfe.getCause());
			fnfe.printStackTrace();
			String errorText = "File not found choose a different path!";
			EditDatabasePanel.log.append(errorText+"\n");
			EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
		} catch( IOException ioe ){
			LOG.error(ioe.getMessage()+" "+ioe.getCause());
			ioe.printStackTrace();
			String errorText = "Exception while writing to file!";
			EditDatabasePanel.log.append(errorText+"\n");
			EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
		}
	}
}
