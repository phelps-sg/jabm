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

/**
 * A simulation in which agents repeatedly arrive at the simulation in 
 * multiple rounds.
 *  
 * @author Steve Phelps
 *
 */
public class RepeatedInteractionSimulation extends SimpleSimulation 
		implements Serializable {

	/**
	 * The current round a.k.a. tick.
	 */
	protected int round = 0;
	
	/**
	 * The maximum number of rounds.
	 */
	protected int maximumRounds = Integer.MAX_VALUE;
	
	public RepeatedInteractionSimulation(
			SimulationController simulationController) {
		super(simulationController);
	}
	
	public RepeatedInteractionSimulation() {
		super();
	}

	@Override
	public SimulationTime getSimulationTime() {
		return new SimulationTime(round);
	}

	@Override
	public void run() {
		begin();
		for(round = 0; round < maximumRounds && isRunning; round++) {
			step();
		}
		end();
	}

	public int getMaximumRounds() {
		return maximumRounds;
	}

	/**
	 * Configure the maximum number of rounds this simulation will run before
	 * being automatically terminated.
	 * 
	 * @param maximumRounds
	 */
	public void setMaximumRounds(int maximumRounds) {
		this.maximumRounds = maximumRounds;
	}
	
}
