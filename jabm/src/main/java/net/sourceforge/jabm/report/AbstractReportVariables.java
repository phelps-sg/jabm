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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sourceforge.jabm.Simulation;
import net.sourceforge.jabm.event.AbstractModel;
import net.sourceforge.jabm.event.ReportVariablesChangedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationEvent;
import net.sourceforge.jabm.event.SimulationStartingEvent;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public abstract class AbstractReportVariables extends AbstractModel 
		implements XYReportVariables {

	protected long timeStamp;
	
	protected String name;
	
	protected String xVariableName;
	
	protected List<Object> yVariableNames = new LinkedList<Object>();

	protected Simulation simulation;

	public AbstractReportVariables(String name) {
		super();
		this.name = name;
		this.xVariableName = name + ".t";
	}

	public Map<Object, Number> getVariableBindings() {
		LinkedHashMap<Object, Number> result 
			= new LinkedHashMap<Object,Number>();
		result.put(this.xVariableName, timeStamp);
		return result;
	}

	public Object createVariable(String variableName) {
		return this.name + "." + variableName;
	}
	
	public void recordMoments(Object statName, Map<Object, Number> variables,
			SummaryStatistics stats) {
		variables.put(createVariable(statName + ".mean"), stats.getMean());
		variables.put(createVariable(statName + ".variance"),
				stats.getVariance());
	}
	
	public void recordSummaryStatistics(Object statName,
			Map<Object, Number> variables, SummaryStatistics stats) {
		variables.put(createVariable(statName + ".mean"), stats.getMean());
		variables.put(createVariable(statName + ".min"), stats.getMin());
		variables.put(createVariable(statName + ".max"), stats.getMax());
		variables.put(createVariable(statName + ".n"), stats.getN());
		variables.put(createVariable(statName + ".stdev"), stats
				.getStandardDeviation());
	}
	
	public void compute(SimEvent event) {	
//		if (event instanceof SimulationEvent) {
//			onSimulationEvent((SimulationEvent) event);
//		}
		if (simulation != null) {
			timeStamp = this.simulation.getSimulationTime().getTicks();
		}
		fireEvent(new ReportVariablesChangedEvent(this));
	}
//
//	public void onSimulationEvent(SimulationEvent event) {
//		timeStamp = event.getSimulation().getSimulationTime().getTicks();
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	@Override
	public void eventOccurred(SimEvent event) {
		if (event instanceof SimulationStartingEvent) {
			this.simulation = ((SimulationEvent) event).getSimulation();
		}
	}

	@Override
	public void dispose(SimEvent event) {
		// Default is do nothing
	}

	@Override
	public void initialise(SimEvent event) {
//		this.yVariableNames = new LinkedList<Object>();
//		this.yVariableNames.addAll(getVariableBindings().keySet());
	}

	@Override
	public Number getX(int seriesIndex) {
		return getVariableBindings().get(this.xVariableName);
	}

	@Override
	public Number getY(int seriesIndex) {
//		return getVariableBindings().get(
//				this.yVariableNames.get(seriesIndex + 1));
		return getVariableBindings().get(getyVariableNames().get(seriesIndex));
	}

	@Override
	public String getxVariableName() {
		return xVariableName;
	}

	@Override
	public List<Object> getyVariableNames() {
		return yVariableNames;
	}

	@Override
	public int getNumberOfSeries() {
		return getVariableBindings().size() - 1;
	}
	
	public String tag(Object strategy) {
		if (strategy instanceof Taggable) {
			return ((Taggable) strategy).getTag();
		} else {
			return strategy.getClass().toString();
		}
	}


}