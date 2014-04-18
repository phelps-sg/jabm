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


/**
 * A prediction strategy which uses an exponential moving average.
 * 
 * @author Steve Phelps
 *
 */
public class ExponentialMovingAveragePredictionStrategy 
		extends AbstractPredictionStrategy {

	protected double alpha = 0.1;
	
	@Override
	public void makePrediction() {
		currentPrediction = alpha * barTender.getAttendanceAtLag(1) +
			(1 - alpha) * currentPrediction;
	}

	public double getAlpha() {
		return alpha;
	}

	/**
	 * Configure half-life parameter.
	 * 
	 * @param alpha  Higher values of this parameter will put more weight
	 *                   on recent values as opposed to old values.
	 */
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	@Override
	public String toString() {
		return "ExponentialMovingAveragePredictionStrategy [alpha=" + alpha
				+ " currentPrediction=" + currentPrediction + "]";
	}
	
}
