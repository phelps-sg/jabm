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
import net.sourceforge.jabm.event.SimulationFinishedEvent;

import org.apache.log4j.Logger;

/**
 * A report which initialises its variables at the start of each simulation
 * and updates them at the end of each simulation.
 * 
 * @author Steve Phelps
 *
 */
public class SimulationFinishedNoInitialisation extends AbstractReport implements Report {

	static Logger logger = Logger.getLogger(SimulationFinishedNoInitialisation.class);

	@Override
	public void eventOccurred(SimEvent event) {
		super.eventOccurred(event);
		if (event instanceof SimulationFinishedEvent) {
			reportVariables.compute((SimulationFinishedEvent) event);
		} 
	}

}
