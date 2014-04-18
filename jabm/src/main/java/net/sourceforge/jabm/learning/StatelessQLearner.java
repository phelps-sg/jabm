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

import net.sourceforge.jabm.util.Prototypeable;
import net.sourceforge.jabm.util.Resetable;

import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.engine.RandomEngine;

/**
 * A memory-less version of the Q-Learning algorithm.
 * 
 * This class implements StimuliResponseLearner instead of MDPLearner, and so
 * can be used in place of, e.g. a RothErevLearner.
 * 
 * We use the standard MDP QLearner class, but fool it with this wrapper into
 * thinking that there is only one state.
 * 
 * @author Steve Phelps
 * @version $Revision: 189 $
 */
public class StatelessQLearner extends AbstractLearner implements
		StimuliResponseLearner, Resetable, Serializable,
		Prototypeable {

	QLearner qLearner;

	public StatelessQLearner(RandomEngine prng) {
		qLearner = new QLearner(prng);
	}

	public StatelessQLearner(int numActions, 
			double learningRate, double discountRate, RandomEngine prng) {

		qLearner = new QLearner(1, numActions, learningRate,
				discountRate, prng);
	}
	
	public StatelessQLearner(int numActions, RandomEngine prng) {
		this(numActions,QLearner.DEFAULT_LEARNING_RATE, QLearner.DEFAULT_DISCOUNT_RATE, prng);
	}


	public int act() {
		return qLearner.act();
	}

	public int bestAction() {
		return qLearner.bestAction(0);
	}
	
	public int worstAction() {
		return qLearner.worstAction(0);
	}

	public double getDiscountRate() {
		return qLearner.getDiscountRate();
	}

	public int getLastActionChosen() {
		return qLearner.getLastActionChosen();
	}

	public double getLearningRate() {
		return qLearner.getLearningRate();
	}

	public int getNumActions() {
		return qLearner.getNumberOfActions();
	}

	public int getPreviousState() {
		return qLearner.getPreviousState();
	}

	public RandomEngine getPrng() {
		return qLearner.getPrng();
	}

	public int getState() {
		return qLearner.getState();
	}

	public void initialise() {
		qLearner.initialise();
	}

	public double maxQ(int newState) {
		return qLearner.maxQ(newState);
	}

	public void setDiscountRate(double discountRate) {
		qLearner.setDiscountRate(discountRate);
	}

	public void setLearningRate(double learningRate) {
		qLearner.setLearningRate(learningRate);
	}
	
	public String toString() {
		return qLearner.toString();
	}

	public void reward(double reward) {
		qLearner.newState(reward, 0);
	}

	public void reset() {
		qLearner.reset();
	}

	public double getLearningDelta() {
		return qLearner.getLearningDelta();
	}

	public int getNumberOfActions() {
		return qLearner.getNumberOfActions();
	}

	public void setNumberOfActions(int n) {
		qLearner.setStatesAndActions(1, n);
	}
	
	public QLearner getqLearner() {
		return qLearner;
	}

	public void setqLearner(QLearner qLearner) {
		this.qLearner = qLearner;
	}

	public ActionSelector getActionSelector() {
		return qLearner.getActionSelector();
	}

	@Required
	public void setActionSelector(ActionSelector actionSelector) {
		qLearner.setActionSelector(actionSelector);
	}

	public void dumpState(net.sourceforge.jabm.report.DataWriter out) {
		qLearner.dumpState(out);
	}

	public Object protoClone() {
		try {
			StatelessQLearner cloned = (StatelessQLearner) this.clone();
			cloned.qLearner = (QLearner) this.qLearner.protoClone();
			return cloned;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	
	public void setInitialQValue(double initialQ) {
		qLearner.setInitialQValue(initialQ);
	}
	
	public double getInitialQValue() {
		return qLearner.getInitialQValue();
	}

}