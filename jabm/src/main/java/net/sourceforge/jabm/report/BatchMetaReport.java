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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.jabm.event.BatchFinishedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationFinishedEvent;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;


public class BatchMetaReport implements Report, Serializable {

	protected List<Report> reports;
	
	protected Map<Object,SummaryStatistics> variables = 
		new LinkedHashMap<Object,SummaryStatistics>();
	
	static Logger logger = Logger.getLogger(BatchMetaReport.class);
	
	public BatchMetaReport(ArrayList<Report> reports) {
		super();
		this.reports = reports;
		logger.debug("reports = " + reports);
	}
	
	public BatchMetaReport() {
	}

	public void eventOccurred(SimEvent event) {
		for (Report report : reports) {
			report.eventOccurred(event);
		}
		if (event instanceof SimulationFinishedEvent) {
			onSimulationFinished();
		} 
		if (event instanceof BatchFinishedEvent) {			
			onBatchFinished();
		}
	}

	private void onBatchFinished() {
		logger.debug("variables = " + variables);
		Iterator<Object> i = variables.keySet().iterator();
		while (i.hasNext()) {
			Object variable = i.next();
			SummaryStatistics stats = variables.get(variable);
			logger.info(variable + ": " + stats);
		}
	}

	private void onSimulationFinished() {
		logger.debug("reports = " + reports);
		for (Report report : reports) {
			Map<Object,Number> singleSimulationVars = report.getVariableBindings();
			Iterator<Object> i = singleSimulationVars.keySet().iterator();
			while (i.hasNext()) {
				Object variable = i.next();
				Object value = singleSimulationVars.get(variable);
				if (value instanceof Number) {
					double dValue = singleSimulationVars.get(variable).doubleValue();
					SummaryStatistics stats = variables.get(variable);
					if (stats == null) {
						stats = new SummaryStatistics();
						variables.put(variable, stats);
					}
					stats.addValue(dValue);
				}
			}
		}
	}
	

	public List<Report> getReports() {
		return reports;
	}


	public void setReports(List<Report> reports) {
		this.reports = reports;
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
