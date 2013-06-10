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

import java.util.ArrayList;
import java.util.Map;

import net.sourceforge.jabm.event.RoundFinishedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationFinishedEvent;
import net.sourceforge.jabm.event.SimulationStartingEvent;
import net.sourceforge.jabm.report.AbstractReportVariables;

import org.apache.log4j.Logger;

/**
 * The BarTender agent keeps track of current and historical attendance
 * at the bar, and writes the attendance time series data to a CSV
 * file at the end of each simulation.
 * 
 * @author Steve Phelps
 *
 */
public class BarTender extends AbstractReportVariables {

	protected int currentAttendance;
	
	protected ArrayList<Integer> historicalAttendance;
	
	public static final String NAME = "bartender";
	
	static Logger logger = Logger.getLogger(BarTender.class);
	
	public BarTender() {	
		super(NAME);
		yVariableNames.add(NAME +".attendance");
	}

	@Override
	public void eventOccurred(SimEvent event) {
		super.eventOccurred(event);
		if (event instanceof AttendedBarEvent) {
			onAttendedBar();
		} else if (event instanceof StayedAtHomeEvent) {
			onStayedAtHome();
		} else if (event instanceof RoundFinishedEvent) {
			onRoundFinished((RoundFinishedEvent) event);
		} else if (event instanceof SimulationFinishedEvent) {
			onSimulationFinished();
		} else if (event instanceof SimulationStartingEvent) {
			onSimulationStarting();
		}
	}
	
	public void onSimulationStarting() {
		this.historicalAttendance = new ArrayList<Integer>();
	}

	public void onSimulationFinished() {
	}

	public void onRoundFinished(RoundFinishedEvent event) {		
		int currentWeek = 
			(int) event.getSimulation().getSimulationTime().getTicks();
		// TODO
		this.timeStamp = currentWeek;
		historicalAttendance.add(currentWeek, currentAttendance);
		if (logger.isDebugEnabled()) {
			logger.debug("attendance in week " + currentWeek + " is "
					+ currentAttendance);
		}
		currentAttendance = 0;
	}

	public void onStayedAtHome() {
		// Do nothing
	}

	public void onAttendedBar() {
		currentAttendance++;
	}

	public ArrayList<Integer> getHistoricalAttendance() {
		return historicalAttendance;
	}

	public void setHistoricalAttendance(ArrayList<Integer> historicalAttendance) {
		this.historicalAttendance = historicalAttendance;
	}
	
	/**
	 * Get the attendance <code>lag</code> weeks ago relative to 
	 * the current time.
	 * 
	 * @param lag  The number of periods to go back. 
	 * @return  The total number of agents who attended the bar during
	 *           the specified period.
	 */
	public int getAttendanceAtLag(int lag) {
		assert lag > 0;
		int t = historicalAttendance.size();
		return getAttendance(t - lag);
	}

	/**
	 * Get attendance at the specified (absolute) time.
	 * @param t  The week number (the first week is 0).
	 * @return  The total number of agents who attended the bar during
	 *           the specified period.
	 */
	public int getAttendance(int t) {
		if (t < 0 || t >= historicalAttendance.size()) {
			return 0;
		}
		Integer attendance = historicalAttendance.get(t);
		if (attendance == null) {
			return 0;
		} else {
			return attendance;
		}
	}	
	
	@Override
	public String toString() {
		return "BarTender [currentAttendance=" + currentAttendance
				+ ", historicalAttendance=" + historicalAttendance + "]";
	}

	@Override
	public Map<Object, Number> getVariableBindings() {
		Map<Object, Number> result = super.getVariableBindings();
		result.put(NAME + ".attendance", currentAttendance);
		return result;
	}			
	
}