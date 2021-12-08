 package org.einclusion.model;

import static org.einclusion.model.InstanceManager.retrieveModelInstances;

import static org.einclusion.model.ModelManager.entityManager;
import static org.einclusion.model.ModelManager.transaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.einclusion.frontend.RegressionModel;

//import com.sun.tools.sjavac.Log;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 	Calculates the M3 value
 * 	@author student
 */
public class M3 {
	static final Logger LOG = Logger.getLogger(M3.class);
	//LOG.
	//LOG.Level(Level.ALL);
	static final String QUERY_STRING = "SELECT KFA, PUOU, VOTE from Student where KFA>0 AND PUOU>0 AND OU IS NOT NULL";
	//static final String QUERY_STRING = "SELECT  VOTE from Student";

	/**
	 * 	Function that calculates M3 regression model for students in a specific topic and writes it to database
	 * 	@param topic - name of topic (String)
	 * 	@param regression_key - key needed for unique field in database
	 */
	public static void getRegression(String topic, String regression_key) {
		System.out.println("\\\\\\\\\\\\\\\\\\\\ "+topic+" : "+regression_key+ " \\\\\\\\\\\\\\\\\\\\\n");
		
		calculateKFA(topic);
		LOG.info("***Calculate VOTE before");
		calculateVOTE(topic);
		LOG.info("***Calculate VOTE after");
		
		try {
		String statement = QUERY_STRING + " and Topic is '" + topic + "'";
		Instances data = retrieveModelInstances(statement);					// load data
		data.setClassIndex(data.numAttributes() - 1);
		LinearRegression model = new LinearRegression();					// build model
			model.buildClassifier(data);
			LOG.debug(model);
			System.out.println(model);
			String[] options = new String[2];
			options[0] = "-R"; 				// "range"
			options[1] = "1"; 				// first attribute
			Remove remove = new Remove(); 	// new instance of filter
			remove.setOptions(options); 	// set options
			remove.setInputFormat(data);
			
			Evaluation eval = new Evaluation(data);
			int instances = data.numInstances();
			if( instances > 10)
				instances = 10;
			eval.crossValidateModel(model, data, instances, new Random(1));
			double coefficient = eval.correlationCoefficient();
			double relative = eval.relativeAbsoluteError();
			LOG.info( "Relative absolute error: " + eval.relativeAbsoluteError());
//			System.out.println( "Mean absolute error: " +  eval.meanAbsoluteError());
//			System.out.println( "Root mean squared error: " +  eval.rootMeanSquaredError());
//			System.out.println( "Relative absolute error: " +  eval.relativeAbsoluteError());
//			System.out.println( "Root relative squared error: " +  eval.rootRelativeSquaredError());
//			System.out.println( "Number of instances: " +  eval.numInstances());
		
			try{
				ModelManager.getStringCoefficient(regression_key);
			} catch( NullPointerException npe ){
				ModelManager.setStringCoefficient(regression_key, Integer.MIN_VALUE+"");
			}
			
			String databaseCoefficient = null;
			LOG.info( "Corelation coefficient: " +  eval.correlationCoefficient());
			databaseCoefficient = ModelManager.getStringCoefficient(regression_key);
			LOG.info("Database coefficient: "+databaseCoefficient);
			
			if( databaseCoefficient.equals(Integer.MIN_VALUE+"") ){
				Query query;
				if( model.toString().contains("KFA") ){
					// Save regression coefficients
					LOG.info("databaseCoefficient == null");
					Regression regression = new Regression(model, data);
					ModelManager.setObjectValue(regression_key, regression, coefficient+"", relative+"");
					transaction.begin();
					List<?> students = Student.getStudents(topic);
					RegressionModel rm = new RegressionModel(regression_key, regression.coefficients.toString(),true);
					System.out.println(regression.coefficients.toString());
					Double maxRmValue = RegressionModel.getM3regressionDegree(rm, 25);
					for (Object o: students) {
						Student student = (Student)o;
						Double m3 = RegressionModel.getM3regressionDegree(rm, student.getKFA());
						System.out.println( student.getName() +" "+student.getTopic()+ " m3: "+m3 );
						m3 = m3 / maxRmValue*100;
						student.setM3(m3);
						String sql = "UPDATE STUDENT SET M3='"+student.getM3()+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
						query = entityManager.createNativeQuery(sql);
						query.executeUpdate();
					}
					transaction.commit();
					LOG.info("Changes have been commited to the database successfully\n");
				}
				else{
					Regression regression = new Regression(model, data);
					ModelManager.setObjectValue(regression_key, regression);
					transaction.begin();
					List<?> students = Student.getStudents(topic);
					for (Object o: students) {
						Student student = (Student)o;
						String sql = "UPDATE STUDENT SET M3='-1' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
						query = entityManager.createNativeQuery(sql);
						query.executeUpdate();
					}
					transaction.commit();
					LOG.info("Changes have been commited to the database successfully\n");
					String errorText = "Couldn't generate valid M3 regression model for "+topic+" there are either too less values or the values aren't different enough";
					//EditDatabasePanel.log.append(errorText+"\n");
					//EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
					LOG.warn(errorText+"\n");
					
				}
			} else {
				if( eval.correlationCoefficient() > Double.parseDouble(databaseCoefficient) ){
					LOG.info("Correlation coef > databaseCoefficient");
					Query query;
					if( model.toString().contains("KFA") ){
						// Save regression coefficients
						Regression regression = new Regression(model, data);
						ModelManager.setObjectValue(regression_key, regression, coefficient+"", relative+"");
						transaction.begin();
						List<?> students = Student.getStudents(topic);
						RegressionModel rm = new RegressionModel(regression_key, regression.coefficients.toString(),true);
						System.out.println(regression.coefficients.toString());
						Double maxRmValue = RegressionModel.getM3regressionDegree(rm, 25);
						for (Object o: students) {
							Student student = (Student)o;
							Double m3 = RegressionModel.getM3regressionDegree(rm, student.getKFA());
							LOG.info( student.getName() +" "+student.getTopic()+ " m3: "+m3 );
							m3 = m3 / maxRmValue*100;
							student.setM3(m3);
							String sql = "UPDATE STUDENT SET M3='"+student.getM3()+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
							query = entityManager.createNativeQuery(sql);
							query.executeUpdate();
						}
						transaction.commit();
						LOG.info("Changes have been commited to the database successfully\n");
					}
					else{
						Regression regression = new Regression(model, data);
						ModelManager.setObjectValue(regression_key, regression);
						transaction.begin();
						List<?> students = Student.getStudents(topic);
						for (Object o: students) {
							Student student = (Student)o;
							String sql = "UPDATE STUDENT SET M3='-1' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
							query = entityManager.createNativeQuery(sql);
							query.executeUpdate();
						}
						transaction.commit();
						LOG.info("Changes have been commited to the database successfully\n");
						String errorText = "Couldn't generate valid M3 regression model for "+topic+" there are either too less values or the values aren't different enough";
						//EditDatabasePanel.log.append(errorText+"\n");
						//EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
						LOG.warn(errorText+"\n");
						
					}
				} else {
					LOG.info("Don't calculate new model");
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage() + " " + e.getCause());
			String errorText = "Couldn't build M3:"+topic+" model";
			LOG.warn(errorText+"\n");
			
			//EditDatabasePanel.log.append(errorText+"\n");
			//EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
		}
	}
	
	/**
	 * 	Function that calculates Knowledge flow acceleration
	 * 	@param topic - topic name (String)
	 */
	public static void calculateKFA(String topic){
		try{
		Query query;
		transaction.begin();
		List<?> students = Student.getStudents(topic);
		for (Object o: students) {
			Student student = (Student)o;
			Double KFA = ((student.getIWS() + student.getELE() + student.getELM()) * student.getKLBL()) / 3;
			student.setKFA(KFA);
			String sql = "UPDATE STUDENT SET KFA='"+student.getKFA()+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
			query = entityManager.createNativeQuery(sql);
			query.executeUpdate();
		}
		}
		catch ( Exception e ){
			LOG.error(e.getMessage() + " " + e.getCause());
		} finally {
			if( transaction.isActive() )
				transaction.commit();
		}
	}
	
	public static void calculateVOTE(String topic){
		try{
		Query query;
		transaction.begin();
		List<?> students = Student.getStudents(topic);
		//PrintReader pw = new PrintReader(new FileWriter(
          //      "spambase-L5.txt"));
		List<String>  a = new ArrayList();
		//String dir = M3.class.getProtectionDomain().getCodeSource().getLocation().getFile();//System.getProperty("user.dir");
		File currentDirectory = new File(new File("spambase-L5").getAbsolutePath().replaceAll("\\","//"));
		 Scanner scanner = new Scanner(currentDirectory);

		    // read until end of file (EOF)
		    while (scanner.hasNextLine()) {
		        System.out.println(scanner.nextLine());
		        a.add(scanner.nextLine().trim());
		    }

		    // close the scanner
		    scanner.close();
		    
		    LOG.info(a.get(0)+"^^^");
		
		/*try(
				Path path = Paths.get("spambase-L5.txt");
				
				BufferedReader br = new BufferedReader(new FileReader("spambase-L5.txt"))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        // process the line.
		    	a.add(br.readLine());
		    }
		    // line is not visible here.
		}
		*/
		int i = 0;
	
		for (Object o: students) {
			Student student = (Student)o;
			//Double KFA = ((student.getIWS() + student.getELE() + student.getELM()) * student.getKLBL()) / 3;
			//student.setKFA(KFA);
			int g = Integer.parseInt(a.get(i));
		student.setVote(g);
		
			//String sql = "UPDATE STUDENT SET VOTE='"+student.getVote()+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
			//String sql = "UPDATE STUDENT SET VOTE='"+a.get(7)+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
			
		String sql = "UPDATE STUDENT SET VOTE='"+1234+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
		
			LOG.info("-------+++---"+ a.get(i));
			query = entityManager.createNativeQuery(sql);
			query.executeUpdate();
			
			i++;
		}
		}
		catch ( Exception e ){
			LOG.error(e.getMessage() + " " + e.getCause());
		} finally {
			if( transaction.isActive() )
				transaction.commit();
		}
	}
	
}
