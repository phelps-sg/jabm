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
import net.sourceforge.jabm.util.Resetable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

/**
 * <p>
 * An implementation of the Q-learning algorithm. This algorithm is described in
 * Watkins, J. C. H., Dayan, P., 1992. Q-learning. Machine Learning 8, 279-292.
 * </p>
 * 
 * @author Steve Phelps
 * @version $Revision: 189 $
 */

public class QLearner extends AbstractLearner implements MDPLearner, Resetable,
    	InitializingBean, Serializable, Prototypeable {

	/**
	 * The number of possible states
	 */
	protected int numStates;

	/**
	 * The number of possible actions
	 */
	protected int numActions;

	/**
	 * The matrix representing the estimated payoff of each possible action in
	 * each possible state.
	 */
	protected double q[][];

	/**
	 * The learning rate.
	 */
	protected double learningRate;

	/**
	 * The discount rate for future payoffs.
	 */
	protected double discountRate;


	/**
	 * The previous state
	 */
	protected int previousState;

	/**
	 * The current state
	 */
	protected int currentState;

	/**
	 * The last action that was chosen.
	 */
	protected int lastActionChosen;

	/**
	 * The best action for the current state
	 */
	protected int bestAction;
	
	protected RandomEngine prng;
	
	protected ActionSelector actionSelector;

	protected double initialQValue;

	static final double DEFAULT_LEARNING_RATE = 0.5;

	static final double DEFAULT_DISCOUNT_RATE = 0.8;

	static Logger logger = Logger.getLogger(QLearner.class);

	public QLearner(int numStates, int numActions,
	    double learningRate, double discountRate, RandomEngine prng) {
		setStatesAndActions(numStates, numActions);
		this.learningRate = learningRate;
		this.discountRate = discountRate;
		this.prng = prng;
		this.actionSelector = new EpsilonGreedyActionSelector(prng);
		initialise();
	}

	public QLearner(RandomEngine prng) {
		this(0, 0, DEFAULT_LEARNING_RATE, DEFAULT_DISCOUNT_RATE, prng);
	}
	
	public QLearner() {
		this(null);
	}

	public Object protoClone() {
		try {
			QLearner cloned = (QLearner) clone();
			return cloned;
		} catch (CloneNotSupportedException e) {
			logger.error(e.getMessage());
			throw new Error(e);
		}
	}

	public void initialise() {
		q = new double[numStates][numActions];
		for (int s = 0; s < numStates; s++) {
			for (int a = 0; a < numActions; a++) {
				q[s][a] = initialQValue;
			}
		}
		currentState = 0;
		previousState = 0;
		bestAction = 0;
		lastActionChosen = 0;
	}

	public void setStatesAndActions(int numStates, int numActions) {
		this.numStates = numStates;
		this.numActions = numActions;
		initialise();
	}

//	public void setup(ParameterDatabase parameters, Parameter base) {
//
//		super.setup(parameters, base);
//
//		learningRate = parameters.getDoubleWithDefault(base.push(P_LEARNING_RATE),
//		    null, DEFAULT_LEARNING_RATE);
//
//		discountRate = parameters.getDoubleWithDefault(base.push(P_DISCOUNT_RATE),
//		    null, DEFAULT_DISCOUNT_RATE);
//
//		epsilon = parameters.getDoubleWithDefault(base.push(P_EPSILON), null,
//		    DEFAULT_EPSILON);
//
//		numStates = parameters.getInt(base.push(P_NUM_STATES), null);
//
//		numActions = parameters.getInt(base.push(P_NUM_ACTIONS), null);
//
//		setStatesAndActions(numStates, numActions);
//	}

	public void setState(int newState) {
		previousState = currentState;
		currentState = newState;
	}

	public int getState() {
		return currentState;
	}

	public int act() {
		this.lastActionChosen = actionSelector.act(currentState, this);
		return lastActionChosen;
	}

	public void newState(double reward, int newState) {
		updateQ(reward, newState);
		setState(newState);
	}

	protected void updateQ(double reward, int newState) {
		q[currentState][lastActionChosen] = learningRate
		    * (reward + discountRate * maxQ(newState)) + (1 - learningRate)
		    * q[currentState][lastActionChosen];
	}

	public double maxQ(int newState) {
		Uniform dist = new Uniform(0, numActions-1, prng);
		bestAction = dist.nextInt();
		double max = q[newState][bestAction];
		for (int a = 0; a < numActions; a++) {
			if (q[newState][a] > max) {
				max = q[newState][a];
				bestAction = a;
			}
		}
		return max;
	}
	
	public int worstAction(int state) {
		int result = 0;
		double min = Double.POSITIVE_INFINITY;
		for (int a = 0; a < numActions; a++) {
			if (q[state][a] > min) {
				min = q[state][a];
			}
		}
		return result;
	}

	public int bestAction(int state) {
		maxQ(state);
		return bestAction;
	}

	public void reset() {
		initialise();
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	public int getLastActionChosen() {
		return lastActionChosen;
	}

	public double getLearningDelta() {
		return 0; // TODO
	}

	public void dumpState(DataWriter out) {
		// TODO
	}

	public int getNumberOfActions() {
		return numActions;
	}

	
	public double getLearningRate() {
		return learningRate;
	}
	
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public int getNumberOfStates() {
		return numStates;
	}
	
	public void setNumberOfStates(int numStates) {
		this.numStates = numStates;
	}

	public void setNumberOfActions(int numActions) {
		this.numActions = numActions;
	}

	public int getPreviousState() {
		return previousState;
	}
	
	public RandomEngine getPrng() {
		return prng;
	}

	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

	public ActionSelector getActionSelector() {
		return actionSelector;
	}

	public void setActionSelector(ActionSelector actionSelector) {
		this.actionSelector = actionSelector;
	}

	public String toString() {
		return "(" + getClass() + " lastActionChosen:" + lastActionChosen
		    + " actionSelector:" + actionSelector + " learningRate:" + learningRate
		    + " discountRate:" + discountRate + ")";
	}

	public double getValueEstimate(int action) {
		return q[this.currentState][action];
	}

	public void setInitialQValue(double initialQValue) {
		this.initialQValue = initialQValue;
	}
	
	public double getInitialQValue() {
		return this.initialQValue;
	}

	@Override
	public double[] getValueEstimates(int state) {
		return this.q[state];
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initialise();
	}

}