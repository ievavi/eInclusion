package org.einclusion.model;

import static org.einclusion.model.ModelManager.entityManager;
import static org.einclusion.model.ModelManager.transaction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.einclusion.frontend.Coefficient;
import org.einclusion.frontend.RegressionModel;

public class Prediction {
	static final Logger LOG = Logger.getLogger(Prediction.class);
	static final String QUERY_STRING = "SELECT OU from Student where OU IS null";
	static List<String>  a = new ArrayList();
	/**
	 * 	Calculates M1 and M2 or M3 values using functions saved in database
	 * 	@param topic - name of topic
	 */
	public static void getPrediction(String topic){
		try{
			LOG.info("getPrediction1");
		System.out.println("\\\\\\\\\\\\\\\\\\\\ "+topic+" : PREDICTION \\\\\\\\\\\\\\\\\\\\\n");
		LOG.info("getPrediction2");
		transaction.begin();
		LOG.info("getPrediction3");
		List<?> result = Student.getStudentsforPrediction(topic);
		LOG.info("getPrediction4");
		for (Object o: result) {
			Student student = (Student)o;
			LOG.info("getPrediction5");
			LOG.info(student.getName()+" "+student.getTopic()+" OU: "+ student.getOU());
			LOG.info("getPrediction6");
			calculateM2orM3(topic, student);
			LOG.info("getPrediction7");
		}
		} catch ( Exception e ){
			LOG.error(e.getMessage()+" "+e.getCause());
		} finally {
			if( transaction.isActive() )
				transaction.commit();
		}
		
	}
	
	
	
	/**
	 * 	Function that calculates M2 or M3 value for one student and writes it to database
	 * 	@param topic - topic name (String)
	 * 	@param student - student object (Student)
	 */
	public static void calculateM2orM3(String topic, Student student){
		try{
			
			LOG.info("Prediction201");
			String m2coefficient = ModelManager.getStringCoefficient("M2-"+topic);
			LOG.info("Prediction202");
			//String m3coefficient = ModelManager.getStringCoefficient("M3-"+topic);
			LOG.info("Prediction203");
			String m2relative = ModelManager.getRelativeCoefficient("M2-"+topic);
			LOG.info("Prediction204");
			//String m3relative = ModelManager.getRelativeCoefficient("M3-"+topic);
			LOG.info("Prediction205");
			Query query;
			LOG.info("Prediction206");
			if( m2coefficient == null ) {//&& m3coefficient == null ){
				LOG.info("Prediction207");
				LOG.warn("Cant calculate M2 and M3 values, both null");
			}
			
			else if( m2coefficient == null ){  //-1
				LOG.info("Prediction208");
				LOG.info("log1");
				String centroidDistance = M1.calculateEuclidianDistance(student.getSWL(), student.getDS(), student.getSAL(), student.getELM(),
						student.getIWS(), student.getELE(), student.getPU(), "M1-centroids-"+topic );
				if( centroidDistance.contains("|") ){			// if there are valid results
					String [] split = centroidDistance.split("\\|");
					Double centroidOU = Double.parseDouble(split[0]);
					Double distance = Double.parseDouble(split[1]);
					LOG.info(student.getName()+" "+student.getTopic() + " shortest euclidian distance: "+ distance);
					student.setM1((int)Math.round(centroidOU));
					LOG.info("log1");
					String sql = "UPDATE STUDENT SET M1='"+(int)Math.round(centroidOU)+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
					LOG.info("log1");
					query = entityManager.createNativeQuery(sql);
					query.executeUpdate();
				} else {			
					LOG.info("Prediction209");
					// if there aren't valid results
					String errorText = "Couldnt calculate shortest euclidian distance for student: "+student.getName()+" topic: "+student.getTopic();
					//EditDatabasePanel.log.append(errorText+"\n");
					//EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
					LOG.warn(errorText+"\n");
					LOG.warn(centroidDistance);
				}
				
				/*
				String m3equation = ModelManager.getStringValue("M3-"+topic);
				LOG.info("Prediction210");
				if( m3equation.contains("KFA") ){
					LOG.info("Prediction211");
					Double KFA = ((student.getIWS() + student.getELE() + student.getELM()) * student.getKLBL()) / 3;
					LOG.info("Prediction212");
					student.setKFA(KFA);
					String sql = "UPDATE STUDENT SET KFA='"+student.getKFA()+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
					query = entityManager.createNativeQuery(sql);
					query.executeUpdate();
					LOG.info("Prediction213");
					m3equation = Prediction.convertValue(m3equation);
					RegressionModel rm3 = new RegressionModel("M3-"+topic, m3equation, true);
					Double maxRmValue = RegressionModel.getM3regressionDegree(rm3, 25);
					LOG.info("Prediction215");
					//Double maxRmValue = RegressionModel.getM2regressionDegreeMax(rm2);// maxswl, maxsal, maxelm, maxiws, maxele);
					
					Double m3 = RegressionModel.getM3regressionDegree(rm3, student.getKFA());
					LOG.info("Prediction216");
					LOG.info( student.getName() +" "+student.getTopic()+ " m2=null m3: "+m3 );
					m3 = m3 / maxRmValue*100;
					student.setM3(m3);
					int i = 0;
					student.setVote(Integer.valueOf(a.get(i)));
					LOG.info("Prediction217");
					sql = "UPDATE STUDENT SET M3='"+student.getM3()+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
					//sql = "UPDATE STUDENT SET VOTE="+student.getVote()+" WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
					i++;
					query = entityManager.createNativeQuery(sql);
					query.executeUpdate();
					LOG.info("Prediction218");
				} else{
					LOG.info("Prediction219");
					LOG.warn("Inavlid m3 equation doesnt contain coefficient");
				}
				LOG.info("Prediction220");
				
				*/
				String reliability = calculateReliability(student.getM1(), toInt(student.getM3()));
				LOG.info("Prediction221");
				LOG.info("Name: "+student.getName()+" Topic: "+student.getTopic()+" M1: "+student.getM1()+" M3: "+toInt(student.getM3()));
				String sql = "UPDATE STUDENT SET RELIABILITY='"+reliability+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
				LOG.info("Prediction222");
				query = entityManager.createNativeQuery(sql);
				query.executeUpdate();
				LOG.info("Prediction223");
			} 
			
			//else if ( m3coefficient == null )
			{
				LOG.info("log1");
				String centroidDistance = M1.calculateEuclidianDistance(student.getSWL(), student.getDS(), student.getSAL(), student.getELM(),
						student.getIWS(), student.getELE(), student.getPU(), "M1-centroids-"+topic );
				LOG.info("log1");
				if( centroidDistance.contains("|") ){			// if there are valid results
					String [] split = centroidDistance.split("\\|");
					Double centroidOU = Double.parseDouble(split[0]);
					Double distance = Double.parseDouble(split[1]);
					LOG.info(student.getName()+" "+student.getTopic() + " shortest euclidian distance: "+ distance);
					student.setM1((int)Math.round(centroidOU));
					LOG.info("log1");
					String sql = "UPDATE STUDENT SET M1='"+(int)Math.round(centroidOU)+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
					query = entityManager.createNativeQuery(sql);
					query.executeUpdate();
					LOG.info("log1");
				} else {				
					LOG.info("Prediction224");// if there aren't valid results
					String errorText = "Couldnt calculate shortest euclidian distance for student: "+student.getName()+" topic: "+student.getTopic();
					//EditDatabasePanel.log.append(errorText+"\n");
					//EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
					LOG.warn(errorText+"\n");
					LOG.warn(centroidDistance);
				}
				
				LOG.info("Prediction225");
				String m2equation =	ModelManager.getStringValue("M2-"+topic);
				if( m2equation.contains("SWL") || m2equation.contains("SAL") || m2equation.contains("ELM") || m2equation.contains("IWS") || m2equation.contains("ELE") ){
					m2equation = Prediction.convertValue(m2equation);
					RegressionModel rm2 = new RegressionModel("M2-"+topic, m2equation, true);
					Double maxswl = (double)5;
					Double maxsal = (double)5;
					Double maxelm = (double)5;
					Double maxiws = (double)5;
					Double maxele = (double)5;
					LOG.info("Prediction226");
					for( Coefficient c : rm2.coefficients ){			// if model contains negative coefficients
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
					LOG.info("Prediction227");
					//Double maxRmValue = RegressionModel.getM2regressionDegree(rm2, maxswl, maxsal, maxelm, maxiws, maxele);
					Double maxRmValue = RegressionModel.getM2regressionDegreeMax(rm2);
					LOG.info("Prediction228");
					Double m2 = RegressionModel.getM2regressionDegree(rm2, student.getSWL(), student.getSAL(), student.getELM(), student.getIWS(), student.getELE());
					LOG.info(student.getName()+" "+student.getTopic()+" m3 null m2: "+m2);
					m2 = m2/maxRmValue*100;
					student.setM2(m2);
					
					LOG.info("Prediction229");
					String sql = "UPDATE STUDENT SET M2='"+m2+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
					query = entityManager.createNativeQuery(sql);
					query.executeUpdate();
					LOG.info("Prediction230");
				} else {
					LOG.info("Prediction231");
					LOG.warn("Invalid m2 equation doesnt contain coefficients");
				}
				LOG.info("Prediction232");
				String reliability = calculateReliability(student.getM1(), toInt(student.getM2()));
				LOG.info("Name: "+student.getName()+" Topic: "+student.getTopic()+" M1: "+student.getM1()+" M2: "+toInt(student.getM2()));
				String sql = "UPDATE STUDENT SET RELIABILITY='"+reliability+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
				query = entityManager.createNativeQuery(sql);
				LOG.info("Prediction233");
				query.executeUpdate();
				
			}
			//else
			{
				LOG.info("Prediction234");
				String centroidDistance = M1.calculateEuclidianDistance(student.getSWL(), student.getDS(), student.getSAL(), student.getELM(),
						student.getIWS(), student.getELE(), student.getPU(), "M1-centroids-"+topic );
				if( centroidDistance.contains("|") ){			// if there are valid results
					LOG.info("log1");
					String [] split = centroidDistance.split("\\|");
					Double centroidOU = Double.parseDouble(split[0]);
					Double distance = Double.parseDouble(split[1]);
					LOG.info(student.getName()+" "+student.getTopic() + " shortest euclidian distance: "+ distance);
					student.setM1((int)Math.round(centroidOU));
					LOG.info("log1");
					String sql = "UPDATE STUDENT SET M1='"+(int)Math.round(centroidOU)+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
					query = entityManager.createNativeQuery(sql);
					query.executeUpdate();
					LOG.info("log1");
				} else {				
					LOG.info("Prediction234");// if there aren't valid results
					String errorText = "Couldnt calculate shortest euclidian distance for student: "+student.getName()+" topic: "+student.getTopic();
					//EditDatabasePanel.log.append(errorText+"\n");
					//EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
					LOG.warn(errorText+"\n");
					LOG.warn(centroidDistance);
				}
				
				LOG.info("Prediction235");
				String m2equation =	ModelManager.getStringValue("M2-"+topic);
				if( m2equation.contains("SWL") || m2equation.contains("SAL") || m2equation.contains("ELM") || m2equation.contains("IWS") || m2equation.contains("ELE") ){
					m2equation = Prediction.convertValue(m2equation);
					RegressionModel rm2 = new RegressionModel("M2-"+topic, m2equation, true);
					Double maxswl = (double)5;
					Double maxsal = (double)5;
					Double maxelm = (double)5;
					Double maxiws = (double)5;
					Double maxele = (double)5;
					LOG.info("Prediction236");
					for( Coefficient c : rm2.coefficients ){			// if model contains negative coefficients
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
					LOG.info("Prediction237");
					//Double maxRmValue = RegressionModel.getM2regressionDegree(rm2, maxswl, maxsal, maxelm, maxiws, maxele);
					Double maxRmValue = RegressionModel.getM2regressionDegreeMax(rm2);
					Double m2 = RegressionModel.getM2regressionDegree(rm2, student.getSWL(), student.getSAL(), student.getELM(), student.getIWS(), student.getELE());
					LOG.info(student.getName()+" "+student.getTopic()+" both not null m2: "+m2);
					m2 = m2/maxRmValue*100;
					student.setM2(m2);
					LOG.info("Prediction238");
					String sql = "UPDATE STUDENT SET M2='"+m2+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
					query = entityManager.createNativeQuery(sql);
					query.executeUpdate();
					LOG.info("Prediction239");
				} else {
					LOG.info("Prediction240");
					LOG.warn("Inavlid m2 equation doesnt containt coefficients");
				}
			
				/*
			String m3equation = ModelManager.getStringValue("M3-"+topic);
				if( m3equation.contains("KFA") ){
					LOG.info("Prediction241");
					Double KFA = ((student.getIWS() + student.getELE() + student.getELM()) * student.getKLBL()) / 3;
					student.setKFA(KFA);
					String sql = "UPDATE STUDENT SET KFA='"+student.getKFA()+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
					query = entityManager.createNativeQuery(sql);
					query.executeUpdate();
					LOG.info("Prediction242");

					m3equation = Prediction.convertValue(m3equation);
					RegressionModel rm3 = new RegressionModel("M3-"+topic, m3equation, true);
					LOG.info("Prediction243");
					Double maxRmValue = RegressionModel.getM3regressionDegree(rm3, 25);
					Double m3 = RegressionModel.getM3regressionDegree(rm3, student.getKFA());
					LOG.info("Prediction244");
					LOG.info( student.getName() +" "+student.getTopic()+ " m2=null m3: "+m3 );
					m3 = m3 / maxRmValue*100;
					student.setM3(m3);
					int i = 0;
					student.setVote(Integer.valueOf(a.get(i)));
					LOG.info("Prediction235");
					sql = "UPDATE STUDENT SET M3='"+student.getM3()+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
					//sql = "UPDATE STUDENT SET VOTE="+student.getVote()+" WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
//i++;
					LOG.info("Prediction236");
					query = entityManager.createNativeQuery(sql);
					query.executeUpdate();
					LOG.info("Prediction237");
				} else{
					LOG.warn("Inavlid m3 equation doesnt contain coefficient");
					LOG.info("Prediction238");
				}
				
				*/
				
				//if( Double.parseDouble(m2coefficient) > Double.parseDouble(m3coefficient) )
				{
				String reliability = calculateReliability(student.getM1(), toInt(student.getM2()));
				LOG.info("Prediction239");
				LOG.info("Name: "+student.getName()+" Topic: "+student.getTopic()+" M1: "+student.getM1()+" M2: "+toInt(student.getM2()));
				String sql = "UPDATE STUDENT SET RELIABILITY='"+reliability+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
				query = entityManager.createNativeQuery(sql);
				query.executeUpdate();
				LOG.info("Prediction240");
				} 
				
				/*
				else if( Double.parseDouble(m2coefficient) == Double.parseDouble(m3coefficient) )
				{
					if( Double.parseDouble(m2relative) <= Double.parseDouble(m3relative) ){
						LOG.info("Prediction240");
						String reliability = calculateReliability(student.getM1(), toInt(student.getM2()));
						LOG.info("Name: "+student.getName()+" Topic: "+student.getTopic()+" M1: "+student.getM1()+" M2: "+toInt(student.getM2()));
						String sql = "UPDATE STUDENT SET RELIABILITY='"+reliability+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
						query = entityManager.createNativeQuery(sql);
						query.executeUpdate();
						LOG.info("Prediction240");
					} else {
						LOG.info("Prediction240");
						String reliability = calculateReliability(student.getM1(), toInt(student.getM3()));
						LOG.info("Name: "+student.getName()+" Topic: "+student.getTopic()+" M1: "+student.getM1()+" M3: "+toInt(student.getM3()));
						String sql = "UPDATE STUDENT SET RELIABILITY='"+reliability+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
						query = entityManager.createNativeQuery(sql);
						query.executeUpdate();
						LOG.info("Prediction240");
					}
				}
				*/
				//else
				{
					LOG.info("Prediction240");
					String reliability = calculateReliability(student.getM1(), toInt(student.getM3()));
					LOG.info("Name: "+student.getName()+" Topic: "+student.getTopic()+" M1: "+student.getM1()+" M3: "+toInt(student.getM3()));
					String sql = "UPDATE STUDENT SET RELIABILITY='"+reliability+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
					query = entityManager.createNativeQuery(sql);
					query.executeUpdate();
					LOG.info("Prediction240");
				}
				

			}
		} catch ( Exception e ){
			
			LOG.info("Prediction240");
			LOG.error(e.getMessage()+" "+e.getCause() );
		}
	}


	/**
	 * 	Function that converts percentage to int
	 * @param value - percentage (double)
	 * @return	int
	 */
	public static int toInt(double value){
		if( value >= 60 && value <= 100 )
			return 2;
		else if ( value >= 25 && value < 60 )
			return 1;
		else if ( value >= 0 && value < 25 )
			return 0;
		else
			return -10;
	}
	
	/**
	 * 	Function that callculates reliability: low, medium or high
	 * 	@param m1 - m1 value (int)
	 * 	@param m - m2 or m3 value (int)
	 * 	@return realiblity (String)
	 */
	public static String calculateReliability(int m1, int m){
		int abs = Math.abs(m1 - m);
		if( abs == 0 )
			return "High";
		else if( abs == 1 )
			return "Medium";
		else if ( abs == 2 )
			return "Low";
		else return "not available";
	}
	
	/**
	 *	Converts equation from database so it can be passed to RegressionModel
	 *	@param value - equation from database (String)
	 */
	public static String convertValue(String value){
		value = value.substring(16, value.length()-1);	// removes {"coefficients": and } from end
		value = value.replaceAll("\"", "");				// replaces all " with blankspace
		value = value.replaceAll(":", "=");				// replaces : with =
		return value;
	}
	
}
