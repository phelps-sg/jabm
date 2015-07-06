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

import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.event.AgentArrivalEvent;
import net.sourceforge.jabm.event.RoundStartingEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationFinishedEvent;
import net.sourceforge.jabm.strategy.Strategy;

/**
 * An abstract superclass for JABM agents which provides default event-handling
 * functionality implementing several methods in the <code>Agent</code>
 * interface. The only requirement for an object to model a JABM agent is that
 * it implement the <code>Agent</code> interface; agents do not have to subclass
 * <code>AbstractAgent</code>.
 * 
 * @author Steve Phelps
 * 
 */
public abstract class AbstractAgent implements Serializable, Agent {

	protected boolean interacted;
	
	/**
	 * The event scheduler used to fire events originating from this agent.
	 */
	protected EventScheduler scheduler;
	
	protected Strategy strategy;
	
	public AbstractAgent(EventScheduler scheduler) {
		super();
		this.scheduler = scheduler;
		if (scheduler != null) {
			subscribeToEvents();
		}
	}

	public AbstractAgent() {
		super();
	}

	/**
	 * Subscribe to the specific events that we are interested in.
	 */
	public void subscribeToEvents() {
		scheduler.addListener(AgentArrivalEvent.class, this);
		scheduler.addListener(SimulationFinishedEvent.class, this);
		scheduler.addListener(RoundStartingEvent.class, this);
		Strategy strategy = getStrategy();
		if (strategy != null) {
			strategy.subscribeToEvents(scheduler);
		}
	}
	
	public void unsubscribeFromEvents() {
		scheduler.removeListener(this);
	}
	
	@Override
	public void eventOccurred(SimEvent event) {
		if (event instanceof AgentArrivalEvent) {
			AgentArrivalEvent arrivalEvent = (AgentArrivalEvent) event;
			if (arrivalEvent.getSubject() == this) {
				onAgentArrival((AgentArrivalEvent) event);
			}
		} else if (event instanceof SimulationFinishedEvent) {
			onSimulationFinished((SimulationFinishedEvent) event);
		} else if (event instanceof RoundStartingEvent) {
			onRoundStarting((RoundStartingEvent) event);
		}
	}
	
	public void onSimulationFinished(SimulationFinishedEvent event) {
		unsubscribeFromEvents();
	}
	
	public void onRoundStarting(RoundStartingEvent event) {
		interacted = false;
	}
	
	public void fireEvent(SimEvent event) {
		scheduler.fireEvent(event);
	}

	public EventScheduler getScheduler() {
		return scheduler;
	}

	@Override
	public void setScheduler(EventScheduler scheduler) {
		this.scheduler = scheduler;
		// Once we have a reference to the scheduler, subscribe
		//  to the events that our agent is interested in.
		subscribeToEvents();
	}
	
	public double getPayoffDelta() {
		return 0.0;
	}

	/**
	 * This method is called whenever an agent arrives at the 
	 * simulation.  The default behaviour is to execute the
	 * agent's strategy.
	 */
	public void onAgentArrival(AgentArrivalEvent event) {
		strategy.execute(event.getObjects());
		interacted = true;
	}
	
	@Override
	public boolean isInteracted() {
		return interacted;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	/**
	 * Configure the strategy for this agent.
	 */
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
		strategy.setAgent(this);
	}
	
	
}