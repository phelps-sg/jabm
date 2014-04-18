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

import java.io.Serializable;

import net.sourceforge.jabm.report.DataWriter;

/**
 * @author Steve Phelps
 * @version $Revision: 189 $
 */
public abstract class AbstractLearner implements Learner,
		Serializable {

	protected LearnerMonitor monitor = null;

	public AbstractLearner() {
	}

	public void monitor() {
		if (monitor != null) {
			monitor.startRecording();
			dumpState(monitor);
			monitor.finishRecording();
		}
	}

	public abstract double getLearningDelta();

	public abstract void dumpState(DataWriter out);

}