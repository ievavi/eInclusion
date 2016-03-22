package org.einclusion.webtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.webengine.M1Web;
import org.webengine.M2Web;
import org.webengine.M3Web;
import org.webengine.PredictionWeb;
import org.webengine.WebTable;

public class JUnit {

	@Test
	public void TestReadDBfilteredM1() {
		M1Web test = new M1Web();
		test.readDBfiltered("*", "All");
		assertFalse("readDBfiltered method list is empty", M1Web.list.isEmpty());
	}

	@Test
	public void TestReturnLabelsM1() {
		M1Web test = new M1Web();
		assertFalse("Adding labels does not work", test.returnLabels().isEmpty());
	}

	@Test
	public void TestReadDBfilteredM2() {
		M2Web test = new M2Web();
		test.readDBfiltered("*", "All");
		assertFalse("readDBfiltered method list is empty", M2Web.list.isEmpty());
	}

	@Test
	public void TestGetRegressionModel() {
		M2Web test = new M2Web();
		String testString = test.getRegressionModel("test");
		assertFalse("Regression model is empty", testString.isEmpty());
	}

	@Test
	public void TestReturnLabelsM2() {
		M2Web test = new M2Web();
		assertFalse("Adding labels does not work", test.returnLabels().isEmpty());
	}

	@Test
	public void TestReadDBfilteredM3() {
		M3Web test = new M3Web();
		test.readDBfiltered("*", "All");
		assertFalse("readDBfiltered method list is empty", M3Web.list.isEmpty());
	}

	@Test
	public void TestReturnLabelsM3() {
		M3Web test = new M3Web();
		assertFalse("Adding labels does not work", test.returnLabels().isEmpty());
	}

	@Test
	public void TestReadDBfilteredPrediction() {
		PredictionWeb test = new PredictionWeb();
		test.readDBfiltered("*", "All");
		assertFalse("readDBfiltered method list is empty", PredictionWeb.list.isEmpty());
	}

	@Test
	public void TestReturnLabelsPrediction() {
		PredictionWeb test = new PredictionWeb();
		assertFalse("Adding labels does not work", test.returnLabels().isEmpty());
	}

	@Test
	public void TestRound() {
		double test = WebTable.round(2.567975, 2);
		assertEquals("Round method works incorrectly", 2.57, test, 0.000001);
	}

}
