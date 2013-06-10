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
package net.sourceforge.jabm.examples.elfarolbar;

import java.io.Serializable;

import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.agent.AbstractAgent;
import net.sourceforge.jabm.event.AgentArrivalEvent;
import net.sourceforge.jabm.event.SimEvent;

/**
 * An agent representing a patron at the El Farol Bar.
 * 
 * @author Steve Phelps
 */
public class PatronAgent extends AbstractAgent implements Serializable {

	/**
	 * The attendance level at which this agent feels overcrowded. 
	 */
	protected int barCapacity;
	
	public PatronAgent(EventScheduler scheduler) {
		super(scheduler);
	}
	
	public PatronAgent() {
		super();
	}

	@Override
	public void onAgentArrival(AgentArrivalEvent event) {
		super.onAgentArrival(event);
		AbstractPredictionStrategy predictionStrategy = 
			(AbstractPredictionStrategy) getStrategy();
		double predictedAttendance = predictionStrategy.getCurrentPrediction();
		if (Math.round(predictedAttendance) < barCapacity) {
			attendBar();
		} else {
			stayAtHome();
		}
	}

	public void stayAtHome() {
		fireEvent(new StayedAtHomeEvent(this));
	}

	public void attendBar() {
		fireEvent(new AttendedBarEvent(this));
	}

	@Override
	public double getPayoff() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void initialise() {
		// Default is do nothing.
	}


	public int getBarCapacity() {
		return barCapacity;
	}

	public void setBarCapacity(int barCapacity) {
		this.barCapacity = barCapacity;
	}

	@Override
	public void eventOccurred(SimEvent event) {
		super.eventOccurred(event);
	}

	@Override
	public void subscribeToEvents() {
		super.subscribeToEvents();
	}

	@Override
	public String toString() {
		return "ElFarolBarAgent [strategy=" + strategy
				+ ", barCapacity=" + barCapacity + "]";
	}
	
}
