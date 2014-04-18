/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2013 Steve Phelps
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package net.sourceforge.jabm.learning;

import cern.jet.random.engine.RandomEngine;

/**
 * <p>
 * A modification of RothErev to address parameter degeneracy, and modified
 * learning with 0-reward. These modifications are made in the context of using
 * the RE algorithm for trader agents in a double auction. See:
 * </p>
 * <p>
 * "Market Power and Efficiency in a Computational Electricity Market with
 * Discriminatory Double-Auction Pricing" Nicolaisen, Petrov & Tesfatsion<br>
 * in IEEE Transactions on Evolutionary Computation Vol. 5, No. 5, p 504.
 * </p>
 * 
 * @author Steve Phelps
 * @version $Revision: 16 $
 */

public class NPTRothErevLearner extends RothErevLearner {

	
	public NPTRothErevLearner(int k, double r, double e, double s1,
			RandomEngine prng) {
		super(k, r, e, s1, prng);
	}

	public NPTRothErevLearner(RandomEngine prng) {
		super(prng);
	}
	
	public NPTRothErevLearner(int k, RandomEngine prng, double[] propensities) {
		super(k, prng, propensities);
	}

	public NPTRothErevLearner(int k, RandomEngine prng) {
		super(k, prng);
	}

	/**
	 * The modified update function.
	 */
	public double experience(int i, int action, double reward) {
		if (i == action) {
			return reward * (1 - e);
		} else {
			return q[i] * (e / (double) (k - 1));
		}
	}

}