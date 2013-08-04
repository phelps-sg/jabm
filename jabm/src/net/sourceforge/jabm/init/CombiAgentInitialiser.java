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
import java.util.ArrayList;

import net.sourceforge.jabm.Population;

/**
 * An AgentInitialiser which combines a list of other AgentInitialisers. Each
 * sub-initialiser is executed in turn (the ordering being specified by ordering
 * of the supplied list); when this class's 
 * 
 * @author Steve Phelps
 * 
 */
public class CombiAgentInitialiser implements AgentInitialiser, Serializable {

	protected ArrayList<AgentInitialiser> initialisers;
	
	/**
	 * Create a new agent initialiser by combining the effect
	 * of the supplied list of initialisers.
	 * @param initialisers
	 */
	public CombiAgentInitialiser(ArrayList<AgentInitialiser> initialisers) {
		super();
		this.initialisers = initialisers;
	}
	
	public CombiAgentInitialiser() {
		this(new ArrayList<AgentInitialiser>());
	}

	public void initialise(Population population) {
		for(AgentInitialiser initialiser : initialisers) {
			initialiser.initialise(population);
		}
	}

	public ArrayList<AgentInitialiser> getInitialisers() {
		return initialisers;
	}

	public void setInitialisers(ArrayList<AgentInitialiser> initialisers) {
		this.initialisers = initialisers;
	}
	
	public void setInitialiser(AgentInitialiser initialiser) {
		initialisers = new ArrayList<AgentInitialiser>(2);
		initialisers.add(0, new BasicAgentInitialiser());
		initialisers.add(1, initialiser);
	}
	
	public AgentInitialiser getInitialiser() {
		return initialisers.get(1);
	}
}
