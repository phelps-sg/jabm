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

package net.sourceforge.jabm.util;

import java.io.Serializable;

/**
 * <p>
 * A simple wrapper for a primitive <code>double</code> value which is
 * publicly mutable.
 * </p>
 */
public class MutableDoubleWrapper extends Number implements Serializable {

	public double value;

	public MutableDoubleWrapper() {
		this(0);
	}

	public MutableDoubleWrapper(double value) {
		this.value = value;
	}

	public double doubleValue() {
		return value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public int intValue() {
		return (int) Math.round(value);
	}

	@Override
	public long longValue() {
		return Math.round(value);
	}

	@Override
	public float floatValue() {
		return (float) value;
	}

	
}
