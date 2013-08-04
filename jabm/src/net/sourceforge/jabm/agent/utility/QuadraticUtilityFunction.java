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
package net.sourceforge.jabm.agent.utility;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class QuadraticUtilityFunction extends AbstractUtilityFunction implements
		Serializable {

	protected double alpha;
	
	protected double beta;
	
	static Logger logger = Logger.getLogger(QuadraticUtilityFunction.class);
	
	public QuadraticUtilityFunction(double alpha,
			double beta) {
		this.alpha = alpha;
		this.beta = beta;
	}

	public QuadraticUtilityFunction() {
		super();
	}

	@Override
	public double calculatePayoff(double profit) {
		double result = alpha * profit  +  beta * profit * profit;
		return result;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	
}
