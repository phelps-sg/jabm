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

import net.sourceforge.jabm.SimulationController;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.agent.AgentList;
import net.sourceforge.jabm.event.AgentArrivalEvent;

import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

public class StochasticPairwiseAgentMixer extends FullPairwiseAgentMixer
		implements Serializable {
	
	protected RandomEngine prng;
	
	public StochasticPairwiseAgentMixer(RandomEngine prng) {
		super();
		this.prng = prng;
	}

	public StochasticPairwiseAgentMixer() {
		super();
	}
	
	@Override
	public void invokeInteraction(AgentList group, 
			SimulationController simulation) {
		Uniform distribution = new Uniform(0, group.size() - 1, prng);
		int i, j;
		do {
			i = distribution.nextInt();
			j = distribution.nextInt();
		} while (i == j);
		if (logger.isDebugEnabled()) {
			logger.debug("i = " + i);
			logger.debug("j = " + j);
		}
		Agent ai = group.get(i);
		Agent aj = group.get(j);	
		ArrayList<Agent> agents = new ArrayList<Agent>(1);
		agents.add(aj);
		AgentArrivalEvent event = new AgentArrivalEvent(simulation, ai, agents);
		simulation.fireEvent(event);
	}

	public RandomEngine getPrng() {
		return prng;
	}

	@Required
	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}
	
}
