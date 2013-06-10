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
 * A strategy which predicts the next-period attendance using the historical
 * attendance at a pre-specified lag.
 * 
 * @author Steve Phelps
 * 
 */
public class LaggedPredictionStrategy extends AbstractPredictionStrategy {

	protected int lag;
	
	public LaggedPredictionStrategy(int lag) {
		super();
		this.lag = lag;
	}
	
	public LaggedPredictionStrategy() {
		super();
	}

	@Override
	public void makePrediction() {	
		currentPrediction = barTender.getAttendanceAtLag(lag);
	}

	public int getLag() {
		return lag;
	}

	/**
	 * The time-series lag to use for predicted the next week's attendance.
	 *  
	 * @param lag
	 */
	public void setLag(int lag) {
		this.lag = lag;
	}

	@Override
	public String toString() {
		return "LaggedPredictionStrategy [lag=" + lag + " currentPrediction="
				+ currentPrediction + "]";
	}

	
}
