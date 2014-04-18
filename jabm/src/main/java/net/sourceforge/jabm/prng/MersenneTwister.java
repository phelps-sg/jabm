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
package net.sourceforge.jabm.prng;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

import cern.jet.random.engine.MersenneTwister64;

public class MersenneTwister extends MersenneTwister64 implements Serializable {

	protected int seed;
	
	static Logger logger = Logger.getLogger(MersenneTwister.class);
	
	public MersenneTwister() {
		this(new Date());
	}
	
	public MersenneTwister(Date date) {
		super(date);
	}

	public MersenneTwister(int seed) {
		super(seed);
	}

	@Override
	public void setSeed(int seed) {
		super.setSeed(seed);
		this.seed = seed;
		logger.debug("seed = " + seed);
	}
	
	public int getSeed() {
		return this.seed;
	}
	
	
	
}
