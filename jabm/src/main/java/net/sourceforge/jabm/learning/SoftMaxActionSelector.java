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

import net.sourceforge.jabm.prng.DiscreteProbabilityDistribution;
import cern.jet.random.engine.RandomEngine;

/**
 * <p>
 * An implementation of the softmax action selection policy.
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
public class SoftMaxActionSelector implements ActionSelector {

	protected RandomEngine prng;
	
	/**
	 * The "temperature" used to modulate the propensity distribution.
	 */
	protected double tau;
	
	public SoftMaxActionSelector() {
		super();
	}
	
	public SoftMaxActionSelector(RandomEngine prng, double tau) {
		super();
		this.prng = prng;
		this.tau = tau;
	}

	@Override
	public int act(int state, MDPLearner learner) {
		double q[] = learner.getValueEstimates(state);
		double p[] = new double[q.length];
		double total = 0;
		double totalQ = 0;
		for(int i=0; i<p.length; i++) {
			double propensity = Math.exp(q[i]/tau);
			p[i] = propensity;
			total += propensity;
			totalQ += q[i];
		}
		if (Math.abs(totalQ) > 10e-6) {
			for(int i=0; i<q.length; i++) {
				p[i] = p[i] / total;
			}
		} else {
			for(int i=0; i<q.length; i++) {
				p[i] = 1.0/(double) q.length;
			}
		}
		
		DiscreteProbabilityDistribution dist = 
			new DiscreteProbabilityDistribution(prng, p);
		return dist.generateRandomEvent();
	}

	public RandomEngine getPrng() {
		return prng;
	}

	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

	public double getTau() {
		return tau;
	}

	/**
	 * @param tau  The "temperature" used to modulate the propensities.
	 */
	public void setTau(double tau) {
		this.tau = tau;
	}

	
	
}
