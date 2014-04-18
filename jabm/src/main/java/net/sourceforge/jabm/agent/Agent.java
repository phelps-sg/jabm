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

import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.event.EventListener;
import net.sourceforge.jabm.strategy.Strategy;

/**
 * A simulated agent. Every agent has a strategy, which specifies the behaviour
 * of the agent.
 * 
 * @author sphelps
 */
public interface Agent extends EventListener {

	/**
	 * @return the accrued payoff to the agent during the course of the simulation.
	 */
	public double getPayoff();
	
	/**
	 * @return the change in payoff during the most recent period.
	 */
	public double getPayoffDelta();
	
	/**
	 * Initialise the agent.
	 */
	public void initialise();
	
	/**
	 * Find out whether the agent has interacted with the environment or other
	 * agents.
	 */
	public boolean isInteracted();

	/**
	 * Set the event scheduler for this agent.
	 */
	public void setScheduler(EventScheduler eventScheduler);
	
	/**
	 * @return the current <code>Strategy</code> used by this agent.
	 */
	public Strategy getStrategy();

	/**
	 * Configure the current <code>Strategy</code> for this agent.
	 */
	public void setStrategy(Strategy strategy);
	
}
