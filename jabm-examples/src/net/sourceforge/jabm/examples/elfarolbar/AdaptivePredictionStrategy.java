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
package net.sourceforge.jabm.examples.elfarolbar;

import java.util.List;

import net.sourceforge.jabm.agent.Agent;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

/**
 * A prediction strategy which adaptively chooses amongst a set of other
 * prediction strategies by choosing the one with the minimum
 * forecasting error at any given time.
 * 
 * @author Steve Phelps
 */
public class AdaptivePredictionStrategy extends AbstractPredictionStrategy 
		implements InitializingBean {

	protected AbstractPredictionStrategy[] rules;
	
	protected int numRules;
	
	protected ObjectFactory<AbstractPredictionStrategy> ruleFactory;
	
	protected AbstractPredictionStrategy currentBestPredictor;
	
	/**
	 * The pseudo-random number generator used to tie-break rules with
	 * the same forecast error.
	 */
	protected RandomEngine prng;

	public AdaptivePredictionStrategy(int numRules,
			ObjectFactory<AbstractPredictionStrategy> ruleFactory) {
		this.numRules = numRules;
		this.ruleFactory = ruleFactory;
		afterPropertiesSet();
	}
	
	public AdaptivePredictionStrategy() {
		super();
	}
	
	public int getNumRules() {
		return numRules;
	}

	public void setNumRules(int numRules) {
		this.numRules = numRules;
	}

	public ObjectFactory<AbstractPredictionStrategy> getRuleFactory() {
		return ruleFactory;
	}

	public void setRuleFactory(ObjectFactory<AbstractPredictionStrategy> ruleFactory) {
		this.ruleFactory = ruleFactory;
	}
	
	public void execute(List<Agent> otherAgents) {
		super.execute(otherAgents);
		if (logger.isDebugEnabled()) {
			logger.debug("currentPrediction = " + currentPrediction);
			logger.debug("Executing best predictor: " + currentBestPredictor);
		}
		currentBestPredictor.execute(otherAgents);
	}
	
	public void makePrediction() {
		findBestPredictor();
		currentPrediction = currentBestPredictor.getCurrentPrediction();
	}
	
	public AbstractPredictionStrategy findBestPredictor() {
		Uniform dist = new Uniform(0, rules.length-1, prng);
		currentBestPredictor = rules[dist.nextInt()];
		for(int i=0; i<rules.length; i++) {
			if (rules[i].getForecastError()  
					< currentBestPredictor.getForecastError()) {
				currentBestPredictor = rules[i];
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("currentBestPredictor = " + currentBestPredictor);
		}
		return currentBestPredictor;
	}

	@Override
	public void setAgent(Agent agent) {
		super.setAgent(agent);
		for(int i=0; i < rules.length; i++) {
			rules[i].setAgent(agent);
		}
	}

	public RandomEngine getPrng() {
		return prng;
	}

	@Required
	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

//	@Override
//	public void eventOccurred(SimEvent event) {
//		super.eventOccurred(event);
//		for(int i=0; i < rules.length; i++) {
//			rules[i].eventOccurred(event);
//		}
//	}

	@Override
	public void afterPropertiesSet() {
		rules = new AbstractPredictionStrategy[numRules];
		for (int i = 0; i < numRules; i++) {
			rules[i] = ruleFactory.getObject();
		}
	}

	public AbstractPredictionStrategy[] getRules() {
		return rules;
	}

	public void setRules(AbstractPredictionStrategy[] rules) {
		this.rules = rules;
	}

	public AbstractPredictionStrategy getCurrentBestPredictor() {
		return currentBestPredictor;
	}
	
	
}
