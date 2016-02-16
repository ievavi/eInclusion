package org.einclusion.model;

import java.util.List;
import java.util.Random;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.einclusion.GUI.EditDatabasePanel;
import org.einclusion.frontend.Coefficient;
import org.einclusion.frontend.RegressionModel;

import static org.einclusion.model.InstanceManager.*;
import static org.einclusion.model.ModelManager.*;
import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.filters.unsupervised.attribute.Remove;
/**
 * 
 * 	This class calculates the M2 value 
 *	@author student
 */
public class M2 {
	static final Logger LOG = Logger.getLogger(M2.class);
	static final String QUERY_STRING = "SELECT SWL, SAL, ELM, IWS, ELE, PUOU from Student where "
			+ "SWL>0 and SAL>0 and ELM>0 and IWS>0 and ELE>0 and PUOU>0 AND OU IS NOT null";

	/**
	 * 	Function that calculates M2 regression model for students in a specific topic and writes it to database
	 * 	@param topic - name of topic (String)
	 * 	@param regression_key - key needed for unique field in database
	 */
	public static void getRegression(String topic, String regression_key) {
		System.out.println("\\\\\\\\\\\\\\\\\\\\ "+topic+" : "+regression_key+ " \\\\\\\\\\\\\\\\\\\\\n");
		String statement = QUERY_STRING + " and Topic is '" + topic + "'";
		Instances data = retrieveModelInstances(statement);					// get instances from database
		
		data.setClassIndex(data.numAttributes() - 1);
		LinearRegression model = new LinearRegression();					// create new linear regression model
		try {
			model.buildClassifier(data);									// build model
			LOG.debug(model);
			System.out.println(model);
			String[] options = new String[2];	// options for remove
			options[0] = "-R"; 					// "range"
			options[1] = "1"; 					// first attribute
			Remove remove = new Remove(); 		// new instance of filter
			remove.setOptions(options); 		// set options
			remove.setInputFormat(data);
			
			Evaluation eval = new Evaluation(data);							// new evaluation model for given instances
			int instances = data.numInstances();
			if( instances > 10)
				instances = 10;
			eval.crossValidateModel(model, data, instances, new Random(1));		// crossvalidate model
			double coefficient = eval.correlationCoefficient();				// get correlation coefficient
			double relative = eval.relativeAbsoluteError();
			LOG.info( "Relative absolute error: " + eval.relativeAbsoluteError());
//			System.out.println( "Mean absolute error: " +  eval.meanAbsoluteError());
//			System.out.println( "Root mean squared error: " +  eval.rootMeanSquaredError());
//			System.out.println( "Relative absolute error: " +  eval.relativeAbsoluteError());
//			System.out.println( "Root relative squared error: " +  eval.rootRelativeSquaredError());
//			System.out.println( "Number of instances: " +  eval.numInstances());
			
//			Instance studentb = data.instance(0);
//			pu = model.classifyInstance(studentb);
//			System.out.println("\n\n"+studentb+" : "+ pu +"\n\n");
			
			
			try{
				ModelManager.getStringCoefficient(regression_key);
			} catch( NullPointerException npe ){
				ModelManager.setStringCoefficient(regression_key, Integer.MIN_VALUE+"");
			}
			
			LOG.info( "Corelation coefficient: " +  eval.correlationCoefficient());
			String databaseCoefficient = null;
			databaseCoefficient = ModelManager.getStringCoefficient(regression_key);
			LOG.info("Database coefficient: "+databaseCoefficient);
			
			if( databaseCoefficient.equals(Integer.MIN_VALUE+"") ){
				if( model.toString().contains("SWL") || model.toString().contains("SAL") || model.toString().contains("ELM") || model.toString().contains("IWS") || model.toString().contains("ELE") ){
					// Save regression coefficients
					LOG.info("databaseCoefficient == null");
					Regression regression = new Regression(model, data);
					ModelManager.setObjectValue(regression_key, regression, coefficient+"", relative+"");
					Query query;
					transaction.begin();
					List<?> result = Student.getStudents(topic);
					RegressionModel rm = new RegressionModel(regression_key, regression.coefficients.toString(),true);
					System.out.println("\n"+regression.coefficients.toString()+"\n");
					Double maxswl = (double)5;
					Double maxsal = (double)5;
					Double maxelm = (double)5;
					Double maxiws = (double)5;
					Double maxele = (double)5;

					for( Coefficient c : rm.coefficients ){			// if model contains negative coefficients
						switch( c.name ){
						case"SWL":
							if( Double.parseDouble(c.value) < 0 )
								maxswl = (double)1;
							break;
						case"SAL":
							if( Double.parseDouble(c.value) < 0 )
								maxsal = (double)1;
							break;
						case"ELM":
							if( Double.parseDouble(c.value) < 0 )
								maxelm = (double)1;
							break;
						case"IWS":
							if( Double.parseDouble(c.value) < 0 )
								maxiws = (double)1;
							break;
						case"ELE":
							if( Double.parseDouble(c.value) < 0 )
								maxele = (double)1;
							break;
						default:
							if( Double.parseDouble(c.value) < 0 )
								maxele = (double)1;
							break;
						}
					}

					Double maxRmValue = RegressionModel.getM2regressionDegree(rm, maxswl, maxsal, maxelm, maxiws, maxele);
					for (Object o: result) {
						Student student = (Student)o;
						Double m2 = RegressionModel.getM2regressionDegree(rm, student.getSWL(), student.getSAL(), student.getELM(), student.getIWS(), student.getELE());
						LOG.info(student.getName()+" "+student.getTopic()+" m2: "+m2);
						m2 = m2/maxRmValue*100;
						student.setM2(m2);
						String sql = "UPDATE STUDENT SET M2='"+m2+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
						query = entityManager.createNativeQuery(sql);
						query.executeUpdate();
					}
					LOG.info("Changes have been commited to the database successfully\n");
				}
				else{
					Regression regression = new Regression(model, data);
					ModelManager.setObjectValue(regression_key, regression);
					Query query;
					transaction.begin();
					List<?> result = Student.getStudents(topic);
					for (Object o: result) {
						Student student = (Student)o;
						String sql = "UPDATE STUDENT SET M2='-1' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
						query = entityManager.createNativeQuery(sql);
						query.executeUpdate();
					}
					LOG.info("Changes have been commited to the database successfully\n");
					String errorText = "Couldn't generate valid M2 regression model for "+topic+" there are either too less values or the values aren't different enough";
					EditDatabasePanel.log.append(errorText+"\n");
					EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
				}
			} else {
				if( eval.correlationCoefficient() > Double.parseDouble(databaseCoefficient) ){
					if( model.toString().contains("SWL") || model.toString().contains("SAL") || model.toString().contains("ELM") || model.toString().contains("IWS") || model.toString().contains("ELE") ){
						// Save regression coefficients
						LOG.info("Correlation coef > databaseCoefficient");
						Regression regression = new Regression(model, data);
						ModelManager.setObjectValue(regression_key, regression, coefficient+"", relative+"");
						Query query;
						transaction.begin();
						List<?> result = Student.getStudents(topic);
						RegressionModel rm = new RegressionModel(regression_key, regression.coefficients.toString(),true);
						System.out.println("\n"+regression.coefficients.toString()+"\n");
						Double maxswl = (double)5;
						Double maxsal = (double)5;
						Double maxelm = (double)5;
						Double maxiws = (double)5;
						Double maxele = (double)5;

						for( Coefficient c : rm.coefficients ){			// if model contains negative coefficients
							switch( c.name ){
							case"SWL":
								if( Double.parseDouble(c.value) < 0 )
									maxswl = (double)1;
								break;
							case"SAL":
								if( Double.parseDouble(c.value) < 0 )
									maxsal = (double)1;
								break;
							case"ELM":
								if( Double.parseDouble(c.value) < 0 )
									maxelm = (double)1;
								break;
							case"IWS":
								if( Double.parseDouble(c.value) < 0 )
									maxiws = (double)1;
								break;
							case"ELE":
								if( Double.parseDouble(c.value) < 0 )
									maxele = (double)1;
								break;
							default:
								if( Double.parseDouble(c.value) < 0 )
									maxele = (double)1;
								break;
							}
						}

						Double maxRmValue = RegressionModel.getM2regressionDegree(rm, maxswl, maxsal, maxelm, maxiws, maxele);
						for (Object o: result) {
							Student student = (Student)o;
							Double m2 = RegressionModel.getM2regressionDegree(rm, student.getSWL(), student.getSAL(), student.getELM(), student.getIWS(), student.getELE());
							System.out.println(student.getName()+" "+student.getTopic()+" m2: "+m2);
							m2 = m2/maxRmValue*100;
							student.setM2(m2);
							String sql = "UPDATE STUDENT SET M2='"+m2+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
							query = entityManager.createNativeQuery(sql);
							query.executeUpdate();
						}
						LOG.info("Changes have been commited to the database successfully\n");
					}
					else{
						Regression regression = new Regression(model, data);
						ModelManager.setObjectValue(regression_key, regression);
						Query query;
						transaction.begin();
						List<?> result = Student.getStudents(topic);
						for (Object o: result) {
							Student student = (Student)o;
							String sql = "UPDATE STUDENT SET M2='-1' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
							query = entityManager.createNativeQuery(sql);
							query.executeUpdate();
						}
						LOG.info("Changes have been commited to the database successfully\n");
						String errorText = "Couldn't generate valid M2 regression model for "+topic+" there are either too less values or the values aren't different enough";
						EditDatabasePanel.log.append(errorText+"\n");
						EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
					}
				} else {
					LOG.info("Don't calculate new model");
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage() + " " + e.getCause());
			String errorText = "Couldn't build M2:"+topic+" model";
			EditDatabasePanel.log.append(errorText+"\n");
			EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
		} finally {
			if( transaction.isActive() )
				transaction.commit();
		}
	}

}