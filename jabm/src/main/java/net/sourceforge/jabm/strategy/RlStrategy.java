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
import net.sourceforge.jabm.learning.StatelessQLearner;
import net.sourceforge.jabm.learning.StimuliResponseLearner;
import net.sourceforge.jabm.report.Taggable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

public class RlStrategy extends AbstractRlStrategy 
		implements Serializable, InitializingBean, Taggable {

	protected StimuliResponseLearner learner;
	
	static Logger logger = Logger.getLogger(RlStrategy.class);
	
	public RlStrategy(Agent agent, 
							ObjectFactory<Strategy> strategyFactory, 
							StimuliResponseLearner learner) {
		super(agent);
		this.learner = learner;
		this.strategyFactory = strategyFactory;
		initialise();
	}
	
	public RlStrategy(ObjectFactory<Strategy> strategyFactory,
			StimuliResponseLearner learner) {
		this(null, strategyFactory, learner);
	}
	
	public RlStrategy() {
	}

	public void initialise() {
		int numActions = learner.getNumberOfActions();
		actions = new Strategy[numActions];
		for(int i=0; i<numActions; i++) {
			Strategy strategy = strategyFactory.getObject();
			strategy.setAgent(agent);
			actions[i] = strategy;
		}
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
		if (agent.isInteracted()) {
			double reward = agent.getPayoffDelta();
			learner.reward(reward);
		}
		int action = learner.act();
		currentStrategy = actions[action];
		assert currentStrategy.getAgent() != null;
		currentStrategy.execute(otherAgents);		
	}

	public StimuliResponseLearner getLearner() {
		return learner;
	}

	@Required
	public void setLearner(StimuliResponseLearner learner) {
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

	public void setInitialPropensities(double[] initialPropensities) {
		StatelessQLearner qLearner = (StatelessQLearner) this.learner;
		double[] propensities = qLearner.getqLearner().getValueEstimates(0);
		System.arraycopy(initialPropensities, 0, propensities, 0, actions.length);
	}

	public int getNumberOfActions() {
		return learner.getNumberOfActions();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
//		initialise();
	}

	@Override
	public void setTag(String tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTag() {
		if (currentStrategy != null && currentStrategy instanceof Taggable) {
			return "RL: " + ((Taggable) currentStrategy).getTag();
		} else {
			return this.getClass().toString();
		}
	}
	
}
