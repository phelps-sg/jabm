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
import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.engine.RandomEngine;

/**
 * All agents arrive simultaneously at the simulation.  The agents
 * are shuffled into a random order prior to scheduling the AgentArrivalEvent.
 * @author Steve Phelps
 *
 */
public class RandomRobinAgentMixer implements AgentMixer, Serializable {
	
	protected RandomEngine prng;
	
	public static final ArrayList<Agent> EMPTY_LIST = new ArrayList<Agent>(0);
	
	static Logger logger = Logger.getLogger(RandomRobinAgentMixer.class);	
	
	public RandomRobinAgentMixer() {
	}
	
	public RandomRobinAgentMixer(RandomEngine prng) {
		this.prng = prng;
	}
	
	public void invokeInteractions(AgentList group, SimulationController model) {
		group.shuffle(prng);
		for (Agent agent : group.getAgents()) {
			AgentArrivalEvent event = 
				new AgentArrivalEvent(model, agent, EMPTY_LIST);
			model.fireEvent(event);
//			agent.interact(new ArrayList<Agent>(0));
		}
	}
	
	public void invokeAgentInteractions(Population population, SimulationController scheduler) {
		invokeInteractions(population.getAgentList(), scheduler);
	}
	
	public void eventOccurred(SimEvent event) {
	}

	public RandomEngine getPrng() {
		return prng;
	}

	@Required
	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

}
