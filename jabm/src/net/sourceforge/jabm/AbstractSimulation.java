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

import net.sourceforge.jabm.event.EventListener;
import net.sourceforge.jabm.event.InteractionsFinishedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.init.AgentInitialiser;
import net.sourceforge.jabm.mixing.AgentMixer;

import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractSimulation implements Simulation, Serializable {

	protected SimulationController simulationController;
	
	protected AgentInitialiser agentInitialiser;
	
	protected AgentMixer agentMixer;
	
	protected Population population;
	
	protected boolean isRunning = true;
	
	protected boolean isPaused = false;
	
	/**
	 * Interval in milliseconds to wait in between
	 * steps if the simulation is to be slowed down.
	 * A value of zero indicates full speed.
	 */
	protected int slowSleepInterval = 0;

	public static final int PAUSE_SLEEP_INTERVAL_MS = 100;


	public AbstractSimulation(SimulationController simulationController) {
		super();
		this.simulationController = simulationController;
	}

	public AbstractSimulation(SimulationController simulationController,
			Population population) {
		super();
		this.simulationController = simulationController;
		this.population = population;
	}
	
	public AbstractSimulation() {
		super();
	}

	public SimulationController getSimulationController() {
		return simulationController;
	}

	@Required
	public void setSimulationController(SimulationController simulationController) {
		this.simulationController = simulationController;
	}

	public void fireEvent(SimEvent event) {
		simulationController.fireEvent(event);
	}

	public void addListener(EventListener listener) {
		simulationController.addListener(listener);
	}

	public void initialiseAgents() {
		agentInitialiser.initialise(population);
	}
	
//	public void reproduce() {
//		population.reproduce();
//	}
	
	/**
	 * Schedule events of type AgentArrivalEvent for each
	 * agent in the simulation according to the AgentMixer in
	 * use by the SimulationController.
	 */
	public void invokeAgentInteractions() {
		agentMixer.invokeAgentInteractions(population, simulationController);	
		fireEvent(new InteractionsFinishedEvent(this));
	}

	public AgentInitialiser getAgentInitialiser() {
		return agentInitialiser;
	}

	@Required
	public void setAgentInitialiser(AgentInitialiser agentInitialiser) {
		this.agentInitialiser = agentInitialiser;
	}

	public Population getPopulation() {
		return population;
	}

	@Required
	public void setPopulation(Population population) {
		this.population = population;
	}

	public AgentMixer getAgentMixer() {
		return agentMixer;
	}

	/**
	 * Configure the mixing policy which specifies how agents interact
	 * each other.
	 * 
	 * @see AgentMixer
	 * @param agentMixer
	 */
	public void setAgentMixer(AgentMixer agentMixer) {
		this.agentMixer = agentMixer;
	}
	
	public void pause() {
		this.isPaused = true;
	}
	
	public void resume() {
		this.isPaused = false;
	}
	
	public void terminate() {
		this.isRunning = false;
	}
	
	public void waitIfPaused() {
		while (isPaused) {
			// Wait
			try {
				Thread.sleep(PAUSE_SLEEP_INTERVAL_MS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public void waitIfSlowed() {
		if (slowSleepInterval > 0) {
			try {
				Thread.sleep(slowSleepInterval);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public void step() {
		waitIfPaused();
		waitIfSlowed();
	}

	public int getSlowSleepInterval() {
		return slowSleepInterval;
	}

	public void setSlowSleepInterval(int slowSleepInterval) {
		this.slowSleepInterval = slowSleepInterval;
	}
	
	public void slow(int slowSleepInterval) {
		setSlowSleepInterval(slowSleepInterval);
	}
	
}
