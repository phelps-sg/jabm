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
package net.sourceforge.jabm.evolution;

import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.agent.AgentList;

import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.engine.RandomEngine;

public class MutationBreeder implements Breeder {

	protected double mutationProbability = 0.1;
	
	protected RandomEngine prng;
	
	protected MutationOperator mutationOperator;
	
	@Override
	public AgentList reproduce(AgentList currentGeneration) {
		for(Agent agent : currentGeneration.getAgents()) {
			if (prng.nextDouble() < mutationProbability) {
				mutationOperator.mutate(agent);
			}
		}
		return currentGeneration;
	}

	public double getMutationProbability() {
		return mutationProbability;
	}

	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	public RandomEngine getPrng() {
		return prng;
	}

	@Required
	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

	public MutationOperator getMutationOperator() {
		return mutationOperator;
	}

	public void setMutationOperator(MutationOperator mutationOperator) {
		this.mutationOperator = mutationOperator;
	}
	
}
