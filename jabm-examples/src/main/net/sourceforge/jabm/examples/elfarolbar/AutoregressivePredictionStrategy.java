/*
 * JABM - Java Agent-Based Modeling Toolkit
 * Copyright (C) 2013 Steve Phelps
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package net.sourceforge.jabm.examples.elfarolbar;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.AbstractContinousDistribution;

public class AutoregressivePredictionStrategy extends
		AbstractPredictionStrategy implements InitializingBean {
	
	protected double[] coefficients;
	
	protected double offset;
	
	protected int windowSize;
	
	protected AbstractContinousDistribution coefficientDistribution;
	
	protected AbstractContinousDistribution offsetDistribution;
	
	public AutoregressivePredictionStrategy(int memorySize, 
								AbstractContinousDistribution coefficientDistribution) {
		super();
		this.coefficientDistribution = coefficientDistribution;
		afterPropertiesSet();
	}
	
	public AutoregressivePredictionStrategy() {
		super();
	}

	public void randomlyInitialise() {
		for(int i=0; i<windowSize; i++) {
			coefficients[i] = coefficientDistribution.nextDouble();
		}
		offset = offsetDistribution.nextDouble();
	}

	@Override
	public void makePrediction() {
		currentPrediction = 0.0;
		for(int i=0; i<windowSize; i++) {
			currentPrediction += barTender.getAttendanceAtLag(i+1) 
									* coefficients[i];
		}
		currentPrediction += offset * 100;
	}

	public AbstractContinousDistribution getCoefficientDistribution() {
		return coefficientDistribution;
	}

	/**
	 * The probability distribution used to draw coefficient values.
	 * @param coefficientDistribution
	 */
	@Required
	public void setCoefficientDistribution(
			AbstractContinousDistribution coefficientDistribution) {
		this.coefficientDistribution = coefficientDistribution;
	}
	
	/**
	 * The probability distribution used to draw the offset value.
	 * @return
	 */
	@Required
	public AbstractContinousDistribution getOffsetDistribution() {
		return offsetDistribution;
	}

	public void setOffsetDistribution(
			AbstractContinousDistribution offsetDistribution) {
		this.offsetDistribution = offsetDistribution;
	}

	@Override
	public void afterPropertiesSet() {
		coefficients = new double[windowSize];
		randomlyInitialise();
	}

	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	@Override
	public String toString() {
		return "AutoregressivePredictionStrategy [offset=" + offset
				+ ", windowSize=" + windowSize + ", coefficientDistribution="
				+ coefficientDistribution + ", offsetDistribution="
				+ offsetDistribution + "]";
	}
	
	
}
