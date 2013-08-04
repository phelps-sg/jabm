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

import net.sourceforge.jabm.prng.DiscreteProbabilityDistribution;
import net.sourceforge.jabm.report.DataWriter;
import net.sourceforge.jabm.util.MathUtil;

import org.apache.log4j.Logger;

import cern.jet.random.engine.RandomEngine;

/**
 * <p>
 * A class implementing the Roth-Erev learning algorithm. This learning
 * algorithm is designed to mimic human-like behaviour in extensive form games.
 * See:
 * </p>
 * <p>
 * A.E.Roth and I. Erev "Learning in extensive form games: experimental data and
 * simple dynamic models in the intermediate term" Games and Economic Behiour,
 * Volume 8
 * </p>
 * 
 * @author Steve Phelps
 * @version $Revision: 106 $
 */
public class RothErevLearner extends AbstractLearner implements 
    StimuliResponseLearner, Serializable {

	/**
	 * The number of choices available to make at each iteration.
	 */
	protected int k;

	/**
	 * The recency parameter.
	 */
	protected double r;

	/**
	 * The experimentation parameter.
	 */
	protected double e;

	/**
	 * The scaling parameter.
	 */
	protected double s1;

	/**
	 * Propensity for each possible action.
	 */
	protected double q[];

	/**
	 * Probabilities for each possible action.
	 */
	protected DiscreteProbabilityDistribution probabilities;

	/**
	 * The current iteration.
	 */
	protected int iteration;

	/**
	 * The last action chosen.
	 */
	protected int lastAction;

	/**
	 * The total amount of update to the probability vector on the last iteration.
	 */
	protected double deltaP;	

	static final int DEFAULT_K = 100;

	static final double DEFAULT_R = 0.1;

	static final double DEFAULT_E = 0.2;

	static final double DEFAULT_S1 = 1.0;

	static Logger logger = Logger.getLogger(RothErevLearner.class);

	/**
	 * Construct a new learner.
	 * 
	 * @param k
	 *          The no. of possible actions.
	 * @param r
	 *          The recency parameter.
	 * @param e
	 *          The experimentation parameter.
	 */
	public RothErevLearner(int k, double r, double e, double s1, RandomEngine prng) {
		this.k = k;
		this.r = r;
		this.e = e;
		this.s1 = s1;
		validateParams();
		q = new double[k];
		probabilities = new DiscreteProbabilityDistribution(prng, k);
		resetPropensities();
		updateProbabilities();
		iteration = 0;
	}

	public RothErevLearner(RandomEngine prng) {
		this(DEFAULT_K, DEFAULT_R, DEFAULT_E, DEFAULT_S1, prng);
	}
	
	public RothErevLearner(int k, RandomEngine prng) {
		this(k, DEFAULT_R, DEFAULT_E, DEFAULT_S1, prng);
	}
	
	public RothErevLearner(int k, RandomEngine prng, double[] propensities) {
		this(k, prng);
		setPropensities(propensities);
	}
	

	public Object protoClone() {
//		RothErevLearner clonedLearner;
		//TODO
//		try {
//			clonedLearner = (RothErevLearner) clone();
//			clonedLearner.probabilities = (DiscreteProbabilityDistribution) probabilities.protoClone();
//		} catch (CloneNotSupportedException e) {
//			throw new Error(e);
//		}
		return null;
	}

	protected void validateParams() {
		if (!(k > 0)) {
			throw new IllegalArgumentException("k must be positive");
		}
		if (!(r >= 0 && r <= 1)) {
			throw new IllegalArgumentException("r must range [0..1]");
		}
		if (!(e >= 0 && e <= 1)) {
			throw new IllegalArgumentException("e must range [0..1]");
		}
	}

	/**
	 * Generate the next action for this learner.
	 * 
	 * @return An int in the range [0..k) representing the choice made by the
	 *         learner.
	 */
	public int act() {
		int action = choose();
		lastAction = action;
		iteration++;
		return action;
	}

	/**
	 * Reward the last action taken by the learner according to some payoff.
	 * 
	 * @param reward
	 *          The payoff for the last action taken by the learner.
	 */
	public void reward(double reward) {
		assert reward >= 0;
		updatePropensities(lastAction, reward);
		updateProbabilities();
	}

	/**
	 * Choose a random number according to the probability distribution defined
	 * by the probabilities.
	 * 
	 * @return one of [0..k) according to the probabilities [0..k-1].
	 */
	public int choose() {
		return probabilities.generateRandomEvent();
	}

	/**
	 * Update the propensities for each possible action.
	 * 
	 * @param action
	 *          The last action chosen by the learner
	 */
	protected void updatePropensities(int action, double reward) {
		for (int i = 0; i < k; i++) {
			q[i] = (1 - r) * q[i] + experience(i, action, reward);
		}
	}

	/**
	 * Update the probabilities from the propensities.
	 */
	protected void updateProbabilities() {
		double sigmaQ = 0;
		for (int i = 0; i < k; i++) {
			sigmaQ += q[i];
		}
		deltaP = 0;
		for (int i = 0; i < k; i++) {
			double p1 = q[i] / sigmaQ;
			deltaP += MathUtil.diffSq(probabilities.getProbability(i), p1);
			probabilities.setProbability(i, p1);
		}
	}

	/**
	 * The experience function
	 * 
	 * @param i
	 *          The action under consideration
	 * 
	 * @param action
	 *          The last action chosen
	 */
	public double experience(int i, int action, double reward) {
		if (i == action) {
			return reward * (1 - e);
		} else {
			return reward * (e / (k - 1));
		}
	}

	/**
	 * Replace the current propensities with the supplied propensity array.
	 * 
	 * @param q
	 *          The new propensity array to use.
	 */
	public void setPropensities(double q[]) {
		this.q = q;
		updateProbabilities();
	}
	

	public void resetPropensities() {
		double initialPropensity = s1 / k;
		for (int i = 0; i < k; i++) {
			q[i] = initialPropensity;
			probabilities.setProbability(i, 1.0/k);
		}
	}


	public void setRecency(double r) {
		this.r = r;
		validateParams();
	}

	public void setExperimentation(double e) {
		this.e = e;
		validateParams();
	}

	public void setScaling(double s1) {
		this.s1 = s1;
	}

	private static int sign(double value) {
		return (new Double(value)).compareTo(new Double(0));
	}

	public void dumpState(DataWriter out) {
		for (int i = 0; i < k; i++) {
			out.newData(probabilities.getProbability(i));
		}
	}

	/**
	 * Get the total number of actions.
	 */
	public int getK() {
		return k;
	}
		
	/**
	 * Get the total number of actions
	 */
	public int getNumberOfActions() {
		return getK();
	}

	public double getLearningDelta() {
		return deltaP;
	}

	/**
	 * Get the probability of the ith action.
	 */
	public double getProbability(int i) {
		return probabilities.getProbability(i);
	}
	
	/**
	 * Get the probability distribution corresponding to the current
	 * propensities.
	 */
	public DiscreteProbabilityDistribution getProbabilities() {
		return probabilities;
	}
	
	public double getE() {
		return e;
	}

	public int getIteration() {
		return iteration;
	}

	public int getLastAction() {
		return lastAction;
	}

	public double getR() {
		return r;
	}

	public double getS1() {
		return s1;
	}

	public String toString() {
		return "(" + getClass() + " k:" + k + " r:" + r + " e:" + e + " s1:" + s1
		    + " learningDelta:" + deltaP + ")";
	}

	@Override
	public int bestAction() {
		double max = Double.NEGATIVE_INFINITY;
		int result = -1;
		for(int i=0; i<q.length; i++) {
			if (q[i] > max) {
				max = q[i];
				result = i;
			}
		}
		return result;
	}
	
	@Override
	public int worstAction() {
		double max = Double.POSITIVE_INFINITY;
		int result = -1;
		for(int i=0; i<q.length; i++) {
			if (q[i] < max) {
				max = q[i];
				result = i;
			}
		}
		return result;
	}


}
