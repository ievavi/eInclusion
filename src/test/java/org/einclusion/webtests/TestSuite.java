package org.einclusion.webtests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
public class TestSuite {

	@Test
	public void allTests() {

		Result result = null;
		result = JUnitCore.runClasses(JUnit.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("Tests successful: " + result.wasSuccessful());

	}

}
