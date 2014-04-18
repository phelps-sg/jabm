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
package net.sourceforge.jabm.learning;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

/**
 * <p>
 * An implementation of the epsilon-greedy action selection policy.
 * </p>
 * 
 * <p>
 * See:<br>
 * Sutton, R. S., Barto, A. G., 1998. Reinforcement Learning: An Introduction.
 * MIT Press.<br>
 * </p>
 * 
 * @author Steve Phelps
 * 
 */
public class EpsilonGreedyActionSelector implements ActionSelector {

	/**
	 * The parameter representing the probability of choosing a random action on
	 * any given iteration.
	 */
	protected double epsilon;

	/**
	 * |The pseudo-random number generator used to randomly select whether to
	 * explore and to randomly select an action when the algorithm is exploring.
	 */
	protected RandomEngine prng;
	
	static Logger logger = Logger.getLogger(EpsilonGreedyActionSelector.class);
	
	public static final double DEFAULT_EPSILON = 0.01;
	
	public EpsilonGreedyActionSelector(double epsilon, RandomEngine prng) {
		super();
		this.epsilon = epsilon;
		this.prng = prng;
	}
	
	public EpsilonGreedyActionSelector(RandomEngine prng) {
		this(DEFAULT_EPSILON, prng);
	}
	
	public EpsilonGreedyActionSelector() {
		this(DEFAULT_EPSILON, null);
	}

	@Override
	public int act(int state, MDPLearner qLearner) {
		if (prng.raw() <= epsilon) {
			// lastActionChosen = prng.choose(0, numActions-1);
			Uniform dist = new Uniform(prng);
			int randomAction = dist.nextIntFromTo(0,
			    qLearner.getNumberOfActions() - 1);
			return randomAction;
		} else {
			return qLearner.bestAction(state);
		}
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
		if (logger.isDebugEnabled()) {
			logger.debug("epsilon = " + epsilon);
		}
	}

	public RandomEngine getPrng() {
		return prng;
	}

	@Required
	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

}
