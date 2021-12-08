package org.einclusion.model;

import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.classifiers.Classifier;
import static org.einclusion.model.InstanceManager.*; 
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.lazy.LWL;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.CVParameterSelection;
import weka.classifiers.meta.FilteredClassifier;
//import weka.classifiers.meta.GridSearch;
import weka.classifiers.meta.Vote;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.neighboursearch.LinearNNSearch;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.MathExpression;
import weka.filters.unsupervised.attribute.NumericCleaner;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.attribute.StringToWordVector;

import static org.einclusion.model.InstanceManager.retrieveModelInstances;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.Query;

import jdk.nashorn.internal.codegen.types.NumericType;
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import org.apache.log4j.Logger; 
import org.einclusion.model.Student;

/**
 * @author Gowtham Girithar Srirangasamy
 *
 */
//Random forest demo
public class M0 {
	static final Logger LOG = Logger.getLogger(M0.class);

	/** file names are defined*/
	public static final String TRAINING_DATA_SET_FILENAME="decision-train.arff";
	public static final String TESTING_DATA_SET_FILENAME="decision-test.arff";
	
	/**
	 * This method is to load the data set.
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Instances getDataSet(String fileName,String topic) throws IOException {
		/**
		 * we can set the file i.e., loader.setFile("finename") to load the data
		 */
		
		//Student st = new Student();
		//st.getStudentsforPrediction("*");
		
		 //final String QUERY_STRING = "SELECT SWL, DS, SAL, ELM, IWS, ELE, PU,OU from STUDENT where " 
			//	+ "SWL>0 and DS>0 and SAL>0 and ELM>0 and IWS>0 and ELE>0 and PU>0 and OU is null";
		 final String QUERY_STRING = "SELECT SWL, DS,SAL, ELM, IWS, ELE, PU,OU from STUDENT where " 
					+ "SWL>0 and DS>0 and SAL>0 and ELM>0 and IWS>0 and ELE>0 and PU>0 and OU is null";

		String sql = QUERY_STRING +" AND TOPIC ='"+topic+"'";		// gets data from specific topic
		Instances data = org.einclusion.model.InstanceManager.retrieveModelInstances(sql);
	
		System.out.println(data.numInstances()+ " INSTANCE count !!!!!");
		int classIdx = 1;
		/** the arffloader to load the arff file */
	//	ArffLoader loader = new ArffLoader();
		/** load the traing data */
		//loader.setSource(M0.class.getResourceAsStream("/" + fileName));
		//loader.setSource(M0.class.getResourceAsStream("src/main/java/" + "decision-train.arff"));
		/**
		 * we can also set the file like 
		 **/
	//	 loader.setFile(new
	//	  File("c://temp/decision-test.arff"));
		 
		//loader.setFile(new File(fileName));
		//Instances dataSet = loader.getDataSet();
		/** set the index based on the data given in the arff files */
		//dataSet.setClassIndex(classIdx);
		
		//data.setClassIndex(data.numAttributes() - 1);
		//data.setClassIndex(1);
		data.setClassIndex(data.numAttributes() - 1);
	//	if(fileName.equals("decision-train.arff")) return dataSet; else
		
		///////////////////////////
		//////////////////////////
		////////////////////
		//////////////////////
		////////////////////
		
		Instances fsource = null;
		//StringToWordVector stwv = new StringToWordVector();
		NumericToNominal ntn = new NumericToNominal();
		try {
			String[] options= new String[2];							// options for filter
	        options[0]="-R";											// "range" ( which attributes it will apply to )
	        options[1]="last";  										// range of variables to make numeric (1st, 2nd, last);
	        
	        ntn.setOptions(options);
			ntn.setInputFormat(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
		   data = Filter.useFilter(data, ntn);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    data.setClassIndex(data.numAttributes() - 1);
	   // data.setRelationName("TRAIN_DATA");
	    
	    
		
		return data;// data;//fsource;//data;//dataSet;
	}
	public static Instances getDataSetUnlabeled(String fileName,String topic) throws IOException {
		/**
		 * we can set the file i.e., loader.setFile("finename") to load the data
		 */
		
		//Student st = new Student();
		//st.getStudentsforPrediction("*");
		
		 //final String QUERY_STRING = "SELECT SWL, DS, SAL, ELM, IWS, ELE, PU,OU from STUDENT where " 
			//	+ "SWL>0 and DS>0 and SAL>0 and ELM>0 and IWS>0 and ELE>0 and PU>0 and OU is null";
		 final String QUERY_STRING = "SELECT SWL, DS,SAL, ELM, IWS, ELE, PU,OU from STUDENT where " 
					+ "SWL>0 and DS>0 and SAL>0 and ELM>0 and IWS>0 and ELE>0 and PU>0 and OU is null";

		String sql = QUERY_STRING +" AND TOPIC ='"+topic+"'";		// gets data from specific topic
		Instances data = org.einclusion.model.InstanceManager.retrieveModelInstances(sql);
	
		System.out.println(data.numInstances()+ " INSTANCE count !!!!!");
		int classIdx = 1;
		/** the arffloader to load the arff file */
	//	ArffLoader loader = new ArffLoader();
		/** load the traing data */
		//loader.setSource(M0.class.getResourceAsStream("/" + fileName));
		//loader.setSource(M0.class.getResourceAsStream("src/main/java/" + "decision-train.arff"));
		/**
		 * we can also set the file like 
		 **/
	//	 loader.setFile(new
	//	  File("c://temp/decision-test.arff"));
		 
		//loader.setFile(new File(fileName));
		//Instances dataSet = loader.getDataSet();
		/** set the index based on the data given in the arff files */
		//dataSet.setClassIndex(classIdx);
		
		//data.setClassIndex(data.numAttributes() - 1);
		//data.setClassIndex(1);
		
		
		//data.setClassIndex(data.numAttributes() - 1);
		
		
	//	if(fileName.equals("decision-train.arff")) return dataSet; else
		
		///////////////////////////
		//////////////////////////
		////////////////////
		//////////////////////
		////////////////////
		
	/*	Instances fsource = null;
		
		NumericToNominal ntn = new NumericToNominal();
		try {
			String[] options= new String[2];							
	        options[0]="-R";											
	        options[1]="last";  										
	        
	        ntn.setOptions(options);
			ntn.setInputFormat(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
		   data = Filter.useFilter(data, ntn);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    data.setClassIndex(data.numAttributes() - 1);
	    data.setRelationName("TRAIN_DATA");
	  */  
	    
		
		return data;// data;//fsource;//data;//dataSet;
	}
	
	public static Instances getDataSet2(String fileName,String topic) throws IOException {
		/**
		 * we can set the file i.e., loader.setFile("finename") to load the data
		 */
		
		//Student st = new Student();
		//st.getStudentsforPrediction("*");
		
		// final String QUERY_STRING = "SELECT SWL, DS, SAL, ELM, IWS, ELE, PU, OU from STUDENT where " 
			//	+ "SWL>0 and DS>0 and SAL>0 and ELM>0 and IWS>0 and ELE>0 and PU>0 and OU IS NOT NULL";

		 final String QUERY_STRING = "SELECT SWL, DS,SAL, ELM, IWS, ELE, PU, OU from STUDENT where " 
					+ "SWL>0 and DS>0 and SAL>0 and ELM>0 and IWS>0 and ELE>0 and PU>0 and OU IS NOT NULL";

		String sql = QUERY_STRING +" AND TOPIC ='"+topic+"'";		// gets data from specific topic
		Instances data = InstanceManager.retrieveModelInstances(sql);
	
		System.out.println(data.numInstances()+ "INSTANCE count !!!!!");
		int classIdx = 1;
		/** the arffloader to load the arff file */
	//	ArffLoader loader = new ArffLoader();
		/** load the traing data */
		//loader.setSource(M0.class.getResourceAsStream("/" + fileName));
		//loader.setSource(M0.class.getResourceAsStream("src/main/java/" + "decision-train.arff"));
		/**
		 * we can also set the file like 
		 **/
	//	 loader.setFile(new
	//	  File("c://temp/decision-test.arff"));
		 
		//loader.setFile(new File(fileName));
		//Instances dataSet = loader.getDataSet();
		/** set the index based on the data given in the arff files */
		//dataSet.setClassIndex(classIdx);
		
		//data.setClassIndex(data.numAttributes() - 1);
		//data.setClassIndex(1);
		data.setClassIndex(data.numAttributes() - 1);
		//data.setClassIndex(data.numAttributes() - 1)
		//data.setRelationName("TEST_DATA");
		////////////////////
				
		///////////////////
		Instances fsource = null;
		StringToWordVector stwv = new StringToWordVector();
		NumericToNominal  ntn = new NumericToNominal();
		try {
			String[] options= new String[2];							// options for filter
	        options[0]="-R";											// "range" ( which attributes it will apply to )
	        options[1]="last";  										// range of variables to make numeric (1st, 2nd, last);
	        
	        ntn.setOptions(options);	
	        ntn.setInputFormat(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
		   data = Filter.useFilter(data, ntn);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		data.setClassIndex(data.numAttributes() - 1);
		return data;
	//	if(fileName.equals("decision-train.arff")) return dataSet; else
		//return data;//dataSet;
	}

	public static Instances getDataSetUnlabeled2(String fileName,String topic) throws IOException {
		/**
		 * we can set the file i.e., loader.setFile("finename") to load the data
		 */
		
		//Student st = new Student();
		//st.getStudentsforPrediction("*");
		
		// final String QUERY_STRING = "SELECT SWL, DS, SAL, ELM, IWS, ELE, PU, OU from STUDENT where " 
			//	+ "SWL>0 and DS>0 and SAL>0 and ELM>0 and IWS>0 and ELE>0 and PU>0 and OU IS NOT NULL";

		 final String QUERY_STRING = "SELECT SWL, DS,SAL, ELM, IWS, ELE, PU, OU from STUDENT where " 
					+ "SWL>0 and DS>0 and SAL>0 and ELM>0 and IWS>0 and ELE>0 and PU>0 and OU IS NOT NULL";

		String sql = QUERY_STRING +" AND TOPIC ='"+topic+"'";		// gets data from specific topic
		Instances data = InstanceManager.retrieveModelInstances(sql);
	
		System.out.println(data.numInstances()+ "INSTANCE count !!!!!");
		int classIdx = 1;
		/** the arffloader to load the arff file */
	//	ArffLoader loader = new ArffLoader();
		/** load the traing data */
		//loader.setSource(M0.class.getResourceAsStream("/" + fileName));
		//loader.setSource(M0.class.getResourceAsStream("src/main/java/" + "decision-train.arff"));
		/**
		 * we can also set the file like 
		 **/
	//	 loader.setFile(new
	//	  File("c://temp/decision-test.arff"));
		 
		//loader.setFile(new File(fileName));
		//Instances dataSet = loader.getDataSet();
		/** set the index based on the data given in the arff files */
		//dataSet.setClassIndex(classIdx);
		
		//data.setClassIndex(data.numAttributes() - 1);
		//data.setClassIndex(1);
		data.setClassIndex(data.numAttributes() - 1);
		///data.setRelationName("TEST_DATA");
		////////////////////
				
		///////////////////
		Instances fsource = null;
		StringToWordVector stwv = new StringToWordVector();
		NumericToNominal  ntn = new NumericToNominal();
		try {
			String[] options= new String[2];							// options for filter
	        options[0]="-R";											// "range" ( which attributes it will apply to )
	        options[1]="last";  										// range of variables to make numeric (1st, 2nd, last);
	        
	        ntn.setOptions(options);	
	        ntn.setInputFormat(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
		  // data = Filter.useFilter(data, ntn);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		data.setClassIndex(data.numAttributes() - 1);
		return data;
	//	if(fileName.equals("decision-train.arff")) return dataSet; else
		//return data;//dataSet;
	}

	/**
	 * This method is used to process the input and return the statistics.
	 * 
	 * @throws Exception
	 */
	public static void process() throws Exception {

		Instances trainingDataSet = getDataSet(TRAINING_DATA_SET_FILENAME,"*");
		Instances testingDataSet = getDataSet(TESTING_DATA_SET_FILENAME,"*");
		
		
		/*LWL lwl = new LWL();
		lwl.setKNN(10);
		lwl.buildClassifier(trainingDataSet);
		*/
		
		 LMT lmt = new LMT();
	        lmt.buildClassifier(trainingDataSet);
	        SimpleLogistic sl = new SimpleLogistic();
	        sl.buildClassifier(trainingDataSet);

	
		
		RandomForest forest=new RandomForest();
	//	forest.setNumTrees(1);//10
		
		
		/** */
		forest.buildClassifier(trainingDataSet);
		/**
		 * train the alogorithm with the training data and evaluate the
		 * algorithm with testing data
		 */
		 
		Evaluation eval = new Evaluation(trainingDataSet);
		//eval.evaluateModel(lwl, testingDataSet);
		 eval.evaluateModel(lmt, testingDataSet);
         eval.evaluateModel(sl, testingDataSet);
		eval.evaluateModel(forest, testingDataSet);
		
		
		/** Print the algorithm summary */
		System.out.println("** Decision Tress Evaluation with Datasets **");
		System.out.println(eval.toSummaryString());
		System.out.print(" the expression for the input data as per alogorithm is ");
		System.out.println(forest);
		System.out.println(eval.toMatrixString());
		 System.out.println(eval.toClassDetailsString());
		
	}
	
	public static  Evaluation process2() throws Exception {

		Instances trainingDataSet = getDataSet2(TRAINING_DATA_SET_FILENAME,"STEM");
		Instances testingDataSet = getDataSet(TESTING_DATA_SET_FILENAME,"STEM");//MII
		Instances testingDataSetUnlabeled = getDataSetUnlabeled(TESTING_DATA_SET_FILENAME,"STEM");//MII
		Instances trainingDataSetUnlabeled = getDataSetUnlabeled2(TRAINING_DATA_SET_FILENAME,"STEM");//MII
		Vote vote = new Vote();
		//FilteredClassifier vote = (Filnew Vote();
		// vote.buildClassifier(trainingDataSet);
		 vote.setSeed(1);
		 
		 //vote.MAX_RULE=100;
		
		
		//RandomForest forest=new RandomForest();
		//forest.setNumTrees(100);//10
	
		
		//LWL lwl = new LWL();
		//lwl.setKNN(0);
		//lwl.buildClassifier(trainingDataSet);
		//lwl.setClassifier(forest);
		
		
		// LMT lmt = new LMT();
	    //    lmt.buildClassifier(trainingDataSet);
	        
	    ////    SimpleLogistic sl = new SimpleLogistic();
	     //   sl.buildClassifier(trainingDataSet);

	
		
		/////
	   // String[] options = new String[]{"-p", "0"};
       // Classifier cls = new NaiveBayes();
       // cls.setOptions(options);
       // Classifier[] c = new Classifier[] {cls};
        /////
        
		
		/** */
		//forest.buildClassifier(trainingDataSet);//train
		//vote.buildClassifier(trainingDataSet);//train
		/////
	//	vote.setClassifiers(c);
		/////
		/**
		 * train the alogorithm with the training data and evaluate the
		 * algorithm with testing data
		 */
		 
		Evaluation eval = new Evaluation(trainingDataSet);
		//eval.crossValidateModel(classifierString, data, numFolds, options, random);
		//eval.crossValidateModel(vote, trainingDataSet, 10, new Random(1));
		//eval.evaluateModel(lwl, testingDataSet);
		// eval.evaluateModel(lmt, testingDataSet);
       // eval.evaluateModel(sl, testingDataSet);
		//eval.evaluateModel(forest, testingDataSet);//testing
		//forest.
		//eval.crossValidateModel(vote, trainingDataSet, 10, new Random(1));
		////////////
		 ////////////////////////
	//	 Vote vs = new Vote();
	     //  GridSearch ps = new GridSearch();
	       
	       // vote.setOptions(weka.core.Utils/* w ww.  j  a  v  a2  s. c  om*/
	       
	       //        .splitOptions("-B \"weka.classifiers.trees.RandomForest -I 10 -K 20 -depth 5\" -R MAJ"));
	       ////!!!!!//// vote.setOptions(weka.core.Utils/* w ww.  j  a  v  a2  s. c  om*/
	               // .splitOptions("-B \"weka.classifiers.functions.SimpleLogistic -I 0 -M 500 -H 50 -W 0.0\""));
	        					//  "-B \"weka.classifiers.functions.SimpleLogistic -I 0 -M 500 -H 50 -W 0.0"
	       
	        		//.splitOptions("-B \"weka.classifiers.functions.SimpleLogistic -I 0 -M 500 -H 50 -W 0.0\" -B \"weka.classifiers.lazy.LWL -U 0 -K -1 -A \\\"weka.core.neighboursearch.LinearNNSearch -A \\\\\\\"weka.core.EuclideanDistance -R first-last\\\\\\\"\\ -B \"weka.classifiers.trees.LMT -I -1 -M 15 -W 0.0\" -R MAJ -classifications weka.classifiers.evaluation.output.prediction.PlainText"));
	        //
	       
	       /////////!!!!!!		//.splitOptions("-B \"weka.classifiers.trees.LMT -I -1 -M 15 -W 0.0\""));//stradaa
	      
	       //  .splitOptions("\" -B \"weka.classifiers.trees.LMT -I -1 -M 15 -W 0.0\" -R MAJ"));
	       // "-P 100 -S 1 -I 10 -W \"weka.classifiers.trees.LMT\""
	     
	       
	       ////////////////////////////////////////
	       /*
	        String[] options = new String[8];
	        
	        options[0] = "-B";                                    // "range"
	        options[1] = "weka.classifiers.trees.LMT";
	        
	        options[2] = "-I";                                    // "range"
	        options[3] = "-1";
	        
	        options[4] = "-M";                                    // "range"
	        options[5] = "15"; 
	        
	        options[6] = "-W";                                    // "range"
	        options[7] = "0.0"; 
	        
	        
	        vote.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE, Vote.TAGS_RULES));
	        vote.setOptions(options);
	        */
	        
	        //FilteredClassifier[] cfsArray = new FilteredClassifier[1];
	       Classifier[] cfsArray = new Classifier[3];  
	       LMT lmt = new LMT();
	       SimpleLogistic sl = new SimpleLogistic();
	       LWL lwl = new LWL();
	       RandomForest randomForest = new RandomForest();
	       //FilteredClassifier[] cfsArray = new FilteredClassifier[1];
	       //randomForest.setNumFeatures(0);
	       //randomForest.setSeed(1);
	       //randomForest.setMaxDepth(0);
	       //randomForest.setNumTrees(100);
	       
	      
	       //String [] optionsRF = new String[14];
	       //optionsRF[0] = "-P";
	       //optionsRF[1] = "100";
	       //optionsRF[2] = "-I";
	       //optionsRF[3] = "100";
	       //optionsRF[4] = "-num-slots";
	       //optionsRF[5] = "1";
	       //optionsRF[6] = "-K";
	      // optionsRF[7] = "0";
	      // optionsRF[8] = "-M";
	      // optionsRF[9] = "1.0";
	      // optionsRF[10] = "-V";
	      // optionsRF[11] = "0.001";
	       //optionsRF[12] = "-S";
	       //optionsRF[13] = "1";
	      /// randomForest.setOptions(optionsRF);
	       LinearNNSearch linearNNSearch = new LinearNNSearch();
	       String [] optionsLinearNNSearch =  new String[2];
	       optionsLinearNNSearch[0] = "-A";
	       optionsLinearNNSearch[1] = "weka.core.EuclideanDistance -R first-last";
	       linearNNSearch.setOptions(optionsLinearNNSearch);
	       //lwl.setClassifier(randomForest);
	       //lwl.buildClassifier(trainingDataSet);
	       lwl.setNearestNeighbourSearchAlgorithm(linearNNSearch);
	       
	       
	       String [] options = new String[6];
	       options[0] = "-I";                                    // "range"
	        options[1] = "-1";
	        
	        options[2] = "-M";                                    // "range"
	        options[3] = "15"; 
	        
	        options[4] = "-W";                                    // "range"
	        options[5] = "0.0"; 
	        lmt.setOptions(options);
	        
	        String [] optionsSl = new String[8];
	        
	        optionsSl[0] = "-I";                                    // "range"
	        optionsSl[1] = "0";
	        
	        optionsSl[2] = "-M";                                    // "range"
	        optionsSl[3] = "500"; 
	        
	        optionsSl[4] = "-H";                                    // "range"
	        optionsSl[5] = "50"; 
	        
	        optionsSl[6] = "-W";                                    // "range"
	        optionsSl[7] = "0.0"; 
	        sl.setOptions(optionsSl);
	        
             String [] optionsLwl = new String[4];
	        
           //-B "weka.classifiers.lazy.LWL -U 0 -K -1 -A \"weka.core.neighboursearch.LinearNNSearch -A \\\"weka.core.EuclideanDistance -R first-last\\\"\" 
             //-W weka.classifiers.trees.RandomForest -- -P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1"
  	       
             
	        optionsLwl[0] = "-U";                                    // "range"
	        optionsLwl[1] = "0";
	        
	        optionsLwl[2] = "-K";                                    // "range"
	        optionsLwl[3] = "-1"; 
	       // optionsLwl[3] = "\""; 
	        
	        //optionsLwl[4] = "-W"; //-B                                   // "range"
	       // optionsLwl[5] = "weka.classifiers.trees.RandomForest -- -P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1"; 
	        
	       // optionsLwl[6]=" --";
	       // optionsLwl[6]=" -P";
	       // optionsLwl[7]=" 100";
	        
	       // optionsLwl[8]=" -num-slots";
	       // optionsLwl[9]=" 1";
	        //optionsLwl[10]=" -M";
	       // optionsLwl[11]=" 1.0";
	       // optionsLwl[12]=" -V";
	       // optionsLwl[13]=" 0.001\"";
	        
	        //weka.classifiers.trees.RandomForest -- -I 100 -K 0 -S 1
	       //// optionsLwl[4] = "-A";                                    // "range"
	       //// optionsLwl[5] = "\"weka.core.neighboursearch.LinearNNSearch -A \\\\\\\"weka.core.EuclideanDistance -R first-last\\\\\\\"\\\""; 
	        
	        ////optionsLwl[6] = "-W";                                    // "range"
	        /////optionsLwl[7] = "weka.classifiers.trees.RandomForest  -- -P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1";
	        ///////////////////////////
	        //optionsLwl[8] = "-W";                                    // "range"
	        //optionsLwl[9] = "0.0"; 
	        
	        //optionsLwl[10] = "-W";                                    // "range"
	        //optionsLwl[11] = "0.0"; 
	        
	        //optionsLwl[12] = "-W";                                    // "range"
	        //optionsLwl[13] = "0.0"; 
	        
	        //optionsLwl[14] = "-W";                                    // "range"
	        //optionsLwl[15] = "0.0"; 
	        
	        //optionsLwl[16] = "-W";                                    // "range"
	        //optionsLwl[17] = "0.0"; 
	        
	        //optionsLwl[18] = "-W";                                    // "range"
	        //optionsLwl[19] = "0.0"; 
	        
	        //optionsLwl[20] = "-W";                                    // "range"
	        //optionsLwl[21] = "0.0"; 
	       String[] optionsRF = new String[15];
	      // optionsRF[0]="--";
	       optionsRF[0]="-P";
	       optionsRF[1]="100";
	       optionsRF[2]="-I";
	       optionsRF[3]="100";
	       optionsRF[4]="-num-slots";
	       optionsRF[5]="1";
	       optionsRF[6]="-K";
	       optionsRF[7]="0";
	       optionsRF[8]="-M";
	       optionsRF[9]="1.0";//1.0
	       optionsRF[10]="-V";
	       optionsRF[11]="0.001";
	      // optionsRF[9]="\"";
	       
	       optionsRF[12]="-S";
	       optionsRF[13]="1";
	       optionsRF[14]="-do-not-check-capabilities";
	       /////////////////////////////
	       RandomTree rt = new RandomTree();
	       String[] optionsRT = new String[8];	
	       optionsRT[0]="-K";
	       optionsRT[1]="0";
	       optionsRT[2]="-M";
	       optionsRT[3]="1.0";//1.0
	       optionsRT[4]="-V";
	       optionsRT[5]="0.001";
	      // optionsRF[9]="\"";
	       
	       optionsRT[6]="-S";
	       optionsRT[7]="1";
	       rt.setOptions(optionsRT);
	       /////////////////////////////
	      // Bagging b = new Bagging();
	      // b.setNumIterations(100);
	       //randomForest.setNumTrees(100);
	      // b.setClassifier(randomForest);
	      //randomForest.setMaxDepth(100);
	       
	      /// randomForest.setNumFeatures(0);//k
	      /// randomForest.setSeed(1); //-s
	      // randomForest.setMaxDepth(0);
	       ///randomForest.setNumTrees(100);   //  I
	       
	       // setup classifier
	      // CVParameterSelection pss = new CVParameterSelection();
	      // pss.setClassifier(new RandomForest());
	      // pss.setNumFolds(10);  // using 10-fold CV
	      // pss.addCVParameter(" -- -P 100 -num-slots 1 -M 1.0 -V 0.001");
	      // pss.buildClassifier(trainingDataSet);
	      // System.out.println(Utils.joinOptions(pss.getBestClassifierOptions()));
	      // lwl.setClassifier(pss);

	       //RandomForestClassifier rfc = new RandomForestClassifier();
	       
	     //  classifier = ensemble.RandomForestClassifier(n_estimators=300,  max_depth=30, min_samples_leaf=1, min_samples_split=1, random_state=1, bootstrap=True, criterion='entropy', n_jobs=-1);
	       //classifier = trainingDataSetRandomForestClassifier(n_estimators=300,  max_depth=30, min_samples_leaf=1, min_samples_split=1, random_state=1, bootstrap=True, criterion='entropy', n_jobs=-1);
	    // FilteredClassifier cls = (RandomForest)new FilteredClassifier();
	     //cls.setClassifier(randomForest);
	     ///cls.setOptions(optionsRF);
	    // -- -P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1");
	       //FilteredClassifier cls = (FilteredClassifier) weka.core.SerializationHelper.read("weka.classifiers.trees.RandomForest -- -P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1");
	      // Classifier cls = (Classifier) weka.core.SerializationHelper.read("bosom.100k.2.j48.MODEL");
	      // weka.classifiers.trees.RandomForest -- -P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1
	     //  RandomForest cls = (RandomForest) weka.core.SerializationHelper.read("weka.classifiers.trees.RandomForest");
	       randomForest.setOptions(optionsRF);
	        lwl.setOptions(optionsLwl);
	        //lwl.setClassifier(cls);//randomForest
	        lwl.setClassifier(randomForest);//randomFOrest
	       
	       lwl.buildClassifier(trainingDataSet);
	        
	        
	        
	        
	        //-B "weka.classifiers.lazy.LWL -U 0 -K -1 -A \"weka.core.neighboursearch.LinearNNSearch -A \\\"weka.core.EuclideanDistance -R first-last\\\"\" -W weka.classifiers.trees.RandomForest -- -P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1"
	       
	       //cfsArray[0]= lmt;
	       //cfsArray[1]= sl;
	       cfsArray[0]=sl;
	       cfsArray[1]=lwl;
	       cfsArray[2]=lmt;
	      // cfsArray[0]=randomForest;
	      // cfsArray[1]=rt;
	       
	       //"weka.classifiers.functions.SimpleLogistic -I 0 -M 500 -H 50 -W 0.0"
	       
	       vote.setClassifiers(cfsArray); 
	       //vote.buildClassifier(trainingDataSet);
	    //   vote.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE, Vote.TAGS_RULES));
	       
	       //RandomTree rt = new RandomTree();
	       //vote.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE, Vote.));
	       
	       //-W weka.classifiers.trees.RandomForest -- -P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 
	        
	        System.out.println(Utils.joinOptions(vote.getOptions()));
	       //
	        //SimpleLogistic sl = new SimpleLogistic();
	        
	       // sl.setOptions();22
	        ///
	       // Instances dt = src.getDataSet();
	       // dt.setClassIndex(dt.numAttributes()- 1);

	        //String[] options = new String[4];
	        //options[0] = "-C";
	        //options[1] = "0.1";
	        //options[2] = "-M";
	        //options[3] = "2";
	        //J48 mytree = new J48();
	        //mytree.setOptions(options);
	        //mytree.buildClassifier(dt);
	        
	       // Evaluation eval = new Evaluation(dt);
	        ///
	     // set your configurations parameters here, e.g., "-S 1"
	        //String options = "whatever configuration you want";

	        // instantiate the classifier object
	       // REPTree tree = new REPTree();

	        // parse and set the classifier's configuration
	        //vote.setOptions(Utils.splitOptions(options));
	        
	        //vote.setOptions(options);
	        
	        // vs.buildClassifier(trainingDataSet);
	        vote.buildClassifier(trainingDataSet);
	        
	        //////////////////////////////
	        ///////////////////////////////
	        /////////////////////////////
	        /**
			 * train the alogorithm with the training data and evaluate the
			 * algorithm with testing data
			 */
			 
			
			
	        
	        /////////////////////////////////
	        
	        
	        
	     
	   
           
	       //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	  /*      int instances = trainingDataSet.numInstances();
			if( instances > 10)
				instances = 10;
			
	        	   
	        	      eval.crossValidateModel(vote, trainingDataSet, instances, new Random(1));//training !!!!!!!!!!!!!!!!!!!!!!!!
	        	     		Double error_c = eval.errorRate();
		
		
	      System.out.println(error_c);
		
	
		System.out.println("** Random Tress Evaluation with Datasets **");
		System.out.println(eval.toSummaryString());
		System.out.print(" the expression for the input data as per alogorithm is ");
		System.out.println(eval.toMatrixString());
		 System.out.println(eval.toClassDetailsString());
		*/ 
		 /////////////////////////////////////////////
		 //////////////////////////////////////////
		 
		 
	        Instances train = getDataSet2(TRAINING_DATA_SET_FILENAME,"STEM");
	        Instances test = getDataSet(TESTING_DATA_SET_FILENAME,"STEM");
	        
	        System.out.println("NumTrainAttributes: "+train.numAttributes());
	        System.out.println("NumTestAttributes: "+test.numAttributes());
	        
	        
	        train.setClassIndex(train.numAttributes() - 1);
	        test.setClassIndex(test.numAttributes() - 1);
	        //set up filter of the data
	        //AttributeSelection filter = new AttributeSelection();
	        //filter.getCapabilities().enableAll();
	        //BestFirst search = new BestFirst();
	        //search.setOptions(weka.core.Utils.splitOptions("-D 2 -N 10"));
	        //filter.setEvaluator(new CfsSubsetEval());
	        //filter.setSearch(search);//from   w  w  w . j av  a2  s .  c  o  m
	       
	        //filter.setInputFormat(train);
	        //Instances newtrain = Filter.useFilter(train, filter);
	        //Instances newtest = Filter.useFilter(test, filter);
	        //System.out.println("NumNewTrainAttributes: "+newtrain.numAttributes());
	        //System.out.println("NumNewTestAttributes: "+newtest.numAttributes());
	       
	        //newtrain.setClassIndex(newtrain.numAttributes() - 1);
	        //newtest.setClassIndex(newtest.numAttributes() - 1);

	        Vote vs = new Vote();
	        vs.setClassifiers(cfsArray); 
	        System.out.println(Utils.joinOptions(vs.getOptions()));

	        vs.buildClassifier(train);
	      
	        String[] optionsR = new String[2];	// options for remove
			optionsR[0] = "-R"; 					// "range"
			optionsR[1] = "7"; 					// first attribute
			Remove remove = new Remove(); 		// new instance of filter
			remove.setOptions(optionsR); 		// set options
			remove.setInputFormat(train);
			Instances newTrain = Filter.useFilter(train, remove); 
	        
			 String[] optionsR2 = new String[2];	// options for remove
				optionsR2[0] = "-R"; 					// "range"
				optionsR2[1] = "7"; 					// first attribute
				Remove remove2 = new Remove(); 		// new instance of filter
				remove2.setOptions(optionsR2); 		// set options
				remove2.setInputFormat(test);
				Instances newData = Filter.useFilter(test, remove2);  
	        
				 Standardize filter = new Standardize();
				 filter.setInputFormat(train);  // initializing the filter once with training set
				 Instances nTrain = Filter.useFilter(train, filter);  // configures the Filter based on train instances and returns filtered instances
				 Instances nTest = Filter.useFilter(test, filter);    // create new test set
        
				nTrain.setClassIndex(nTrain.numAttributes() - 1);
			    nTest.setClassIndex(nTest.numAttributes() - 1);
	       
			    vs.buildClassifier(nTrain);
			    
	        PrintWriter pw = new PrintWriter(new FileWriter(
	                "spambase-L5"));
              //test.setClass(att);
	        
	       
			
					
	        
	        for (int i = 0; i < nTest.numInstances(); i++) {
	        	//System.out.println("Attribute in test: "+  nTest.instance(i).attribute(0));
	           // double pred = vs.classifyInstance(nTest.instance(i));
	        	 double pred = vs.classifyInstance(nTest.instance(i));
	        	// double pred = vs.classifyInstance(nTest.instance(i));
	        	 
	            nTest.instance(i).setClassValue(pred);
	           // System.out.println(newtest.instance(i).stringValue(6));
	           // pw.println(pred);
	            //pw.println(nTest.instance(i)+ nTest.instance(i).stringValue(7));
	         //   pw.println(nTest.instance(i).stringValue(7)+"--"+nTest.instance(i).attribute(1).toString());//+"--"+nTest.instance(i).stringValue(1));
	            pw.println(nTest.instance(i)+"|" + nTest.instance(i).stringValue(7));
	            
	           
	           // LOG.info("ntestInstance1"+ nTest.instance(i).stringValue(1));
	           // LOG.info("ntestInstance2"+ nTest.instance(i).stringValue(2));
	           // LOG.info("ntestInstance3"+ nTest.instance(i).stringValue(3));
	           // LOG.info("ntestInstance4"+ nTest.instance(i).stringValue(4));
	           // LOG.info("ntestInstance5"+ nTest.instance(i).stringValue(5));
	           // LOG.info("ntestInstance6"+ nTest.instance(i).stringValue(6));
	            
	            ///M0.saveInDatabase(nTest.instance(i).stringValue(7),"STEM",Integer.toString(i));
	          //  LOG.info("ntestInstance0"+ nTest.instance(i).stringValue(0));
	           
	            
	          //  Student students = new Student();
	           // students.setVote(Integer.parseInt(nTest.instance(i).stringValue(7)));
	           // Student.setStudent(students);
	         /*
	            List<Student> l = Student.getStudent();
	           int studentAmmount = l.size();
	           Student st = new Student();
	           
	           
	           for(int ii = 0; i < l.size(); ii++)
	           {
	        	   st = Student.getStudent().get(ii);
	              // if (n.getKey().equals(key))
	               //{
	        	   
	        	   l.set(ii, st).setVote(Integer.valueOf(nTest.instance(i).stringValue(7)));
	                   l.get(ii).VOTE.intValue();
	               //}
	           }
	           */
	            
	        }
	        pw.close();
	        
	        
	  
	        
	        
	        
	        ////
	      //  for (int i = 0; i < nTest.numInstances(); i++) {
	        	
	        //    double pred = vs.classifyInstance(nTest.instance(i));
	            
	            //nTest.instance(i).setClassValue(pred);
	            
	           // System.out.println(newtest.instance(i).stringValue(6));
	           // pw.println(pred);
	            //pw.println(nTest.instance(i)+ nTest.instance(i).stringValue(7));
	         //   pw.println(nTest.instance(i).stringValue(7)+"--"+nTest.instance(i).attribute(1).toString());//+"--"+nTest.instance(i).stringValue(1));
	           // pw.println(nTest.instance(i).stringValue(7));
	      
	           // System.out.println("***??"+ nTest.instance(i).stringValue(i));
	            //System.out.println("***??"+ nTest.instance(i).stringValue(6));
	            //LOG.info("***??"+ nTest.instance(i).stringValue(i));
	         
	            //   LOG.error("***??"+ pred);
	           
	            //int index= nTrain.attribute("PHONE").index();
	            //LOG.error("%%%"+index);
	           // LOG.error("***??"+ M0.getDataSet2(null, "STEM").attribute("PHONE").enumerateValues().nextElement().toString());
	           // LOG.error("***??"+nTest.instance(i).stringValue(1));
	            
	         //   M0.getDataSet2(null, "STEM").attribute("Phone").addStringValue(Double.toString(pred));
	           
	            //M0.getDataSet2(null, "STEM").attribute("PHONE").enumerateValues().nextElement().toString();
	           
	            
	            // M0.getDataSet2(null, "STEM").attribute("PHONE").enumerateValues().nextElement()
	            // Attribute att = new Attribute();
	           // LOG.error("***??_"+ nTest.instance(i).classIndex().attribute(0).toString());
	           // eval.
	            LOG.error("!!!!!!");
	             // M0.saveInDatabase((int)Math.round(pred), "STEM");
	            
	            LOG.info("saveInDatabase1\n");
	    		
	    		int intValue = 0;
	   		 final String QUERY_STRING = "SELECT PHONE,TOPIC, VOTE from Student where OU is null";
	   		 Instances data = retrieveModelInstances(QUERY_STRING);	
	   		LOG.info("saveInDatabase1\n");
	   		Query query;
	   		
	   		
	   		LOG.info("saveInDatabase2\n");
	   		List<?> students = Student.getStudentsforPrediction("STEM");
	   		LOG.info("---"+students.size());
	   		LOG.info("saveInDatabase3\n");
	   		int i=0;
	   		for (Object o: students) {
	   			String val = "";
	   		if (i<students.size())	
	   		{
	   			double pred = vs.classifyInstance(nTest.instance(i));
	   			nTest.instance(i).setClassValue(pred);
		          val = nTest.instance(i).stringValue(7);
		            
	   		}
	   		else
	   		{break;}
	   		
	   		
	   		
	   		
	   		
	   	    intValue = Integer.valueOf(val);//(int)Math.round(pred);
	   	     i++;
		       	
	   			LOG.info("saveInDatabase4\n");
	   			Student student = (Student)o;
	   			//Student student = students.get(index)
	   			LOG.info("saveInDatabase5\n");
	   			student.setVote(Integer.valueOf(intValue));
	   			//student.setPhone(data.attribute(0).value(Integer.valueOf(instanceNumber))); 
	   			//data.classAttribute().equals("PHONE");
	   			ModelManager.transaction.begin();	
	   			student.setTopic("STEM");
	   			LOG.info("saveInDatabase6\n");
	   			LOG.info("---"+intValue);
	   			LOG.info("---"+student.PHONE);//get.getPhone());
	   			 Integer ii= Integer.valueOf(intValue);
	   			String sql = "UPDATE STUDENT SET VOTE="+ii+" WHERE PHONE='"+ student.getPhone() +"' AND TOPIC='"+student.getTopic()+"'";
	   			//String sql = "UPDATE STUDENT SET VOTE='"+student.getVote()+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";	
	   			LOG.info("saveInDatabase7\n");
	   			query = ModelManager.entityManager.createNativeQuery(sql);
	   			LOG.info("saveInDatabase8\n");
	   			query.executeUpdate();
	   			LOG.info("saveInDatabase10\n");
	   			
	   			LOG.info("Changes have been commited to the database successfully\n");
	   			LOG.info("saveInDatabase9\n");
	   			//break;
	   			ModelManager.transaction.commit();
		   		LOG.info("Changes have been commited to the database successfully\n");
	
	   			
	   		}
	   		
	            
	            
	            
	            
	            
	              LOG.info("!!!!!!");
	        
	        ////
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	       
	        eval = new Evaluation(nTrain);
	        eval.evaluateModel(vs, nTest);
	        eval.predictions();
	        System.out.println("** Random Tress Evaluation with Datasets **");
			System.out.println(eval.toSummaryString());
			System.out.print(" the expression for the input data as per alogorithm is ");
			System.out.println(eval.toMatrixString());
			 System.out.println(eval.toClassDetailsString());
	    
	       // Evaluation eval = new Evaluation(train);
	       // eval.evaluateModel(vs, test);
	       // Double error_c = eval.errorRate();
	       // System.out.println(error_c);

	  //  }
	   
		 
		 
	//	Vote vote2  = new Vote();
	 //       trainingDataSet.setClassIndex(trainingDataSet.numAttributes()-1);
		//
		 ///////////////////////////////////////////////////////
		 //testingDataSet.setClassIndex(testingDataSet.numAttributes() - 1);
		// testingDataSetUnlabeled.setClassIndex(testingDataSetUnlabeled.numAttributes() - 1);
		
		//// vote2.setOptions(weka.core.Utils/* w ww.  j  a  v  a2  s. c  om*/
	        //        .splitOptions("-B \"weka.classifiers.functions.SMO -C 1 -L 0.01 -P 1E-10\""));

		  // vote2.buildClassifier(trainingDataSet);//training
		/// if (!trainingDataSet.equalHeaders(testingDataSet)) throw new IllegalStateException("Incomplete test, train");
        // PrintWriter pw = new PrintWriter(new FileWriter(
        //      "spambase-L5.txt"));
        // System.out.println("testingDataSet.numInstances()= "+testingDataSet.numInstances());
        // for (int i = 0; i < testingDataSet.numInstances(); i++) {
        //  double pred = vote2.classifyInstance(testingDataSet.instance(i));
        //  pw.println(pred);
     // }
     // pw.close();
     // eval = new Evaluation(trainingDataSetUnlabeled);//train !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    
	        
	        // eval.evaluateModel(vote, trainingDataSet);
     // Double error_c = eval.errorRate();
     // System.out.println(error_c);
	/////////////
	
		 //////////////////////////////////////////////////////////
		//  eval.evaluateModel(vote2, testingDataSetUnlabeled);
	//	  /** Print the algorithm summary */
//			System.out.println("** Random Tress Evaluation with Datasets **");
//			System.out.println(eval.toSummaryString());
//			System.out.print(" the expression for the input data as per alogorithm is ");
			//System.out.println(forest);
//			System.out.println(eval.toMatrixString());
//			 System.out.println(eval.toClassDetailsString());
		
		 ///////////////////////
		 
		 
		 
		 
		 
		 
		 return eval;
		
		
		
		
		
	}
	
	
	private static void saveInDatabase(int intValue, String topic) {
		// TODO Auto-generated method stub
		LOG.info("saveInDatabase1\n");
		
		
		 final String QUERY_STRING = "SELECT PHONE,TOPIC, VOTE from Student where OU=null";
		 Instances data = retrieveModelInstances(QUERY_STRING);	
		LOG.info("saveInDatabase1\n");
		Query query;
		ModelManager.transaction.begin();
		LOG.info("saveInDatabase2\n");
		List<?> students = Student.getStudents(topic);
		LOG.info("---"+students.size());
		LOG.info("saveInDatabase3\n");
		for (Object o: students) {
			
			LOG.info("saveInDatabase4\n");
			Student student = (Student)o;
			//Student student = students.get(index)
			LOG.info("saveInDatabase5\n");
			student.setVote(Integer.valueOf(intValue));
			//student.setPhone(data.attribute(0).value(Integer.valueOf(instanceNumber))); 
			//data.classAttribute().equals("PHONE");
			
			student.setTopic(topic);
			LOG.info("saveInDatabase6\n");
			LOG.info("---"+intValue);
			LOG.info("---"+student.getPhone());
			 Integer ii= Integer.valueOf(intValue);
			String sql = "UPDATE STUDENT SET VOTE="+ii+" WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";
			//String sql = "UPDATE STUDENT SET VOTE='"+student.getVote()+"' WHERE PHONE='"+student.getPhone()+"' AND TOPIC='"+student.getTopic()+"'";	
			LOG.info("saveInDatabase7\n");
			query = ModelManager.entityManager.createNativeQuery(sql);
			LOG.info("saveInDatabase8\n");
			query.executeUpdate();
			LOG.info("saveInDatabase10\n");
			
			LOG.info("Changes have been commited to the database successfully\n");
			LOG.info("saveInDatabase9\n");
			//break;
			
			
		}
		ModelManager.transaction.commit();
		LOG.info("Changes have been commited to the database successfully\n");

		
		
		
		
	}
	
}