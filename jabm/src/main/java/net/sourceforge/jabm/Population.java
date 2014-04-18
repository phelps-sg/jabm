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
import java.util.Collection;

import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.agent.AgentList;
import net.sourceforge.jabm.event.EventListener;
import net.sourceforge.jabm.event.SimEvent;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

/**
 * A population of agents in a simulation.  The population can be resized 
 * dynamically.  Each time the population is resized the agents in the 
 * population will be constructed via the specified <code>ObjectFactory</code>.
 * 
 * @author Steve Phelps
 * 
 */
public class Population implements EventListener, Serializable {

	/**
	 * The list of agents comprising this population.
	 */
	protected AgentList agentList;

	protected RandomEngine prng;
	
//	protected transient ObjectFactory<Agent> agentFactory;
	// TODO
	
//	protected int size;

	public Population() {
		agentList = new AgentList();
	}

	public Population(int size, RandomEngine prng, AgentList agentList) {
		this.prng = prng;
//		this.agentFactory = agentFactory;
		this.agentList = agentList;
		reset();
	}
//
//	public Population(RandomEngine prng, ObjectFactory<Agent> agentFactory) {
//		this(0, prng, agentFactory);
//	}

	public Population(Collection<Agent> agents, RandomEngine prng) {
		this(new AgentList(agents), prng);
	}

	public Population(AgentList agentList, RandomEngine prng) {
		this.agentList = agentList;
		this.prng = prng;
	}

	public Population(RandomEngine prng) {
		this(new AgentList(), prng);
	}

	public void reset() {
//		agentList = new AgentList(size);
//		for (int i = 0; i < size; i++) {
//			Agent agent = agentFactory.getObject();
//			agentList.add(agent);
//		}
		agentList.populateFromFactory();
	}

	public int size() {
		return agentList.getSize();
	}

	public void setSize(int size) {
//		this.size = size;
		agentList.setSize(size);
//		reset();
	}

	public int getSize() {
		return size();
	}

	public Collection<Agent> getAgents() {
		return agentList.getAgents();
	}

	public AgentList getAgentList() {
		return agentList;
	}

	public void setAgentList(AgentList agentList) {
		this.agentList = agentList;
//		this.size = agentList.size();
	}

	public Agent getRandomAgent() {
		Uniform distribution = new Uniform(0, agentList.size() - 1, prng);
		return agentList.get(distribution.nextInt());
	}

	public void add(Agent agent) {
		agentList.add(agent);
	}

	public RandomEngine getPrng() {
		return prng;
	}

	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

	@Override
	public void eventOccurred(SimEvent event) {
	}
//
//	public ObjectFactory<Agent> getAgentFactory() {
//		return this.agentFactory;
//	}
//	
//	public void setAgentFactory(ObjectFactory<Agent> agentFactory) {
//		this.agentFactory = agentFactory;
//	}

}
