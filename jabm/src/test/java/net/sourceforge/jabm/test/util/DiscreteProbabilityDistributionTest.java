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

package net.sourceforge.jabm.test.util;

import org.apache.commons.math3.stat.Frequency;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.jabm.prng.DiscreteProbabilityDistribution;
import net.sourceforge.jabm.test.PRNGTestSeeds;
import net.sourceforge.jabm.util.SummaryStats;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;

/**
 * @author Steve Phelps
 * @version $Revision: 1.2 $
 */

public class DiscreteProbabilityDistributionTest extends TestCase {

	protected DiscreteProbabilityDistribution p[];
	
	protected RandomEngine prng;

	static final int NUM_TRIALS = 10000000;

	static final double probs[][] = { { 0.1, 0.2, 0.4, 0.2, 0.1 },
	    { 0.2, 0.2, 0.2, 0.2, 0.2 }, { 0.25, 0.2, 0.1, 0.2, 0.25 },
	    { 0, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0 }, { 0, 0, 0, 0, 1 } };

	public DiscreteProbabilityDistributionTest(String name) {
		super(name);
	}

	public void setUp() {
		prng = new MersenneTwister64(PRNGTestSeeds.UNIT_TEST_SEED);
		p = new DiscreteProbabilityDistribution[probs.length];
		for (int i = 0; i < probs.length; i++) {
			p[i] = new 
			DiscreteProbabilityDistribution(prng, probs[i].length);
			for (int j = 0; j < probs[i].length; j++) {
				p[i].setProbability(j, probs[i][j]);
			}
		}
	}
	
	public void compareDistributions(DiscreteProbabilityDistribution subject, double[] probs) {

		System.out
		    .println("Testing with subject: " + subject);
		
		Frequency frequency = new Frequency();
		SummaryStats eventData = new SummaryStats(
		    "Event_Data");
		for (int trial = 0; trial < NUM_TRIALS; trial++) {
			int event = subject.generateRandomEvent();
			eventData.newData(event);
			frequency.addValue(event);
		}
		System.out.println(frequency);
		System.out.println(eventData);
		double mean = subject.computeMean();

		System.out.println("target mean = " + mean);
		
		for(int j=0; j<probs.length; j++) {
			assertTrue(approxEqual(probs[j], frequency.getPct(j)));
		}

		assertTrue(approxEqual(eventData.getMean(), mean));
	}
	
	public void testDynamicBehaviour() {

		double[] probs = { 0.1, 0.8, 0.1 };
		DiscreteProbabilityDistribution subject = 
			new DiscreteProbabilityDistribution(prng, probs);
				
		subject.setProbability(0, 0.1);
		subject.setProbability(1, 0.8);
		subject.setProbability(2, 0.1);

		compareDistributions(subject, probs);

		subject.setProbability(0, 0.8);
		subject.setProbability(1, 0.1);
		probs[0] = 0.8;
		probs[1] = 0.1;

		compareDistributions(subject, probs);

		
	}

	public void testStats() {
		for (int test = 0; test < p.length; test++) {
			DiscreteProbabilityDistribution subject = p[test];
			compareDistributions(subject, probs[test]);
			
		}
	}

	public boolean approxEqual(double x, double y) {
		return Math.abs(x - y) < 0.005;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(DiscreteProbabilityDistributionTest.class);
	}
}
