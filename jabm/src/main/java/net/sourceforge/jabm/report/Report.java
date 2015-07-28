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

import java.util.Map;

import net.sourceforge.jabm.event.EventListener;

/**
 * <p>
 * Objects implementing the <code>Report</code> interface provide reporting
 * functionality by collecting data on simulations. They persist across
 * different <code>Simulation</code> runs and are typically declared as
 * <code>singleton</code> in scope when configured via <a
 * href="http://www.springsource.com/developer/spring">Spring</a>. This allows
 * them to collect summary statistics across different simulation runs.
 * </p>
 * 
 * @see net.sourceforge.jabm.report.ReportVariables
 * 
 * @author Steve Phelps
 * 
 */
public interface Report extends EventListener {

	/**
	 * Get the values calculated by this report.
	 * 
	 * @return A {@link Map} of user-readable variable names to their
	 *         associated values.
	 */
	public Map<Object, Number> getVariableBindings();

	public String getName();
	
}
