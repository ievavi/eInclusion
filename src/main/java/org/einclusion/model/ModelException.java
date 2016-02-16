package org.einclusion.model;
/**
 *	A unique exception for InstanceManager
 *	@author student
 */
public class ModelException extends Exception {
	private static final long serialVersionUID = -7937514651641173022L;

	ModelException() {
		super();
	}

	ModelException(String message) {
		super(message);
	}
}
