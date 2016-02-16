package org.einclusion.frontend;

import java.util.ArrayList;
/**
 *	Represents a regression model for WEKA. 
 *	@author student
 */
public class RegressionModel {
	public String key;
	public String value;
	public ArrayList<Coefficient> coefficients = new ArrayList<>();
	
	/**
	 *	Creates a model with set values 
	 *	@param key - name of the model
	 *	@param value - regression model in form {"coefficients":{"":0.3,"IWS":0.2,"SWL":0.1}}, for example.
	 */
	public RegressionModel(String key, String value) {
		this.key = key;
		this.value = value;
		getCoefficients();
	}
	
	/**
	 * 	Creates regression model with given key, coefficients from database
	 * 	@param key - name of the model
	 * 	@param coefficients - string of coefficients in a topic
	 * 	@param isTrue - to know difference between functions
	 */
	public RegressionModel(String key, String coefficients, boolean isTrue) {
		String equation = "{\"coefficients\":{";
		coefficients = coefficients.replaceAll("[ \\{\\}]", "");
		System.out.println("coefficients: "+coefficients);
		String []coefs = coefficients.split(",");
		int counter = 0;
		for (String s: coefs) {
			String []tmp = s.split("=");
			if (counter>0)
				equation += ",";
			if (tmp[0].equals(null))
				tmp[0] = "";
			equation += "\"" + tmp[0] + "\":" + tmp[1];
			counter++;
		}
		equation += "}}";
		
//		System.out.println("equation: "+equation);
		
		this.key = key;
		this.value = equation;
		getCoefficients();
	}
	/**
	 *	Splits member 'value' into Coefficients, keeps them in ArrayList 'coefficients'.
	 */
	public void getCoefficients() {
		coefficients.clear();
		String sr[] = this.value.replaceAll("[{}]", "").split(",");
		
		String coef = sr[0].split(":")[2];
		coefficients.add(new Coefficient("const", coef));
		
		for (int i=1; i<sr.length; i++) {
			String tmp[] = sr[i].split(":");
			String key = tmp[0].replaceAll("\"", "");
			String value = tmp[1];
			coefficients.add(new Coefficient(key, value));
		}
	}
	/**
	 *	Returns false if coefficient with name 'coeffName' is not found in the ArrayList coefficients, 
	 *	true - if it is found.
	 *	@param coeffName
	 *	@return boolean
	 */
	public boolean hasCoefficientValue(String coeffName) {
		String value = "";
		for (Coefficient c: coefficients) {
			if (c.name.equals(coeffName))
				value = c.value;
		}
		return !value.equals("");
	}
	/**
	 *	Calculates risk degree by given values using specified regression models equation.
	 *	@param rm - Regression Model
	 *	@param SWL - value of Students Willingness to Learn
	 *	@param SAL - value of Students ability to Learn
	 *	@param ELM - value of E-learning materials
	 *	@param IWS - value of Instructors Willingness to Share knowledge
	 *	@param ELE - value of E-learning Environment
	 *	@return (double) M2 risk degree value
	 */
	public static double getM2regressionDegree(RegressionModel rm, double SWL, double SAL, double ELM, double IWS, double ELE) {
		double result = 0;
		for (Coefficient c: rm.coefficients) {
			switch (c.name) {
				case "SWL":
					result += Double.parseDouble(c.value)*SWL;
					break;
				case "SAL":
					result += Double.parseDouble(c.value)*SAL;
					break;
				case "ELM":
					result += Double.parseDouble(c.value)*ELM;
					break;
				case "IWS":
					result += Double.parseDouble(c.value)*IWS;
					break;
				case "ELE":
					result += Double.parseDouble(c.value)*ELE;
					break;
				default: // constant
					result += Double.parseDouble(c.value);
					break;
			}
		}
		return result;
	}
	
	/**
	 * 	Calculates risk degree by given values using specified regression models equation.
	 * 	@param rm - RegressionModel
	 * 	@param KFA - Knowledge flow acceleration (double)
	 * 	@return M3 regression degree (Float)
	 */
	public static double getM3regressionDegree(RegressionModel rm, double KFA){
		double result = 0;
		for (Coefficient c: rm.coefficients) {
			switch (c.name) {
				case "KFA":
					result += Double.parseDouble(c.value)*KFA;
					break;
				default: // constant
					result += Double.parseDouble(c.value);
					break;
			}
		}
		return result;
	}
	
}
