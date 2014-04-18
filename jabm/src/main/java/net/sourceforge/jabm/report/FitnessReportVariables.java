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

import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.SimulationController;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationEvent;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;

public class FitnessReportVariables extends AbstractReportVariables {

	protected SummaryStatistics stats = new SummaryStatistics();
	
	static Logger logger = Logger.getLogger(FitnessReportVariables.class);

	public FitnessReportVariables() {
		super("fitness");
	}
	
	@Override
	public Map<Object, Number> getVariableBindings() {
		Map<Object, Number> result = super.getVariableBindings();
		recordSummaryStatistics("population", result, stats);
		return result;
	}

	@Override
	public void compute(SimEvent event) {
		super.compute(event);
		if (event instanceof SimulationEvent) {
			onSimulationEvent((SimulationEvent) event);
		}
	}

	public void onSimulationEvent(SimulationEvent event) {
		stats = new SummaryStatistics();
		SimulationController controller = event.getSimulation()
				.getSimulationController();
		Population population = controller.getPopulation();
		for (Agent agent : population.getAgents()) {
			double fitness = agent.getPayoff();
			stats.addValue(fitness);
		}
		logger.debug(stats.toString());
	}
	
	@Override
	public void initialise(SimEvent event) {
		stats.clear();
	}

}
