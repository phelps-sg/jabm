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
package net.sourceforge.jabm.init;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.agent.Agent;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class ProportionalCombiAgentInitialiser implements AgentInitialiser,
		Serializable, InitializingBean {

	protected float[] proportions;

	protected List<AgentInitialiser> initialisers;

	public ProportionalCombiAgentInitialiser(float[] proportions,
			List<AgentInitialiser> initialisers) {
		this();
		this.proportions = proportions;
		this.initialisers = initialisers;
	}
	
	public ProportionalCombiAgentInitialiser() {
		super();
	}

	public void initialise(Population population) {
		Collection<Agent> agents = population.getAgents();
		int total = agents.size();
		Iterator<Agent> agentIterator = agents.iterator();
		for (int p = 0; p < proportions.length; p++) {
			float proportion = proportions[p];
			AgentInitialiser subInitialiser = initialisers.get(p);
			int n = Math.round(proportion * total);
			Population subPopulation = new Population();
			for (int i = 0; i < n; i++) {
				Agent agent = agentIterator.next();
				subPopulation.add(agent);
				subInitialiser.initialise(subPopulation);
			}
		}
	}

	public float[] getProportions() {
		return proportions;
	}

	@Required
	public void setProportions(float[] proportions) {
		this.proportions = proportions;
	}

	public List<AgentInitialiser> getInitialisers() {
		return initialisers;
	}

	@Required
	public void setInitialisers(List<AgentInitialiser> initialisers) {
		this.initialisers = initialisers;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (initialisers.size() == proportions.length+1 ) {
			inferMissingProportion();
		}
		if (! (initialisers.size() == proportions.length)) {
			throw new RuntimeException("Proportion/initialisers size mismatch");
		}
		checkSumToOne();
	}

	public void inferMissingProportion() {
		float[] shortProportions = this.proportions;
		int n = shortProportions.length;			
		this.proportions = Arrays.copyOf(shortProportions, n + 1);
		float sigma = 0;
		for(int i=0; i<n; i++) {
			sigma += shortProportions[i];
		}
		this.proportions[n] = 1 - sigma;
	}

	public void checkSumToOne() {
		float sigma = 0.0f;
		for(int i=0; i<proportions.length; i++) {
			sigma += proportions[i];
		}
		if ( (sigma > 1.00001f) || (sigma < 0) ) {
			throw new RuntimeException("Proportions do not sum to 1");
		}
	}
	
}
