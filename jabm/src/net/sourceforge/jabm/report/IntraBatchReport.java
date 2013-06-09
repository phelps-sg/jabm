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

import net.sourceforge.jabm.event.BatchFinishedEvent;
import net.sourceforge.jabm.event.BatchStartingEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationFinishedEvent;

/**
 * <p>
 * A report which collects data across simulations and updates
 * report variables at the end of each simulation.
 * </p>
 * 
 * @author Steve Phelps
 *
 */
public class IntraBatchReport extends AbstractReport {

	@Override
	public void eventOccurred(SimEvent event) {
		super.eventOccurred(event);
		if (event instanceof BatchStartingEvent) {
			reportVariables.initialise((BatchStartingEvent) event);
		}
		if (event instanceof SimulationFinishedEvent) {
			reportVariables.compute((SimulationFinishedEvent) event);
		}
		if (event instanceof BatchFinishedEvent) {
			reportVariables.dispose(event);
		}
	}
}
