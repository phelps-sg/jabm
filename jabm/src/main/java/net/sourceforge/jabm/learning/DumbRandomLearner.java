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


import java.io.Serializable;

import net.sourceforge.jabm.report.DataWriter;
import net.sourceforge.jabm.util.Prototypeable;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

/**
 * A learner that simply plays a random action on each iteration without any
 * learning. This is useful for control experiments.
 * 
 * @author Steve Phelps
 * @version $Revision: 189 $
 */

public class DumbRandomLearner extends AbstractLearner implements
    StimuliResponseLearner, Serializable, Prototypeable {

	protected int numActions;

	protected Uniform distribution;

	public static final int DEFAULT_NUM_ACTIONS = 10;

	public DumbRandomLearner(RandomEngine prng) {
		this(DEFAULT_NUM_ACTIONS, prng);
	}

	public DumbRandomLearner(int numActions, RandomEngine prng) {
		this.numActions = numActions;
		distribution = new Uniform(0, 1, prng);
	}

//	public void setup(ParameterDatabase params, Parameter base) {
//		numActions = params.getIntWithDefault(base.push(P_K), null,
//		    DEFAULT_NUM_ACTIONS);
//	}

	public Object protoClone() {
		try {
			return this.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	public void reset() {
		// Do nothing
	}

	public int act() {
		return distribution.nextIntFromTo(0, numActions-1);
	}

	public double getLearningDelta() {
		return 0.0;
	}

	public void dumpState(DataWriter out) {
		// TODO
	}

	public int getNumberOfActions() {
		return numActions;
	}

	public void reward(double reward) {
		// No action
	}

	@Override
	public int bestAction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int worstAction() {
		// TODO Auto-generated method stub
		return 0;
	}

}
