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
package net.sourceforge.jabm.strategy;

import java.util.List;

import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.event.EventListener;

/**
 * Classes implementing this interface define strategies for agents. A strategy
 * encapsulates the behaviour of the agent. Different agents of the same type
 * may be configured with heterogenous behaviours, and a single agent can switch
 * between several different behaviours during the course of a simulation. Since
 * the outcome of following a specific behaviour can depend on the actions of
 * other agents we use the term strategy from game-theory to refer to
 * behaviours.
 * 
 * @author Steve Phelps
 * 
 */
public interface Strategy extends EventListener, Cloneable {
	
	/**
	 * Configure the agent associated with this strategy.
	 */
	public void setAgent(Agent agent);
	
	public Agent getAgent();
	
	/**
	 * A Strategy should subscribe to any events it wants to receive by calling
	 * the {@link addListener} method in the {@link EventScheduler} class.
	 * 
	 * @param scheduler
	 *            The {@link EventScheduler} on which to listen.
	 */
	public void subscribeToEvents(EventScheduler scheduler);

	/**
	 * Execute the behaviour defined by this strategy.
	 * 
	 * @param otherAgents  The other agents with which the agent associated
	 *   						with this strategy is interacting. 
	 */
	public void execute(List<Agent> otherAgents);

	/**
	 * The strategy should call {@link EventScheduler.removeListener()}
	 * to unsubscribe from events when this method is called.  This hook is 
	 * used to clean-up, for example when the strategy is disposed of.
	 */
	public void unsubscribeFromEvents();
	
	public Object clone() throws CloneNotSupportedException;
	
}
