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
package net.sourceforge.jabm.report;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationStartingEvent;
import net.sourceforge.jabm.event.StrategyExecutedEvent;
import net.sourceforge.jabm.strategy.Strategy;

import org.apache.commons.math3.stat.Frequency;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * <p>
 * This class collects data on the frequency with which each strategy is
 * executed.
 * </p>
 * 
 * @see net.sourceforge.jabm.strategy.Strategy
 * @author Steve Phelps
 * 
 */
public class StrategyExecutionFrequency extends AbstractReportVariables
		implements InitializingBean {

	protected Frequency executionFrequency = new Frequency();
	
//	protected Frequency reportedFrequency = new Frequency();
	
	protected List<String> keyList;
	
	/**
	 * If agent is non-null then only track strategies executed by
	 * the specified agent.
	 */
	protected Agent agent = null;
	
	protected Population population;
	
	protected int agentIndex = -1;
	
	public static final String NAME = "frequency";
	
	
	static Logger logger = Logger.getLogger(StrategyExecutionFrequency.class);
	
	public StrategyExecutionFrequency() {
		super(NAME);
	}

	@Override
	@SuppressWarnings (value="rawtypes")
	public Map<Object, Number> getVariableBindings() {
		Map<Object, Number> result = super.getVariableBindings();
		Iterator i = executionFrequency.valuesIterator();
		if (keyList != null) {
			i = keyList.iterator();
		}
		while (i.hasNext()) {
			String tag = (String) i.next();
			result.put(NAME + "." + tag, getPercentage(tag));
		}
		return result;
	}

	public double getPercentage(String tag) {
		double result = executionFrequency.getPct(tag);
		if (Double.isNaN(result)) {
			result = 0.0;
		} 
		return result;
	}

	@Override
	public void eventOccurred(SimEvent event) {
		super.eventOccurred(event);
		if (event instanceof StrategyExecutedEvent) {
			onStrategyExecuted((StrategyExecutedEvent) event);
		} else if (event instanceof SimulationStartingEvent) {
			onSimulationStarting((SimulationStartingEvent) event);
		}
	}
	
	public void onSimulationStarting(SimulationStartingEvent event) {
		if (agentIndex >= 0) {
			Population population = event.getSimulation().getPopulation();
			this.agent =
				population.getAgentList().get(agentIndex);
		}
	}
	
	public void onStrategyExecuted(StrategyExecutedEvent event) {
		Strategy strategy = event.getStrategy();
		if ((this.agent == null) || (strategy.getAgent() == this.agent)) {
			String tag = tag(event.getStrategy());
			executionFrequency.addValue(tag);	
		}
	}
	
	@Override
	public void initialise(SimEvent event) {
		logger.debug("Reseting on " + event);
		super.initialise(event);
		executionFrequency = new Frequency();
	}

	@Override
	public void compute(SimEvent event) {
//		reportedFrequency = executionFrequency;
//		executionFrequency = new Frequency();
		super.compute(event);
	}

	@SuppressWarnings (value="rawtypes")
	public List getKeyList() {
		return keyList;
	}

	/**
	 * Configure the list of the strategies which this report will track.
	 * 
	 * @param keySetObjects
	 *            The list of strategies which will be tracked. These can be
	 *            object factories, object instances, or strings denoting the
	 *            tag of a strategy.
	 */
	@Required
	@SuppressWarnings (value="rawtypes")
	public void setKeyList(List keySetObjects) {
		this.keyList = new LinkedList<String>();
		for (Object object : keySetObjects) {
			if (object instanceof ObjectFactory) {
				object = ((ObjectFactory) object).getObject();
			}
			this.keyList.add(tag(object));
		}
	}

	public int getAgentIndex() {
		return agentIndex;
	}

	/**
	 * The index of a particular agent within the population whose
	 *  strategy executions we want to track.
	 *  
	 * @param agentIndex
	 */
	public void setAgentIndex(int agentIndex) {
		this.agentIndex = agentIndex;
	}

	@Override
	public int getNumberOfSeries() {
		return keyList.size();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for(String key : keyList) {
			yVariableNames.add(NAME + "." + key);
		}
	}
	
}
