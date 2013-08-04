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
package net.sourceforge.jabm.event;

import java.io.Serializable;
import java.util.ArrayList;

import net.sourceforge.jabm.SimulationController;
import net.sourceforge.jabm.agent.Agent;

/**
 * An event that is fired whenever an agent arrives at the simulation.
 * Typically, but not always, this will be at the start of a simulation tick.
 * 
 * @author Steve Phelps
 * 
 */
public class AgentArrivalEvent extends SimulationControllerEvent implements
		Serializable {

	/**
	 * The main agent that has arrived at the simulation.
	 */
	protected Agent subject;

	/**
	 * An optional list of agents that have arrived at the same time as the main
	 * agent. This is used to implement mixing models in which, for example,
	 * agents are randomly paired with one another in some interaction.
	 */
	protected ArrayList<Agent> objects;

	public AgentArrivalEvent(SimulationController controller, Agent subject,
			ArrayList<Agent> objects) {
		super(controller);
		this.subject = subject;
		this.objects = objects;
	}

	public Agent getSubject() {
		return subject;
	}

	public void setSubject(Agent subject) {
		this.subject = subject;
	}

	public ArrayList<Agent> getObjects() {
		return objects;
	}

	public void setObjects(ArrayList<Agent> objects) {
		this.objects = objects;
	}
	
}
