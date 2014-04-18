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
package net.sourceforge.jabm.report;

import net.sourceforge.jabm.event.InteractionsFinishedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationFinishedEvent;
import net.sourceforge.jabm.event.SimulationStartingEvent;

/**
 * <p>
 * A report which samples data regularly in response to
 * an InteractionsFinishedEvent.  The data to be sampled
 * is configured by specifying an instance of <code>ReportVariables</code>.
 * </p>
 *  
 * @see ReportVariables
 * @author Steve Phelps
 *
 */
public class InteractionIntervalReport extends AbstractReport {

	protected int interactions;
	
	protected int sampleInterval;
	
	public static final int DEFAULT_SAMPLE_INTERVAL = 1;

	public InteractionIntervalReport(int sampleInterval,
			ReportVariables reportVariables) {
		super(reportVariables);
		this.sampleInterval = sampleInterval;
	}

	public InteractionIntervalReport() {
		this(DEFAULT_SAMPLE_INTERVAL, null);
	}
	
	public InteractionIntervalReport(int sampleInterval) {
		this(sampleInterval, null);
	}
	
	public InteractionIntervalReport(ReportVariables reportVariables) {
		this(DEFAULT_SAMPLE_INTERVAL, reportVariables);
	}

	public void eventOccurred(SimEvent event) {
		super.eventOccurred(event);
		if (event instanceof SimulationStartingEvent) {
			onSimulationStarting((SimulationStartingEvent) event);
		}
		if (event instanceof InteractionsFinishedEvent) {
			onInteractionsFinished((InteractionsFinishedEvent) event);
		} 
		if (event instanceof SimulationFinishedEvent) {
			onSimulationFinished(event);
		}
	}
	
	public void onSimulationFinished(SimEvent event) {
		reportVariables.dispose(event);
	}

	public void onSimulationStarting(SimulationStartingEvent event) {
		interactions = 0;
		reportVariables.initialise(event);
	}

	public void onInteractionsFinished(InteractionsFinishedEvent event) {
		interactions++;
		if ((interactions % sampleInterval ) ==0) {
			reportVariables.compute(event);
		}
	}
	
	public int getSampleInterval() {
		return sampleInterval;
	}

	/**
	 * Configure the sampling interval for this report.  The underlying
	 * ReportVariables will be updated at the corresponding frequency.
	 * 
	 * @param sampleInterval
	 */
	public void setSampleInterval(int sampleInterval) {
		this.sampleInterval = sampleInterval;
	}
	
}