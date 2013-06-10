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
 * This strategy forecasts the opposite of the most recent attendance.
 * If the bar was overcrowded last week, then we forecast that it will be
 * under-crowded next week and vice versa.
 * 
 * @author sphelps
 *
 */
public class ContrarianPredictionStrategy extends LowHighPredictionStrategy {

	@Override
	public void makePrediction() {
		if (wasOvercrowdedLastWeek()) {
			predictLow();
		} else {
			predictHigh();
		}
	}
	
	
	
}
