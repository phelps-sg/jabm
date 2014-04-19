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
import net.sourceforge.jabm.learning.EpsilonGreedyActionSelector;
import net.sourceforge.jabm.learning.QLearner;
import net.sourceforge.jabm.test.PRNGTestSeeds;
import net.sourceforge.jabm.util.SummaryStats;

import org.apache.log4j.Logger;

import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;


public class QLearnerTest extends TestCase {

	QLearner learner1;
	
	EpsilonGreedyActionSelector actionSelector;

	double score;
	
	RandomEngine prng;
	
	static final double EPSILON = 0.05;

	static final double LEARNING_RATE = 0.8;

	static final double DISCOUNT_RATE = 0.9;

	static final int NUM_ACTIONS = 10;

	static final int CORRECT_ACTION = 2;

	static final int NUM_TRIALS = 20000;

	static Logger logger = Logger.getLogger(QLearnerTest.class);

	public QLearnerTest(String name) {
		super(name);
	}

	public void setUp() {
		prng = new MersenneTwister64(PRNGTestSeeds.UNIT_TEST_SEED); 		
		learner1 = new QLearner(1, NUM_ACTIONS, LEARNING_RATE,
		    DISCOUNT_RATE, prng);
		actionSelector = new EpsilonGreedyActionSelector(prng);
		actionSelector.setPrng(prng);
		actionSelector.setEpsilon(EPSILON);
		learner1.setActionSelector(actionSelector);
		score = 0;
	}

	public void testBestAction() {
		((EpsilonGreedyActionSelector) learner1.getActionSelector()).setEpsilon(0.0);
		System.out.println("testBestAction()");
		SummaryStats stats = new SummaryStats("action");
		int correctActions = 0;
		for (int i = 0; i < NUM_TRIALS; i++) {
			int action = learner1.act();			
			stats.newData(action);
			if (action == CORRECT_ACTION) {
				learner1.newState(1.0, 0);
				correctActions++;
			} else {
				learner1.newState(0.0, 0);
			}
		}
		logger.info("final state of learner1 = " + learner1);
		logger.info("learner1 score = " + score(correctActions) + "%");
		assertTrue(learner1.bestAction(0) == CORRECT_ACTION);
		System.out.println(stats);
	}

	public void testMinimumScore() {
		logger.info("testMinimumScore()");
		SummaryStats stats = new SummaryStats("action");
		int correctActions = 0;
		int bestActionChosen = 0;
		for (int i = 0; i < NUM_TRIALS; i++) {
			int action = learner1.act();
			stats.newData(action);
			assertTrue(action == learner1.getLastActionChosen());
			int bestAction = learner1.bestAction(0);
			if (bestAction == action) {
				bestActionChosen++;
			}
			if (action == CORRECT_ACTION) {
				learner1.newState(1.0, 0);
				correctActions++;
			} else {
				learner1.newState(0.0, 0);
			}
		}
		logger.info("final state of learner1 = " + learner1);
		double score = score(correctActions);
		double bestActionPercent = score(bestActionChosen);
		logger.info("learner1 score = " + score + "%");
		logger.info(stats);
		logger.info("chose best action " + bestActionPercent + "% of the time.");
		assertTrue(score > 80);
		assertTrue(1 - (bestActionPercent / 100) <= EPSILON);
	}

	public void testStates() {
		System.out.println("testStates()");
		int[] correctChoices = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int correctActions = 0;
		learner1.setStatesAndActions(correctChoices.length, NUM_ACTIONS);
		int s = 3;
		for (int i = 0; i < NUM_TRIALS; i++) {
			int action = learner1.act();
			double reward = 0;
			if (action == correctChoices[s]) {
				reward = 1.0;
				correctActions++;
				if (++s > 9) {
					s = 0;
				}
			}
			learner1.newState(reward, s);
			assertTrue(learner1.getState() == s);
		}
		score = score(correctActions);
		System.out.println("score = " + score + "%");
		System.out.println("learner1 = " + learner1);
		assertTrue(score >= 70);
	}

	public void testReset() {
		RandomEngine prng = new MersenneTwister64(PRNGTestSeeds.UNIT_TEST_SEED);
		System.out.println("testReset()");
		System.out.println("virgin learner1 = " + learner1);
		learner1.setPrng(prng);
		actionSelector.setPrng(prng);
		learner1.reset();
		testStates();
		double score1 = score;
		System.out.println("score1 = " + score1);
		
		prng = new MersenneTwister64(PRNGTestSeeds.UNIT_TEST_SEED);
		learner1.setPrng(prng);
		actionSelector.setPrng(prng);
		learner1.reset();
		System.out.println("reseted learner1 = " + learner1);
		testStates();
		double score2 = score;
		System.out.println("score2 = " + score2);
		assertTrue(score1 == score2);
	}

	public double score(int numCorrect) {
		return ((double) numCorrect / (double) NUM_TRIALS) * 100;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(QLearnerTest.class);
	}
}