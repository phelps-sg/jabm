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
 * An agent initialiser which reconstructs the population from scratch before
 * each experiment using the population's agent factory.
 * 
 * @author sphelps
 *
 */
public class GardenOfEdenAgentInitialiser implements AgentInitialiser {

	@Override
	public void initialise(Population population) {
		population.reset();
	}

}
