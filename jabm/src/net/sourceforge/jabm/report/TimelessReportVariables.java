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

import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.jabm.event.SimEvent;

public class TimelessReportVariables implements ReportVariables {

	protected ReportVariables reportVariables;
	
	@Override
	public Map<Object, Number> getVariableBindings() {
		Map<Object, Number> bindings = reportVariables.getVariableBindings();
		Map<Object, Number> result = new LinkedHashMap<Object, Number>(bindings);
		result.remove(reportVariables.getName() + ".t");
		return result;
	}

	@Override
	public void eventOccurred(SimEvent event) {
	}

	@Override
	public void compute(SimEvent event) {
	}

	@Override
	public void dispose(SimEvent event) {
	}

	@Override
	public void initialise(SimEvent event) {
	}

	@Override
	public String getName() {
		return reportVariables.getName();
	}

	public ReportVariables getReportVariables() {
		return reportVariables;
	}

	public void setReportVariables(ReportVariables reportVariables) {
		this.reportVariables = reportVariables;
	}

}
