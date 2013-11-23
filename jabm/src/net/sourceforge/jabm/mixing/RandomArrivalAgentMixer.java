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
package net.sourceforge.jabm.mixing;

import java.io.Serializable;
import java.util.ArrayList;

import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.SimulationController;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.agent.AgentList;
import net.sourceforge.jabm.event.AgentArrivalEvent;
import net.sourceforge.jabm.event.SimEvent;

import org.apache.log4j.Logger;

import cern.jet.random.engine.RandomEngine;

/**
 * With this mixer, on any given time step an agent arrival event is generated
 * with a given probability. The agent is picked uniformly at random from the
 * group.
 * 
 * @author Steve Phelps
 */
public class RandomArrivalAgentMixer implements AgentMixer, Serializable {

	protected RandomEngine prng;

	/**
	 * The probability with which an agent arrives at the simulation on any
	 * given tick.
	 */
	protected double arrivalProbability = 1.0;

	public static final ArrayList<Agent> EMPTY_LIST = new ArrayList<Agent>(0);

	static Logger logger = Logger.getLogger(RandomArrivalAgentMixer.class);

	public RandomArrivalAgentMixer(RandomEngine prng) {
		this.prng = prng;
	}

	public RandomArrivalAgentMixer() {
		this(null);
	}

	public void invokeInteractions(AgentList group,
			SimulationController simulation) {
		if (group.size() > 0 && prng.nextDouble() <= arrivalProbability) {
			int n = group.size() - 1;
			int agentIndex = (int) Math.round(prng.nextDouble() * n);
			Agent agent = group.get(agentIndex);
			fireAgentArrivalEvent(agent, group, simulation);
		}
	}

	public void fireAgentArrivalEvent(Agent agent, AgentList group,
			SimulationController simulation) {
		simulation.fireEvent(new AgentArrivalEvent(simulation, agent,
				EMPTY_LIST));
	}

	public void invokeAgentInteractions(Population population,
			SimulationController simulation) {
		invokeInteractions(population.getAgentList(), simulation);
	}

	public void eventOccurred(SimEvent event) {
	}

	public RandomEngine getPrng() {
		return prng;
	}

	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

	public double getArrivalProbability() {
		return arrivalProbability;
	}

	/**
	 * Configure the probability with which an agent will arrive in any given
	 * tick.
	 */
	public void setArrivalProbability(double arrivalProbability) {
		this.arrivalProbability = arrivalProbability;
	}

}
