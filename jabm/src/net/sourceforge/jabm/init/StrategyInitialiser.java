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
package net.sourceforge.jabm.init;

import java.io.Serializable;

import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.strategy.AbstractStrategy;
import net.sourceforge.jabm.strategy.Strategy;

import org.springframework.beans.factory.ObjectFactory;

public class StrategyInitialiser implements AgentInitialiser, Serializable {

	protected transient ObjectFactory<Strategy> strategyFactory;
	// TODO

	public StrategyInitialiser(ObjectFactory<Strategy> strategyFactory) {
		super();
		this.strategyFactory = strategyFactory;
	}
	
	public StrategyInitialiser() {
	}

	public void initialise(Population population) {
		for (Agent agent : population.getAgents()) {
			AbstractStrategy strategy = (AbstractStrategy) strategyFactory
					.getObject();
			agent.setStrategy(strategy);
			strategy.setAgent(agent);
		}
	}

	public ObjectFactory<Strategy> getStrategyFactory() {
		return strategyFactory;
	}

	public void setStrategyFactory(ObjectFactory<Strategy> strategyFactory) {
		this.strategyFactory = strategyFactory;
	}

}
