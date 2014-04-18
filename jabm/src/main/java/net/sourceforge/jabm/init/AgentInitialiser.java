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


import net.sourceforge.jabm.Population;

/**
 * Classes implementing this interface define agent initialisation policies.
 *
 * @author Steve Phelps
 */
public interface AgentInitialiser {

	/**
	 * Initialise a population of agents.  The initial state of any
	 * given agent may depend on the current state of other agents in
	 * the collection.
	 */
	public void initialise(Population population);
	
}
