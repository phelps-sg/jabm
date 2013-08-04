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
package net.sourceforge.jabm.evolution;

import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.strategy.Strategy;

import org.apache.log4j.Logger;

public class StrategyImitationOperator implements ImitationOperator {

	static Logger logger = Logger.getLogger(StrategyImitationOperator.class);
	
	@Override
	public void inheritBehaviour(Agent child, Agent parent) {
		try {
			if (logger.isDebugEnabled()) logger.debug("Reproducing " + parent);
			child.setStrategy((Strategy) parent.getStrategy().clone());
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

}
