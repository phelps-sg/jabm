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
package net.sourceforge.jabm.mixing;

import java.io.Serializable;
import java.util.ArrayList;

import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.SimulationController;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.agent.AgentList;
import net.sourceforge.jabm.event.AgentArrivalEvent;
import net.sourceforge.jabm.event.SimEvent;

import org.apache.log4j.Logger;

/**
 * Every pair of agents interacts with each other.
 * 
 * @author Steve Phelps
 *
 */
public class FullPairwiseAgentMixer implements AgentMixer, Serializable {
	
	protected int numSteps = 1;
	
	static Logger logger = Logger.getLogger(FullPairwiseAgentMixer.class);
	
	public FullPairwiseAgentMixer() {
	}
	
	public void invokeInteraction(AgentList group, SimulationController simulation) {
		ArrayList<Agent> agents = new ArrayList<Agent>(group.size());
		for(Agent ai : group.getAgents()) {
			for(Agent aj : group.getAgents()) {
				if (ai != aj) {					
					agents.add(aj);
				}
			}
			simulation.fireEvent(new AgentArrivalEvent(simulation, ai, agents));
		}
	}
	
	public void invokeInteractions(AgentList agentList, SimulationController simulation) {
		for(int i=0; i<numSteps; i++) {
			invokeInteraction(agentList, simulation);
		}
	}
	
	@Override
	public void invokeAgentInteractions(Population population, SimulationController simulation) {
		invokeInteractions(population.getAgentList(), simulation);
	}
	
	public int getNumSteps() {
		return numSteps;
	}

	public void setNumSteps(int numSteps) {
		this.numSteps = numSteps;
	}

	public void eventOccurred(SimEvent event) {
	}

	
}
