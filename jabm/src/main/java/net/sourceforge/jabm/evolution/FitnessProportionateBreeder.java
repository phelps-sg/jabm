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

import java.util.Comparator;

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
public class FitnessProportionateBreeder implements Breeder {

	/**
	 * The fitness function which specifies the fitness of each agent.
	 */
	protected FitnessFunction fitnessFunction;
	
	protected ImitationOperator imitationFunction = 
			new StrategyImitationOperator();
	
	protected RandomEngine prng;
	
	protected double totalFitness;
	
	protected double imitationProbability = 1.0;
	
	static Logger logger = Logger.getLogger(FitnessProportionateBreeder.class);
	
	
	public AgentList reproduce(AgentList currentGeneration) {
		
		int n = currentGeneration.size();
		AgentList nextGeneration = new AgentList(currentGeneration);
		
		double[] cummulativeFitnesses = cummulativeFitnesses(currentGeneration);
		
		if (!Double.isNaN(totalFitness)
				&& !Double.isInfinite(totalFitness)) {
			for (int i = 0; i < n; i++) {
				int j = choose(cummulativeFitnesses);
				reproduce(nextGeneration.get(i), currentGeneration.get(j));
			}
		} else {
			logger.warn("Not reproducing because fitness is undefined");
		}
		
		return nextGeneration;
	}

	public int choose(double[] cummulativeFitnesses) {
		double r = prng.nextDouble();
		int j = 0;
		while (j < cummulativeFitnesses.length && cummulativeFitnesses[j] < r) {
			j++;
		}
		return j;
	}
	
	public void reproduce(Agent child, Agent parent) {
		if (prng.nextDouble() < imitationProbability) {
			imitationFunction.inheritBehaviour(child, parent);
		}
	}

	public double[] cummulativeFitnesses(AgentList agents) {
		agents.sortAgents(new Comparator<Agent>() {
			public int compare(Agent o1, Agent o2) {
				return new Double(getFitness(o1)).compareTo(new Double(getFitness(o2)));
			}
		});
		double[] result = new double[agents.size()];
		this.totalFitness = 0.0;
		for(int i=0; i<result.length; i++) {
			totalFitness += getFitness(agents.get(i));
		}
		double cummulativeTotal = 0.0;
		for(int i=0; i<result.length; i++) {
			double fitness = getFitness(agents.get(i));
			cummulativeTotal += fitness;
			result[i] = cummulativeTotal / totalFitness;
		}
		return result;
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
		return imitationFunction;
	}

	public void setImitationOperator(ImitationOperator imitationFunction) {
		this.imitationFunction = imitationFunction;
	}

	public double getImitationProbability() {
		return imitationProbability;
	}

	public void setImitationProbability(double imitationProbability) {
		this.imitationProbability = imitationProbability;
	}
	
}
