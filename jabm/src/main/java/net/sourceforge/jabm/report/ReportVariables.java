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

import net.sourceforge.jabm.event.SimEvent;

/**
 * <p>
 * This interface defines Report objects which keep track of variables which are
 * recomputed in response to various simulation events.
 * </p>
 * 
 * <p>
 * This interface allows the same metrics to be collected in different ways, for
 * example at different frequencies: we might collect data on the mean
 * population fitness at the end of each generation, at the end of each
 * simulation, or at some specified temporal frequency.
 * </p>
 * 
 * <p>
 * In this example, the computation of population fitness can be coded once in a
 * single implementation of ReportVariables, and then different Reports can be
 * created which update these variables at the required frequency or in response
 * to the required events.
 * </p>
 * 
 * @author Steve Phelps
 * 
 */
public interface ReportVariables extends Report {

	/**
	 * Update variables.
	 * @param event
	 */
	public void compute(SimEvent event);
	
	/**
	 * Clean up any side effects (eg close file).
	 * @param event
	 */
	public void dispose(SimEvent event);
	
	/**
	 * Initialise the variables in response to event.
	 * 
	 * @param event
	 */
	public void initialise(SimEvent event);
	
	public String getName();
	
}
