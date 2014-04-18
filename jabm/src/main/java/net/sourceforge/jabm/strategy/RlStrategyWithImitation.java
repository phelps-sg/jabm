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
package net.sourceforge.jabm.strategy;

import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.learning.StimuliResponseLearner;

import org.springframework.beans.factory.ObjectFactory;

import cern.jet.random.engine.RandomEngine;

public class RlStrategyWithImitation extends RlStrategy 
		implements ImitableStrategy, ImitatingStrategy {

	protected RandomEngine prng;
	
	protected ObjectFactory<Strategy> mutationFactory;
	
	public RlStrategyWithImitation(Agent agent, 
			ObjectFactory<Strategy> strategyFactory,
			StimuliResponseLearner learner) {
		super(agent, strategyFactory, learner);
	}
	
	public RlStrategyWithImitation(
			ObjectFactory<Strategy> strategyFactory,
			StimuliResponseLearner learner) {
		super(strategyFactory, learner);
	}

	public void imitate(Agent otherAgent) {
		if (otherAgent.getStrategy() instanceof ImitableStrategy) {
			ImitableStrategy otherStrategy = 
				(ImitableStrategy) otherAgent.getStrategy();
			Strategy strategyToImitiate = 
				(Strategy) otherStrategy.createMimicStrategy();
			strategyToImitiate.setAgent(this.getAgent());
			int worst = this.learner.worstAction();
			disposeOfAction(worst);
			this.actions[worst] = strategyToImitiate;
		}
	}

	@Override
	public Strategy createMimicStrategy() {
		try {
			int best = this.learner.bestAction();
			Strategy bestStrategy = this.actions[best];
			if (logger.isDebugEnabled()) {
				logger.debug("Someone else is copying my best strategy = " + bestStrategy);
				logger.debug("learner = " + learner);
			}
			return (Strategy) bestStrategy.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public RandomEngine getPrng() {
		return prng;
	}

	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}
	
	public void mutate() {
//		int action = (int) Math.round(prng.nextDouble() * (this.actions.length - 1));
		int action = learner.worstAction();
		Strategy newStrategy = mutationFactory.getObject();
		newStrategy.setAgent(this.getAgent());
		disposeOfAction(action);
		actions[action] = newStrategy;
	}
	
	
	public void disposeOfAction(int action) {
		actions[action].unsubscribeFromEvents();
		actions[action].setAgent(null);
	}

	public ObjectFactory<Strategy> getMutationFactory() {
		return mutationFactory;
	}

	public void setMutationFactory(ObjectFactory<Strategy> mutationFactory) {
		this.mutationFactory = mutationFactory;
	}
	
	
}
