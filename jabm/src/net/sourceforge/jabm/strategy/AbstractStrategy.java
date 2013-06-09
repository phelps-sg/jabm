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

import java.io.Serializable;
import java.util.List;

import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationFinishedEvent;
import net.sourceforge.jabm.event.StrategyExecutedEvent;

import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractStrategy implements
		Strategy, Cloneable, Serializable {

	protected Agent agent;

	protected EventScheduler scheduler;
	
	public AbstractStrategy() {
		this(null);
	}
	
	public AbstractStrategy(Agent agent) {
		super();
		this.agent = agent;
	}
	
	public AbstractStrategy(EventScheduler scheduler, Agent agent) {
		this(agent);
		this.scheduler = scheduler;
		subscribeToEvents();
	}
	
	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	@Override
	public void subscribeToEvents(EventScheduler scheduler) {
		this.scheduler = scheduler;
		subscribeToEvents();
	}
	
	public void subscribeToEvents() {
		scheduler.addListener(SimulationFinishedEvent.class, this);
	}
	
	@Required
	public void setScheduler(EventScheduler scheduler) {
		this.scheduler = scheduler;
		subscribeToEvents(scheduler);
	}
	
	public EventScheduler getScheduler() {
		return scheduler;
	}
	
	public void unsubscribeFromEvents() {
		scheduler.removeListener(this);
	}
	
	@Override
	public void eventOccurred(SimEvent event) {
		if (event instanceof SimulationFinishedEvent) {
			onSimulationFinished();
		} 
	}
	
	public void onSimulationFinished() {
		unsubscribeFromEvents();
		setAgent(null);
	}

	@Override
	public void execute(List<Agent> otherAgents) {
		fireEvent(new StrategyExecutedEvent(this));
	}
	
	public void fireEvent(SimEvent event) {
		scheduler.fireEvent(event);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
