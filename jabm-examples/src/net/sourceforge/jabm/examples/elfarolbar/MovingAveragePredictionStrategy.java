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
 * A prediction strategy that uses a simple moving average.
 * 
 * @author Steve Phelps
 *
 */
public class MovingAveragePredictionStrategy extends AbstractPredictionStrategy {

	/**
	 * The window size.
	 */
	protected int windowSize;
	
	@Override
	public void makePrediction() {
		currentPrediction += 
				(double) barTender.getAttendanceAtLag(1) / (double) windowSize;
		currentPrediction -= 
				(double) barTender.getAttendanceAtLag(windowSize + 1) 
					/ (double) windowSize;
		if (logger.isDebugEnabled()) {
			logger.debug("currentPrediction = " + currentPrediction);
			logger.debug("windowSize = " + windowSize);
		}
	}

	public int getWindowSize() {
		return windowSize;
	}

	/**
	 * Configure the window size.
	 * 
	 * @param windowSize  The size of the window used to calculate the MA.
	 */
	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	@Override
	public String toString() {
		return "MovingAveragePredictionStrategy [windowSize=" + windowSize
				+ " currentPrediction=" + currentPrediction + "]";
	}

	
}
