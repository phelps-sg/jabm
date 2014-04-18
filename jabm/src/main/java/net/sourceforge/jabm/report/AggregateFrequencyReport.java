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
package net.sourceforge.jabm.report;

import java.io.Serializable;
import java.util.Map;

import net.sourceforge.jabm.event.SimEvent;

import org.apache.commons.math3.stat.Frequency;


public class AggregateFrequencyReport implements FrequencyReport, Serializable {

	protected FrequencyReport singleCaseReport;
	
	public AggregateFrequencyReport(FrequencyReport singleCaseReport) {
		super();
		this.singleCaseReport = singleCaseReport;
	}

	public Frequency getFrequency() {
		// TODO Auto-generated method stub
		return null;
	}

	public void eventOccurred(SimEvent event) {
		// TODO Auto-generated method stub

	}

	public Map<Object, Number> getVariableBindings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

}
