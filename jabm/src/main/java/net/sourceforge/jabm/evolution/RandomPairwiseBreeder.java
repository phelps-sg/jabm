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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.engine.RandomEngine;

/**
 * <p>
 * A breeder which implements Fitness-proportionate reproduction. Agents are
 * selected for inclusion in the next generation with a probability
 * proportionate to their fitness, as defined by an exogenous fitness function.
 * When agents reproduce they do so via an {@link ImitationOperator} which
 * specifies how agents are copied from one generation to the next.
 * </p>
 * 
 * @author Steve Phelps
 */
public class RandomPairwiseBreeder implements Breeder {

	/**
	 * The fitness function which specifies the fitness of each agent.
	 */
	protected FitnessFunction fitnessFunction;
	
	protected ImitationOperator imitationOperator = 
			new StrategyImitationOperator();
	
	protected RandomEngine prng;
	
	protected double mixing = 1.0;
	
	static Logger logger = Logger.getLogger(RandomPairwiseBreeder.class);
	
	
	public AgentList reproduce(AgentList currentGeneration) {
		
		int n = currentGeneration.size();
		AgentList nextGeneration = new AgentList(currentGeneration);
		
		int numPairs = (int) Math.round(mixing * (n*n));
		for(int i=0; i<numPairs; i++) {
			Agent x = chooseRandomAgent(currentGeneration);
			Agent y = chooseRandomAgent(currentGeneration);
			if (x != y) {
				if (getFitness(x) > getFitness(y)) {
					imitationOperator.inheritBehaviour(y, x);
				} else if (getFitness(x) < getFitness(y)) {
					imitationOperator.inheritBehaviour(x, y);
				}
			}
		}
		return nextGeneration;
	}

	public Agent chooseRandomAgent(AgentList agents) {
		int n = agents.size();
		int i = (int) Math.round(prng.nextDouble() * (n-1));
		return agents.get(i);
	}

	public double getFitness(Agent i) {
        double result = 0.0;
		if (fitnessFunction != null) {
			result = fitnessFunction.getFitness(i);
		} else {
			result = i.getPayoff();
		}
        if (result < 0.0) {
            result = 0.0;
        }
        return result;
	}

	public FitnessFunction getFitnessFunction() {
		return fitnessFunction;
	}

	public void setFitnessFunction(FitnessFunction fitnessFunction) {
		this.fitnessFunction = fitnessFunction;
	}

	public RandomEngine getPrng() {
		return prng;
	}

	@Required
	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

	public ImitationOperator getImitationOperator() {
		return imitationOperator;
	}

	public void setImitationOperator(ImitationOperator imitationFunction) {
		this.imitationOperator = imitationFunction;
	}

	public double getMixing() {
		return mixing;
	}

	public void setMixing(double mixing) {
		this.mixing = mixing;
	}

	
}
