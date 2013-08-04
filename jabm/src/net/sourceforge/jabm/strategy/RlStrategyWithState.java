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

import java.io.Serializable;
import java.util.List;

import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.learning.MDPLearner;
import net.sourceforge.jabm.learning.StatelessQLearner;
import net.sourceforge.jabm.report.Taggable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class RlStrategyWithState extends AbstractRlStrategy 
		implements Serializable, InitializingBean, Taggable {

	protected MDPLearner learner;
	
	static Logger logger = Logger.getLogger(RlStrategyWithState.class);
	
	public RlStrategyWithState(Agent agent, 
							ObjectFactory<Strategy> strategyFactory, 
							MDPLearner learner) {
		super(agent);
		this.learner = learner;
		this.strategyFactory = strategyFactory;
		initialise();
	}
	
	public RlStrategyWithState(ObjectFactory<Strategy> strategyFactory,
			MDPLearner learner) {
		this(null, strategyFactory, learner);
	}
	
	public RlStrategyWithState() {
	}
	
	
	@Override
	public void subscribeToEvents(EventScheduler scheduler) {
		super.subscribeToEvents(scheduler);
		for(int i=0; i<actions.length; i++) {
			actions[i].subscribeToEvents(scheduler);
		}
//		scheduler.addListener(SimulationFinishedEvent.class, this);
//		scheduler.addListener(InteractionsFinishedEvent.class, this);
	}

	public void execute(List<Agent> otherAgents) {		
		assert this.agent != null;
		double reward = agent.getPayoffDelta();
		int state = getState();
		learner.newState(reward, state);
		int action = learner.act();
		currentStrategy = actions[action];
		assert currentStrategy.getAgent() != null;
		currentStrategy.execute(otherAgents);		
	}

	public MDPLearner getLearner() {
		return learner;
	}

	@Required
	public void setLearner(MDPLearner learner) {
		this.learner = learner;
		initialise();
	}

	@Override
	public void setAgent(Agent agent) {
		super.setAgent(agent);
		for(int i=0; i<actions.length; i++) {
			actions[i].setAgent(agent);
		}
	}

	@Override
	public Strategy clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
//
//	@Override
//	public void eventOccurred(SimEvent event) {
//		super.eventOccurred(event);	
//		if (event instanceof InteractionsFinishedEvent) {
//			onInteractionsFinished();
//		}
//	}
	
//	public void onInteractionsFinished() {
//		double reward = agent.getPayoffDelta();
//		learner.reward(reward);
//	}

	@Override
	public void unsubscribeFromEvents() {
		for(int i=0; i<actions.length; i++) {
			actions[i].unsubscribeFromEvents();
		}
		super.unsubscribeFromEvents();
	}

	public ObjectFactory<Strategy> getStrategyFactory() {
		return strategyFactory;
	}

	@Required
	public void setStrategyFactory(ObjectFactory<Strategy> strategyFactory) {
		this.strategyFactory = strategyFactory;
	}

	public double[] getInitialPropensities() {
		return initialPropensities;
	}

	public void setInitialPropensities(double[] initialPropensities) {
		StatelessQLearner qLearner = (StatelessQLearner) this.learner;
		double[] propensities = qLearner.getqLearner().getValueEstimates(0);
		for(int i=0; i<actions.length; i++) {
			propensities[i] = initialPropensities[i];
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
//		initialise();
	}
	
	@Override
	public String getTag() {
		if (currentStrategy != null && currentStrategy instanceof Taggable) {
			return "SRL: " + ((Taggable) currentStrategy).getTag();
		} else {
			return this.getClass().toString();
		}
	}

	@Override
	public void setTag(String tag) {
	}
	
	public int getNumberOfActions() {
		return learner.getNumberOfActions();
	}

	public abstract int getState();
	
}
