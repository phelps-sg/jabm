/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2013 Steve Phelps
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package net.sourceforge.jabm.learning;

import net.sourceforge.jabm.report.DataWriter;

/**
 * Classes implementing this interface indicate that they implement a learning
 * algorithm.
 * 
 * @author Steve Phelps
 * @version $Revision: 16 $
 */

public interface Learner {

	/**
	 * Return a value indicative of the amount of learning that occured during the
	 * last iteration. Values close to 0.0 indicate that the learner has converged
	 * to an equilibrium state.
	 * 
	 * @return A double representing the amount of learning that occured.
	 */
	public double getLearningDelta();

	/**
	 * Write out our state data to the specified data writer.
	 */
	public void dumpState(DataWriter out);

	/**
	 * A hook to provide monitoring functionality. Implementations of learning
	 * algorithms should either log, or provide a visualisation of, their state in
	 * response to this method.
	 */
	public void monitor();

}