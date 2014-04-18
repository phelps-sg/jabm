/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2013 Steve Phelps
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at e;your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package net.sourceforge.jabm.learning;



import java.io.Serializable;

import net.sourceforge.jabm.report.DataWriter;
import net.sourceforge.jabm.util.FixedLengthQueue;
import net.sourceforge.jabm.util.Prototypeable;
import cern.jet.random.AbstractContinousDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

/**
 * maintains a sliding window over the trained data series and use the average
 * of data items falling into the window as the output learned.
 * 
 * @author Jinzhong Niu
 * @version $Revision: 104 $
 */

public class SlidingWindowLearner extends AbstractLearner implements
    MimicryLearner, SelfKnowledgable, Prototypeable, Serializable {
	
	protected AbstractContinousDistribution randomParamDistribution;

	/**
	 * A parameter used to adjust the size of the window
	 */
	protected int windowSize = 4;

	public static final String P_WINDOWSIZE = "windowsize";

	/**
	 * The current output level.
	 */
	protected double currentOutput;

	public static final String P_DEF_BASE = "slidingwindowlearner";

	protected FixedLengthQueue memory;

	public SlidingWindowLearner(RandomEngine prng) {
		randomParamDistribution = new Uniform(1, 10, prng);
	}
	
	public SlidingWindowLearner(
			AbstractContinousDistribution randomParamDistribution) {
		this.randomParamDistribution = randomParamDistribution;
	}

//	public void setup(ParameterDatabase parameters, Parameter base) {
//		super.setup(parameters, base);
//
//		windowSize = parameters.getIntWithDefault(base.push(P_WINDOWSIZE),
//		    new Parameter(P_DEF_BASE).push(P_WINDOWSIZE), windowSize);
//
//		initialise();
//	}

	public void initialise() {
		createMemory();
	}

	public void reset() {
		if (memory != null) {
			memory.reset();
		}
	}

	public void randomInitialise() {
		windowSize = randomParamDistribution.nextInt();
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	public int getWindowSize() {
		return windowSize;
	}

	protected void createMemory() {
		assert (windowSize >= 1);
		memory = new FixedLengthQueue(windowSize);
	}

	public double act() {
		return currentOutput;
	}

	public void train(double target) {
		memory.newData(target);
		currentOutput = memory.getMean();
	}

	public void dumpState(DataWriter out) {
		// TODO
	}

	public double getCurrentOutput() {
		return currentOutput;
	}

	/**
	 * no effect on FixedLengthQueue-based next output!
	 */
	public void setOutputLevel(double currentOutput) {
		this.currentOutput = currentOutput;
	}

	public double getLearningDelta() {
		return 0;
	}

	public Object protoClone() {
		SlidingWindowLearner clone = new SlidingWindowLearner(this.randomParamDistribution);
		clone.setWindowSize(windowSize);
		return clone;
	}

	public String toString() {
		return "(" + getClass().getSimpleName() + " windowSize:" + windowSize + ")";
	}

	public boolean goodEnough() {
		return memory.count() >= windowSize;
	}
}
