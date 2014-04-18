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

import net.sourceforge.jabm.event.RandomVariateInitialisedEvent;
import net.sourceforge.jabm.event.SimEvent;

public class RandomVariateReportVariables extends AbstractReportVariables {

	protected LinkedHashMap<String, Double> bindings
		= new LinkedHashMap<String, Double>();
//	
//	protected LinkedHashMap<String, Double> reportedBindings
//		= new LinkedHashMap<String, Double>();
	
	public RandomVariateReportVariables() {
		super("randomvariates");
	}

	@Override
	public void eventOccurred(SimEvent ev) {
		super.eventOccurred(ev);
		if (ev instanceof RandomVariateInitialisedEvent) {
			onRandomVariateInitialised(ev);
		}
//		} else if (ev instanceof SimulationFinishedEvent) {
//			onSimulationFinished((SimulationFinishedEvent) ev);
//		}
	}

	public void onRandomVariateInitialised(SimEvent ev) {
		RandomVariateInitialisedEvent event =
			(RandomVariateInitialisedEvent) ev;
		bindings.put(event.getPropertyName(), event.getValue());
	}

	@Override
	public void compute(SimEvent event) {
//		this.reportedBindings = this.bindings;
	}
//	
//	public void onSimulationFinished(SimulationFinishedEvent event) {
//		bindings.clear();
//	}

	@Override
	public Map<Object, Number> getVariableBindings() {
		Map<Object, Number> result = super.getVariableBindings();
		result.putAll(this.bindings);
		return result;
	}

	@Override
	public void dispose(SimEvent event) {
		super.dispose(event);
	}

	@Override
	public void initialise(SimEvent event) {
		super.initialise(event);
		this.bindings = new LinkedHashMap<String, Double>();
	}
	

}
