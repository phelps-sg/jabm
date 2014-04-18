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

import java.util.Map;

import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationFinishedEvent;
import net.sourceforge.jabm.event.SimulationStartingEvent;

import org.springframework.beans.factory.annotation.Required;

public class SimEventReport implements Report {

	protected SimEvent eventPrototype;
	
	protected ReportVariables reportVariables;
	
	@Override
	public void eventOccurred(SimEvent event) {
		if (event instanceof SimulationStartingEvent) {
			onSimulationStarting(event);
		} else if (event instanceof SimulationFinishedEvent) {
			onSimulationFinished(event);
		} else if (event.getClass().isAssignableFrom(eventPrototype.getClass())) {
			onEventPrototype(event);
		}
	}
	
	public void onEventPrototype(SimEvent event) {
		reportVariables.compute(event);
	}
	
	public void onSimulationStarting(SimEvent event) {
		reportVariables.initialise(event);
	}
	
	public void onSimulationFinished(SimEvent event) {
		reportVariables.dispose(event);
	}

	@Override
	public Map<Object, Number> getVariableBindings() {
		return reportVariables.getVariableBindings();
	}

	public ReportVariables getReportVariables() {
		return reportVariables;
	}

	@Required
	public void setReportVariables(ReportVariables reportVariables) {
		this.reportVariables = reportVariables;
	}

	public SimEvent getEventPrototype() {
		return eventPrototype;
	}

	@Required
	public void setEventPrototype(SimEvent eventPrototype) {
		this.eventPrototype = eventPrototype;
	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	
}
