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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.jabm.learning.WidrowHoffLearner;
import net.sourceforge.jabm.test.PRNGTestSeeds;
import net.sourceforge.jabm.util.MathUtil;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;

public class WidrowHoffLearnerTest extends TestCase {

	/**
	 * @uml.property name="learner1"
	 * @uml.associationEnd
	 */
	WidrowHoffLearner learner1;

	/**
	 * @uml.property name="score"
	 */
	double score;
	
	RandomEngine prng;

	static final double LEARNING_RATE = 0.8;

	static final double TARGET_VALUE = 0.12;

	static final int ITERATIONS = 100;

	public WidrowHoffLearnerTest(String name) {
		super(name);
	}

	public void setUp() {
		prng = new MersenneTwister64(PRNGTestSeeds.UNIT_TEST_SEED);
		learner1 = new WidrowHoffLearner(LEARNING_RATE, prng);
	}

	public void testConvergence() {
		train(ITERATIONS);
		assertTrue(MathUtil.approxEqual(learner1.act(), TARGET_VALUE, 0.01));
		assertTrue(MathUtil.approxEqual(learner1.getLearningDelta(), 0, 0.01));
	}

	public void testReset() {
		train(2);
		assertTrue(learner1.getLearningDelta() > 0.01);
		learner1.reset();
		assertTrue(MathUtil.approxEqual(learner1.getLearningDelta(), 0, 0.00001));
	}

	protected void train(int iterations) {
		for (int i = 0; i < iterations; i++) {
			learner1.train(TARGET_VALUE);
			System.out.println("Learning delta = " + learner1.getLearningDelta());
			System.out.println("Current output = " + learner1.act());
		}
	}

	public static Test suite() {
		return new TestSuite(WidrowHoffLearnerTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
