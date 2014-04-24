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
package net.sourceforge.jabm.agent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.ObjectFactory;

import cern.jet.random.engine.RandomEngine;

/**
 * A list of agents.
 * 
 * @author Steve Phelps
 */
public class AgentList implements Serializable {

	/**
	 * The agents comprising this list.
	 */
	protected List<Agent> agents;
	
	protected int size;

	protected ObjectFactory<Agent> agentFactory;
	
	public static final Comparator<Agent> ascendingFitnessComparator = 
		new Comparator<Agent>() {

		public int compare(Agent o1, Agent o2) {
			if (o1.getPayoff() > o2.getPayoff()) {
				return 1;
			} else if (o1.getPayoff() < o2.getPayoff()) {
				return -1;
			} else {
				return 0;
			}
		}
		
	};
	
	public static final Comparator<Agent> descendingFitnessComparator = 
		new Comparator<Agent>() {

		public int compare(Agent o1, Agent o2) {
			if (o1.getPayoff() > o2.getPayoff()) {
				return -1;
			} else if (o1.getPayoff() < o2.getPayoff()) {
				return +1;
			} else {
				return 0;
			}
		}
		
	};
	
	/**
	 * Create an empty AgentList with the specified capacity.
	 * @param size
	 */
	public AgentList(int size) {
		this.size = size;
		agents = new ArrayList<Agent>(size);
	}
	
	/**
	 * Create an empty AgentList.
	 */
	public AgentList() {
		agents = new ArrayList<Agent>();
	}
	
	/**
	 * Create a list comprising a single agent.
	 * @param agent
	 */
	public AgentList(Agent agent) {
		agents = new ArrayList<Agent>(1);
		agents.add(agent);
		this.size = 1;
	}
	
	/**
	 * Create an AgentList from the supplied Collection of agents.
	 * @param agents
	 */
	public AgentList(Collection<Agent> agents) {
		this.agents = new ArrayList<Agent>(agents);
	}
	
	/**
	 * Create an AgentList by merging a list of AgentList objects.
	 * @param lists  An ArrayList holding an array of AgentList objects.
	 */
	public AgentList(ArrayList<AgentList> lists) {
		agents = new ArrayList<Agent>();
		for (int i=0; i<lists.size(); i++) {
			AgentList l = lists.get(i);
			addAll(l.getAgents());
		}
		this.size = agents.size();
	}
	
	/**
	 * Create an AgentList by manufacturing objects from the supplied
	 * factory.
	 * @param size  The number of agents to manufacture.
	 * @param agentFactory  The factory used to manufacture agents.
	 */
	public AgentList(int size, ObjectFactory<Agent> agentFactory) {
		this.size = size;
		this.agentFactory = agentFactory;
		populateFromFactory();
	}
	
	public AgentList(AgentList existing) {
		this.size  = existing.size;
		this.agentFactory = existing.agentFactory;
		this.agents = new ArrayList<Agent>(size);
		this.agents.addAll(existing.agents);
	}

	public void populateFromFactory() {
		agents.clear();
		for (int i = 0; i < size; i++) {
			add(agentFactory.getObject());
		}
	}

//	@Override
//	public void afterPropertiesSet() {
//		populateFromFactory();
//	}
	
//	public void setAgents(ArrayList lists) {
//		agents = new ArrayList<Agent>();
//		for (int i=0; i<lists.size(); i++) {
//			AgentList l = (AgentList) lists.get(i);
//			addAll(l.getAgents());
//		}
//	}
	
	public Agent get(int i) {		
		return agents.get(i);
	}
	
	public void set(int i, Agent agent) {
		agents.set(i, agent);
	}
	
	public void add(Agent agent) {
		agents.add(agent);
	}	
	
	public boolean addAll(Collection<? extends Agent> c) {
		return agents.addAll(c);
	}

	public Agent remove(int index) {
		return agents.remove(index);
	}

	public List<Agent> getAgents() {
		return agents;
	}
	
	public Iterator<Agent> iterator() {
		return agents.iterator();
	}
	
	public int size() {
		return this.agents.size();
	}

	public void setAgents(List<Agent> agents) {
		this.agents = agents;
	}

	public double getTotalFitness() {
		float result = 0f;
		for(Agent agent : agents) {
			result += agent.getPayoff();
		}
		return result;
	}
	
	public double getMaxFitness() {
		double result = Float.NEGATIVE_INFINITY;
		for(Agent agent : agents) {
			if (agent.getPayoff() > result) {
				result = agent.getPayoff();
			}
		}
		return result;
	}
	
	/**
	 * Randomly sort the agents in this list.
	 * @param prng
	 */
	public void shuffle(final RandomEngine prng) {
		int n = agents.size();
		for (int i = 0; i < n - 1; i++) {			
			int choice = (int) ((long) n * prng.raw());
			Agent tmp = agents.get(i);
			agents.set(i, agents.get(choice));
			agents.set(choice, tmp);
		}
	}
	
	public void sortAgentsByFitness() {
		sortAgents(ascendingFitnessComparator);
	}
	
	public void sortAgents(Comparator<Agent> comparator) {
		Collections.sort(agents, comparator);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
//		populateFromFactory();
	}

	public ObjectFactory<Agent> getAgentFactory() {
		return agentFactory;
	}

	public void setAgentFactory(ObjectFactory<Agent> agentFactory) {
		this.agentFactory = agentFactory;
	}

	
}
