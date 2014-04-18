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

/**
 * Classes implementing this interface define agent-based simulation models.
 * 
 * @author Steve Phelps
 *
 */
public interface Simulation extends Runnable {

	/**
	 * Fetch the simulation controller for this simulation.
	 */
	public SimulationController getSimulationController();

	/**
	 * Query the current simulation time.
	 */
	public SimulationTime getSimulationTime();
	
	/**
	 * Fetch the Population of agents for this simulation.
	 */
	public Population getPopulation();
	
	/**
	 * Pause the simulation.
	 */
	public void pause();
	
	/**
	 * Resume the simulation after pausing.
	 */
	public void resume();
	
	/**
	 * Terminate the simulation.
	 */
	public void terminate();
	
	/**
	 * Slow down the simulation. 
	 * 
	 * @param slowSleepInterval The number of ms to sleep between ticks.
	 */
	public void slow(int slowSleepInterval);
	
}
