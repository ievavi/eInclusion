package org.einclusion.model; 

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger; 

import weka.core.Instances; 
import weka.clusterers.EM; 
import weka.clusterers.SimpleKMeans; 
import weka.clusterers.ClusterEvaluation; 
import weka.clusterers.Clusterer; 
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import static org.einclusion.model.InstanceManager.*; 
import static org.einclusion.model.ModelManager.entityManager;
import static org.einclusion.model.ModelManager.transaction;
/**
 * 	This class is used to work with clusters, calculate centroids and euicldiian distance
 * 	@author student
 */
public class M1 { 
	private static final Logger LOG = Logger.getLogger(M1.class); 
	static final String QUERY_STRING = "SELECT SWL, DS, SAL, ELM, IWS, ELE, PU, OU from STUDENT where " 
			+ "SWL>0 and DS>0 and SAL>0 and ELM>0 and IWS>0 and ELE>0 and PU>0 and OU>=0 and OU IS NOT NULL";

	/**
	 * 	Function that gets amount of clusters from given data and writes it to database
	 * 	@param topic - name of topic (String)
	 * 	@param clusterKey - unique cluster key, so it can be recognised in database (String)
	 */
	public static int getAmountOfClusters(String topic, String clusterKey) { 
		int clusters = 0;
		try {
			String sql = QUERY_STRING + " AND TOPIC ='"+topic+"'";		// gets data from specific topic
			Instances data = retrieveModelInstances(sql);
			ClusterEvaluation eval = new ClusterEvaluation(); 
			Clusterer clusterer = new EM(); 	// new clusterer instance, default options 
			clusterer.buildClusterer(data); 	// build clusterer 
			eval.setClusterer(clusterer); 		// the cluster to evaluate 
			eval.evaluateClusterer(data); 		// data to evaluate the clusterer on 

			clusters = clusterer.numberOfClusters();			// gets the number of clusters
			//ModelManager.setNumberValue(clusterKey, clusters); 	// writes number of clusters to database
			//LOG.info("M1-clusters: " + clusters);
			return clusters;
			
		} catch (Exception e) {
			LOG.error(e.getMessage()+" "+e.getCause());
			return clusters;
		} 
	} 

	/**
	 * 	Function that gets centroids from given data and writes them to database
	 * 	@param topic - name of topic (String)
	 * 	@param clusterKey - unique cluster key, so it can be recognised in database (String)
	 * 	@param centroidKey - unique centroid key, so it can be recognised in database (String)
	 */
	public static void getCentroids(String topic, String clusterKey, String centroidKey, int amount) { 
		int clusters; 
		try {
			String sql = QUERY_STRING + " AND TOPIC ='"+topic+"'";		// gets data from specific topic
			Instances data = retrieveModelInstances(sql); 				// retrieves instances from database
			
			NumericToNominal convert = new NumericToNominal();			// filter for converting numeric values to nominal values
	        String[] options= new String[2];							// options for filter
	        options[0]="-R";											// "range" ( which attributes it will apply to )
	        options[1]="last";  										// range of variables to make numeric (1st, 2nd, last);
	        
	        convert.setOptions(options);								// adds options to filter
	        convert.setInputFormat(data);								// adds inputformat to filter
	      
	        data = Filter.useFilter(data, convert);						// filters instances (data)
			
			// create the model 
			SimpleKMeans kMeans = new SimpleKMeans(); 					// new SimpleKMeans instance
			
//			clusters = ModelManager.getIntValue(clusterKey); 			// gets amount of clusters from database
			clusters = 2;
			
			kMeans.setNumClusters(clusters);							// sets number of clusters for kmeans
			kMeans.buildClusterer(data);
//			SSE = kMeans.getSquaredError();
//			System.out.println("sum of squared errors: "+kMeans.getSquaredError());
			
			try{
				ModelManager.getStringCoefficient(centroidKey);
			} catch( NullPointerException npe ){
				ModelManager.setStringCoefficient(centroidKey, Integer.MIN_VALUE+"");
			}
			
			try{
				String databaseCoefficient = null;
				LOG.info("SSE = "+kMeans.getSquaredError());
				databaseCoefficient = ModelManager.getStringCoefficient(centroidKey);
				LOG.info("Database coefficient: "+databaseCoefficient);
				if( databaseCoefficient.equals(Integer.MIN_VALUE+"") ){
					LOG.info("databaseCoefficient == null");
					ModelManager.setNumberValue(clusterKey, clusters); 			// writes number of cluters to database
					Instances centroids = kMeans.getClusterCentroids(); 		// gets cluster centroids
					StringBuilder sb = new StringBuilder();						// for appending multiple clusters
					for (int i = 0; i < centroids.numInstances(); i++) {		// iterates amount of centroid instances times
						if( i == centroids.numInstances()-1 ){					// if last iteration
							LOG.info("Centroid " + i + ": " + centroids.instance(i).toString());
							sb.append(centroids.instance(i).toString());		// appends centroids to string builer
							ModelManager.setStringValue(centroidKey, sb.toString(), kMeans.getSquaredError()+"");	// writes centroids to database
						}
						else{													// if not last iteration
							LOG.info("Centroid " + i + ": " + centroids.instance(i).toString());
							sb.append(centroids.instance(i).toString()+"|");	// appends centroids to string builder
						}
					}
				} else{
					LOG.info("databaseCoefficient != null");
					if( kMeans.getSquaredError() < Double.parseDouble(databaseCoefficient) ){
						LOG.info( "SSE < databaseCoefficient" );
						ModelManager.setNumberValue(clusterKey, clusters); 	// writes number of cluters to database
						Instances centroids = kMeans.getClusterCentroids(); 		// gets cluster centroids
						StringBuilder sb = new StringBuilder();						// for appending multiple clusters
						for (int i = 0; i < centroids.numInstances(); i++) {		// iterates amount of centroid instances times
							if( i == centroids.numInstances()-1 ){					// if last iteration
								LOG.info("Centroid " + i + ": " + centroids.instance(i).toString());
								sb.append(centroids.instance(i).toString());		// appends centroids to string builer
								ModelManager.setStringValue(centroidKey, sb.toString(), kMeans.getSquaredError()+"");	// writes centroids to database
							}
							else{													// if not last iteration
								LOG.info("Centroid " + i + ": " + centroids.instance(i).toString());
								sb.append(centroids.instance(i).toString()+"|");	// appends centroids to string builder
							}
						}
					} else {
						LOG.info("Don't calculate new model");
					}
				}

			} catch( NullPointerException npe ){
				LOG.warn(npe.getMessage()+" "+npe.getCause());
				npe.printStackTrace();
			}
		} catch( Exception e ){
			LOG.error(e.getMessage()+" "+e.getCause());
			String errorText = "Couldn't build M1:"+topic+" model";
			LOG.warn(errorText+"\n");
			//EditDatabasePanel.log.append(errorText+"\n");
			//EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
		}
	}
	
	/**
	 * 	Function that converts a string of centroids to an ArrayLsit of doubles and returns it
	 *	@param centroid - a string of centroids (String)
	 * 	@return ArrayList of Doubles (cetroids)
	 */
	public static ArrayList<Double> centroidToDouble(String centroid){
		ArrayList<Double> centroidValues = new ArrayList<Double>();	// double arrayList for saving values
		
			String [] centroidValue = centroid.split("\\,");		// splits values by "," ands adds them to string array
			Double CSWL = Double.parseDouble(centroidValue[0]);
			Double CDS = Double.parseDouble(centroidValue[1]);
			Double CSAL = Double.parseDouble(centroidValue[2]);
			Double CELM = Double.parseDouble(centroidValue[3]);
			Double CIWS = Double.parseDouble(centroidValue[4]);
			Double CELE = Double.parseDouble(centroidValue[5]);
			Double CPU = Double.parseDouble(centroidValue[6]);
			Double COU = Double.parseDouble(centroidValue[7]);
			
			centroidValues.add(CSWL);
			centroidValues.add(CDS);
			centroidValues.add(CSAL);
			centroidValues.add(CELM);
			centroidValues.add(CIWS);
			centroidValues.add(CELE);
			centroidValues.add(CPU);
			centroidValues.add(COU);
		
		return centroidValues;
	}
	
	/**
	 * 	Funtion that returns shortest Euclidian distance and centroid
	 *	@param SWL - student willingness to learn (double)
	 * 	@param DS - digital skills (double)
	 * 	@param SAL - student ability to learn (double)
	 * 	@param ELM - e-learning materials (double)
	 * 	@param IWS - instructors willingnes to share (double)
	 * 	@param ELE - e-learning environment (double)
	 * 	@param PU - predicted usage (double)
	 * 	@param centroidKey - unique centroid key, so it can be recognised in database (String)
	 * 	@return centroid + "|" + euclidian distance (String)
	 */
	public static String calculateEuclidianDistance(double SWL, double DS, double SAL, double ELM, double IWS, double ELE, double PU, String centroidKey){
		
		String centroids = ModelManager.getStringValue(centroidKey);
		
		Double shortestDistance = Double.MAX_VALUE;
		Double centroidOU = (double)-1;
		
		if( centroids.contains("|") ){
			String [] splitCentroids = centroids.split("\\|");
			for( String centroid : splitCentroids ){
				ArrayList<Double> values = centroidToDouble(centroid);
				Double CSWL = values.get(0);
				Double CDS = values.get(1);
				Double CSAL = values.get(2);
				Double CELM = values.get(3);
				Double CIWS = values.get(4);
				Double CELE = values.get(5);
				Double CPU = values.get(6);
				Double COU = values.get(7);
				// calculates euiclidian distance sqrt((pn - qn)^2)
				double euclidianDistance = Math.sqrt( Math.pow((CSWL-SWL), 2) + Math.pow((CDS-DS), 2) + Math.pow((CSAL-SAL), 2) + Math.pow((CELM-ELM), 2)+
										   Math.pow((CIWS-IWS), 2) + Math.pow((CELE-ELE), 2) + Math.pow((CPU-PU), 2));
				if( euclidianDistance < shortestDistance ){		// gets shortest euclidian distance
					shortestDistance = euclidianDistance;
					centroidOU = COU;
				}
			}
		} else {
			ArrayList<Double> values = centroidToDouble(centroids);
			Double CSWL = values.get(0);
			Double CDS = values.get(1);
			Double CAL = values.get(2);
			Double CELM = values.get(3);
			Double CIWS = values.get(4);
			Double CELE = values.get(5);
			Double CPU = values.get(6);
			Double COU = values.get(7);
			// calculates euiclidian distance sqrt((pn - qn)^2)
			double euclidianDistance = Math.sqrt( Math.pow((SWL-CSWL), 2) + Math.pow((DS-CDS), 2) + Math.pow((SAL-CAL), 2) + Math.pow((ELM-CELM), 2)+
									   Math.pow((IWS-CIWS), 2) + Math.pow((ELE-CELE), 2) + Math.pow((PU-CPU), 2));
			if( euclidianDistance < shortestDistance ){		// gets shortest euclidian distance
				shortestDistance = euclidianDistance;
				centroidOU = COU;
			}
		}
		
		if( shortestDistance != Double.MAX_VALUE )			// if shortestDistance is not first initialized value 
			return centroidOU+"|"+shortestDistance;
		else												// if it is error text
			return "Couldnt calculate shortest distance";
	}
	
	/**
	 * 	Function that calculates each student shortest euclidian distance and writes COU to database
	 * 	@param topic - name of topic (String)
	 * 	@param clusterKey - unique cluster key, so it can be recognised in database (String)
	 * 	@param centroidKey - unique centroid key, so it can be recognised in database (String)
	 */
	public static void getCluster(String topic, String clusterKey, String centroidKey){
		System.out.println("\\\\\\\\\\\\\\\\\\\\ "+topic+" : "+clusterKey+" : "+centroidKey+" \\\\\\\\\\\\\\\\\\\\\n");
		int clusters = 0;
		clusters = getAmountOfClusters(topic, clusterKey);			// gets amount of clusters and writes it to database
		
		LOG.info("M1-clusters: " + clusters); 
		
		getCentroids(topic, clusterKey, centroidKey, clusters);	// gets centroids and writes them to database
		
		
		
		try{
		Query query;
		transaction.begin();
		
		List<?> result = Student.getStudents(topic);			// gets a list of students in a specific topic from database
		for (Object o: result) {
			Student student = (Student)o;
			// gets centroidOU (last centroid OU) and shortest euclidian distance for student
			String centroidDistance = calculateEuclidianDistance(student.getSWL(), student.getDS(), student.getSAL(), student.getELM(),
																 student.getIWS(), student.getELE(), student.getPU(), centroidKey );
			if( centroidDistance.contains("|") ){			// if there are valid results
				String [] split = centroidDistance.split("\\|");
				Double centroidOU = Double.parseDouble(split[0]);
				LOG.info("centroidOU###: "+centroidOU);
				Double distance = Double.parseDouble(split[1]);
				LOG.info("distance###: "+centroidOU);
				LOG.info(student.getName()+" "+student.getTopic() + " shortest euclidian distance: "+ distance);
				String sql = "UPDATE STUDENT SET M1='"+(int)Math.round(centroidOU)+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
				query = entityManager.createNativeQuery(sql);
				query.executeUpdate();
			} else {										// if there aren't valid results
				String errorText = "###Couldnt calculate shortest euclidian distance for student: "+student.getName()+" topic: "+student.getTopic();
				//EditDatabasePanel.log.append(errorText+"\n");
				//EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
				LOG.warn(errorText+"\n");
				LOG.warn(centroidDistance);
			}
			///////////////
			/*
			List<?> result2 = Student.getStudentsforPrediction(topic);			// gets a list of students in a specific topic from database
			for (Object o2: result2) {
				Student student2 = (Student)o2;
				// gets centroidOU (last centroid OU) and shortest euclidian distance for student
				String centroidDistance2 = calculateEuclidianDistance(student2.getSWL(), student2.getDS(), student2.getSAL(), student2.getELM(),
																	 student2.getIWS(), student2.getELE(), student2.getPU(), centroidKey );
				if( centroidDistance2.contains("|") ){			// if there are valid results
					String [] split = centroidDistance.split("\\|");
					Double centroidOU = Double.parseDouble(split[0]);
					LOG.info("centroidOU###: "+centroidOU);
					Double distance = Double.parseDouble(split[1]);
					LOG.info("distance###: "+centroidOU);
					LOG.info(student2.getName()+" "+student2.getTopic() + " shortest euclidian distance: "+ distance);
					String sql = "UPDATE STUDENT SET M1='"+(int)Math.round(centroidOU)+"' WHERE PHONE='"+student2.getPhone()+"' AND TOPIC='"+student2.getTopic()+"'";
					query = entityManager.createNativeQuery(sql);
					query.executeUpdate();
				} else {										// if there aren't valid results
					String errorText = "###Couldnt calculate shortest euclidian distance for student: "+student2.getName()+" topic: "+student2.getTopic();
					//EditDatabasePanel.log.append(errorText+"\n");
					//EditDatabasePanel.highlight(EditDatabasePanel.log, errorText);
					LOG.warn(errorText+"\n");
					LOG.warn(centroidDistance);
				}
			
			
			//////////////
			
			
			
		}
			*/
			}
		LOG.info("Changes have been commited to the database successfully\n");
		
		} catch( Exception e ){
			LOG.error(e.getMessage()+" "+e.getCause());
		} finally {
			if( transaction.isActive() )
				transaction.commit();
		}
	}

}
