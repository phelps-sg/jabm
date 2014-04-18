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
package net.sourceforge.jabm;

import java.io.Serializable;

public class SimulationTime implements Serializable, 
		Comparable<SimulationTime> {

	protected long ticks;
	
	public SimulationTime(long ticks) {
		super();
		this.ticks = ticks;
	}

	@Override
	public int compareTo(SimulationTime other) {
		if (this.ticks > other.ticks) {
			return 1;
		} else if (this.ticks < other.ticks) {
			return -1;
		} else {
			return 0;
		}
	}

	public long getTicks() {
		return ticks;
	}

	@Override
	public String toString() {
		return ticks + "";
	}

}
