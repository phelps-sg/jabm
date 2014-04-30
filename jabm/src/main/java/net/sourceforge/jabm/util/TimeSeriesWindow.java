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
package net.sourceforge.jabm.util;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class TimeSeriesWindow {

	protected double[] values;
	
	protected int windowSize;
	
	protected int currentIndex = 0;
	
	public TimeSeriesWindow(int windowSize) {
		this.windowSize = windowSize;
		values = new double[windowSize];
		for(int i=0; i<windowSize; i++) {
			values[i] = Double.NaN;
		}
	}
	
	public int getWindowSize() {
		return windowSize;
	}
	
	public void addValue(double value) {
		values[currentIndex++ % windowSize] = value;
	}
	
	public double getValue(int lag) {
		int j = currentIndex-1 - lag;
		int index;
		if (j < 0) {
			j = -j;
			int n = windowSize;
			int offset = j % n;
			index = windowSize-1 - offset;
		} else {
			index = j % windowSize;
		}
		return values[index];
	}
	
	public SummaryStatistics getSummaryStatistics() {
		SummaryStatistics result = new SummaryStatistics();
		for(int i=0; i<windowSize; i++) {
			result.addValue(getValue(i));
		}
		return result;
	}
	
	public double getSimpleMovingAverage() {
		return getSummaryStatistics().getMean();
	}
		
}
