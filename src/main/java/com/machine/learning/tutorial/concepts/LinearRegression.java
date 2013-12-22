package com.machine.learning.tutorial.concepts;

import java.util.List;

/**
 * LinearRegression class is used to build a model based on the set of data having univariate or multivariate data.
 *  
 * @author Vivek
 *
 */
public class LinearRegression {

	/**
	 * learning rate
	 */
	private final double DEFAULT_LEARNING_RATE = 0.1;
	
	/**
	 * input (x's) data from the training set
	 */
	private List<Double[]> trainingData;
	
	/**
	 * input (y's) data from the training set
	 */
	private List<Double> yData;
	
	/**
	 * true indicates the regression has an intercept false otherwise
	 */
	private boolean hasIntercept;
	
	/**
	 * default constructor
	 */
	public LinearRegression (List<Double[]> trainingData, List<Double> yData, boolean hasIntercept) {
		this (hasIntercept);
		this.trainingData = trainingData;
		this.yData 	 	  = yData;
	}
	
	public LinearRegression (boolean hasIntercept) {
		this.hasIntercept = hasIntercept;
	}
	
	
}