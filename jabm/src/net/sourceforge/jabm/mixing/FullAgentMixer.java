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
 * Every agent interacts with every other agent.
 * 
 * @author Steve Phelps
 *
 */
public class FullAgentMixer implements AgentMixer, Serializable {
	
	protected RandomEngine prng;
	
	static Logger logger = Logger.getLogger(FullAgentMixer.class);
	
	public FullAgentMixer(RandomEngine prng) {
		super();
		this.prng = prng;
	}
	
	public FullAgentMixer() {
		this(null);
	}
	
	public void invokeInteraction(AgentList group, SimulationController model) {
		group.shuffle(prng);
		for(Agent ai : group.getAgents()) {
			ArrayList<Agent> agents = new ArrayList<Agent>(group.size() - 1);
			for(Agent aj : group.getAgents()) {
				if (ai != aj) {
					agents.add(aj);
				}
			}
			if (logger.isDebugEnabled())
				logger.debug(ai + " interacting with " + agents);
			model.fireEvent(new AgentArrivalEvent(model, ai, agents));
		}
	}
	
	public void invokeAgentInteractions(Population population, 
											SimulationController model) {
		invokeInteraction(population.getAgentList(), model);
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
