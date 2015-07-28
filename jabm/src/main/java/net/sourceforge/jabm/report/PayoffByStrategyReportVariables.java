/*
 * JABM - Java Agent-Based Modeling Toolkit
 * Copyright (C) 2015 Steve Phelps
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

import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.SimulationController;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.event.ReportVariablesChangedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationEvent;
import net.sourceforge.jabm.strategy.Strategy;

public class PayoffByStrategyReportVariables extends AbstractReportVariables
		implements ReportVariables, Serializable {

	private PayoffMap payoffMap = new PayoffMap();

	public PayoffByStrategyReportVariables() {
		super("payoffs");
	}

	@Override
	public void compute(SimEvent e) {
		SimulationEvent event = (SimulationEvent) e;
		SimulationController controller = event.getSimulation()
				.getSimulationController();
		Population population = controller.getPopulation();
		for (Agent agent : population.getAgentList().getAgents()) {
			if (agent.isInteracted()) {
				double fitness = getPayoff(agent);
				Strategy strategy = getStrategy(agent);
				updatePayoff(strategy, fitness);
			}
		}
		fireEvent(new ReportVariablesChangedEvent(this));
	}
	
	public Strategy getStrategy(Agent agent) {
		return agent.getStrategy();
	}
	
	public void updatePayoff(Strategy strategy, double fitness) {
		if (!Double.isInfinite(fitness) & !Double.isNaN(fitness)) { //TODO Configurable?
			payoffMap.updatePayoff(strategy, fitness);
		}
	}
	
	public double getPayoff(Agent agent) {
		return agent.getPayoff();
	}
	
	public Map<Object, Number> getVariableBindings() {
        Map<Object, Number> result = super.getVariableBindings();
        for (Strategy strategy : payoffMap.getStrategies()) {
            double meanPayoff = payoffMap.getMeanPayoff(strategy);
            result.put("payoff." + strategy, meanPayoff);
        }
        return result;
    }
	
	public PayoffMap getPayoffMap() {
		return payoffMap;
	}
	
	public void setPayoffMap(PayoffMap payoffMap) {
		this.payoffMap = payoffMap;
	}

	@Override
	public void dispose(SimEvent event) {
	}

	@Override
	public void initialise(SimEvent event) {
		initialise();
	}
	
	public void initialise() {
		payoffMap.initialise();
	}

}

