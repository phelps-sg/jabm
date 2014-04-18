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

package net.sourceforge.jabm.prng;

import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

public class MT32 extends PRNGFactory {

	public RandomEngine create() {
		return new MersenneTwister();
	}

	public RandomEngine create(long seed) {
		return new MersenneTwister((int) seed);
	}

	public String getDescription() {
		return "32-bit Mersenne Twister (Matsumoto and Nishimura)";
	}

}
