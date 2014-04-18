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
 * Polymorphic version of <code>java.lang.Double</code>.
 * </p>
 * 
 * @author Steve Phelps
 * @version $Revision: 16 $
 * 
 */

public class UntypedDouble extends UntypedNumber implements Serializable {

	double primitiveValue;

	public UntypedDouble() {
		this(Double.NaN);
	}

	public UntypedDouble(Double value) {
		this(value.doubleValue());
	}

	public UntypedDouble(double value) {
		primitiveValue = value;
	}

	public UntypedNumber add(UntypedNumber other) {
		return new UntypedDouble(primitiveValue + other.doubleValue());
	}

	public UntypedNumber multiply(UntypedNumber other) {
		return new UntypedDouble(primitiveValue * other.doubleValue());
	}

	public UntypedNumber subtract(UntypedNumber other) {
		return new UntypedDouble(primitiveValue - other.doubleValue());
	}

	public UntypedNumber divide(UntypedNumber other) {
		return new UntypedDouble(primitiveValue / other.doubleValue());
	}

	public int intValue() {
		return (int) primitiveValue;
	}

	public float floatValue() {
		return (float) primitiveValue;
	}

	public double doubleValue() {
		return primitiveValue;
	}

	public long longValue() {
		return (long) primitiveValue;
	}

	public int compareTo(Object other) {
		if (other instanceof Number) {
			double d1 = ((Number) other).doubleValue();
			double d0 = doubleValue();
			if (d0 > d1) {
				return +1;
			} else if (d0 < d1) {
				return -1;
			} else {
				return 0;
			}
		} else {
			throw new ClassCastException();
		}
	}

	public boolean equals(Object other) {
		if (other instanceof UntypedNumber) {
			return doubleValue() == ((UntypedNumber) other).doubleValue();
		} else {
			return super.equals(other);
		}
	}

	public String toString() {
		return primitiveValue + "";
	}

	protected void setValue(double value) {
		primitiveValue = value;
	}

}
