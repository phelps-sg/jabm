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
package net.sourceforge.jabm.util;

import java.io.Serializable;

import cern.jet.random.AbstractContinousDistribution;

/**
 * A random variate that takes on the absolute value of values drawn
 * from an underlying probability distribution.
 * 
 * @author Steve Phelps
 */
public class AbsoluteContinuousDistribution extends
		AbstractContinousDistribution implements Serializable {

	protected AbstractContinousDistribution underlyingDistribution;
	
	public AbsoluteContinuousDistribution(
			AbstractContinousDistribution underlyingDistribution) {
		super();
		this.underlyingDistribution = underlyingDistribution;
	}
	
	public AbsoluteContinuousDistribution() {
		super();
	}

	@Override
	public double nextDouble() {
		return Math.abs(underlyingDistribution.nextDouble());
	}

	public AbstractContinousDistribution getUnderlyingDistribution() {
		return underlyingDistribution;
	}

	public void setUnderlyingDistribution(
			AbstractContinousDistribution underlyingDistribution) {
		this.underlyingDistribution = underlyingDistribution;
	}
	
	

}
