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

import org.apache.log4j.Logger;

public class BasicAgentInitialiser implements AgentInitialiser, Serializable {

	static Logger logger = Logger.getLogger(BasicAgentInitialiser.class);
	
	public void initialise(Population population) {
		for (Agent agent : population.getAgents()) {
			
			if (logger.isDebugEnabled())
				logger.debug("Initialising agent: " + agent);
			
			agent.initialise();
			
			if (logger.isDebugEnabled())
				logger.debug("Finished initialising agent: " + agent);
		}
	}

}
