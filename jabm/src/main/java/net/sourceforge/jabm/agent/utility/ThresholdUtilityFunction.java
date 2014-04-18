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

public class ThresholdUtilityFunction extends AbstractUtilityFunction implements
		Serializable {

	protected double threshold;
	
	static Logger logger = Logger.getLogger(ThresholdUtilityFunction.class);

	public ThresholdUtilityFunction() {
		super();
	}

	@Override
	public double calculatePayoff(double profit) {
		if (profit > threshold) {
			return profit;
		} else {
			return 0;
		}
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	
}
