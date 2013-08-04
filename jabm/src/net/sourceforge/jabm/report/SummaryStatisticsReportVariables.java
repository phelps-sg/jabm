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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.jabm.event.SimEvent;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class SummaryStatisticsReportVariables extends AbstractReportVariables {

	protected Map<Object, SummaryStatistics> summaryVariableBindings
		= new LinkedHashMap<Object, SummaryStatistics>();
	
	protected ReportVariables reportVariables;

	public SummaryStatisticsReportVariables(AbstractReportVariables reportVariables) {
		this(reportVariables.getName(), reportVariables);
	}

	public SummaryStatisticsReportVariables(String name,
			ReportVariables reportVariables) {
		super(name + ".summary");
		this.reportVariables = reportVariables;
	}
	
	public SummaryStatisticsReportVariables(ReportVariables reportVariables) {
		super("summary");
		this.reportVariables = reportVariables;
	}
	
	@Override
	public void compute(SimEvent event) {
		super.compute(event);
		reportVariables.compute(event);
		Map<Object, Number> bindings = reportVariables.getVariableBindings();
		for(Object variable : bindings.keySet()) {
			SummaryStatistics stats = summaryVariableBindings.get(variable);
			if (stats == null) {
				stats = new SummaryStatistics();
				summaryVariableBindings.put(variable, stats);
			}
			stats.addValue(bindings.get(variable).doubleValue());
		}
	}

	@Override
	public Map<Object, Number> getVariableBindings() {
		Map<Object, Number> result = new HashMap<Object, Number>();
		for(Object variable : summaryVariableBindings.keySet()) {
			SummaryStatistics stats = summaryVariableBindings.get(variable);
			recordSummaryStatistics(variable, result, stats);
		}
		return result;
	}

	@Override
	public void initialise(SimEvent event) {
		summaryVariableBindings.clear();
		reportVariables.initialise(event);
	}

	public ReportVariables getReportVariables() {
		return reportVariables;
	}

	public void setReportVariables(ReportVariables reportVariables) {
		this.reportVariables = reportVariables;
	}

}
