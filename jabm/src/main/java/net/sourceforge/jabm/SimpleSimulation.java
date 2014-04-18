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
package net.sourceforge.jabm;

import java.io.Serializable;

import net.sourceforge.jabm.event.RoundFinishedEvent;
import net.sourceforge.jabm.event.RoundStartingEvent;
import net.sourceforge.jabm.event.SimulationFinishedEvent;
import net.sourceforge.jabm.event.SimulationStartingEvent;

public class SimpleSimulation extends AbstractSimulation implements
		Serializable {

	public SimpleSimulation(SimulationController simulationController) {
		super(simulationController);
	}

	public SimpleSimulation() {
		super();
	}
	
	public void begin() {
		initialiseAgents();
		fireEvent(new SimulationStartingEvent(this));
	}
	
	public void end() {
		fireEvent(new SimulationFinishedEvent(this));
	}

	public void run() {
		begin();
		step();
		end();
	}
	
	public void step() {
		super.step();
		fireEvent(new RoundStartingEvent(this));
		invokeAgentInteractions();
		fireEvent(new RoundFinishedEvent(this));	
	}

	@Override
	public SimulationTime getSimulationTime() {
		return simulationController.getSimulationTime();
	}

}
