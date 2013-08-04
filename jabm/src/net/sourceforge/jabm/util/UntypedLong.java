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
 * Faster version of <code>java.lang.Long</code>.
 * </p>
 * 
 * @author Steve Phelps
 * @version $Revision: 16 $
 * 
 */

public class UntypedLong extends UntypedNumber implements Serializable {

	long primitiveValue;

	public UntypedLong() {
		this(0L);
	}

	public UntypedLong(Long value) {
		this(value.longValue());
	}

	public UntypedLong(long value) {
		primitiveValue = value;
	}

	public UntypedNumber add(UntypedNumber other) {
		if (other instanceof UntypedLong) {
			return new UntypedLong(primitiveValue + other.longValue());
		} else if (other instanceof UntypedDouble) {
			return new UntypedDouble(doubleValue() + other.doubleValue());
		} else {
			throw new IllegalArgumentException();
		}
	}

	public UntypedNumber multiply(UntypedNumber other) {
		if (other instanceof UntypedLong) {
			return new UntypedLong(primitiveValue * other.longValue());
		} else if (other instanceof UntypedDouble) {
			return new UntypedDouble(doubleValue() * other.doubleValue());
		} else {
			throw new IllegalArgumentException();
		}
	}

	public UntypedNumber subtract(UntypedNumber other) {
		if (other instanceof UntypedLong) {
			return new UntypedLong(primitiveValue - other.longValue());
		} else if (other instanceof UntypedDouble) {
			return new UntypedDouble(doubleValue() - other.doubleValue());
		} else {
			throw new IllegalArgumentException();
		}
	}

	public UntypedNumber divide(UntypedNumber other) {
		return opResult(doubleValue() / other.doubleValue());
	}

	protected UntypedNumber opResult(double tempResult) {
		long intResult = Math.round(tempResult);
		if (intResult == tempResult) {
			return new UntypedLong(intResult);
		} else {
			return new UntypedDouble(tempResult);
		}
	}

	public int compareTo(Object other) {
		if (other instanceof UntypedLong) {
			long l0 = primitiveValue;
			long l1 = ((UntypedLong) other).longValue();
			if (l0 < l1) {
				return -1;
			} else if (l0 > l1) {
				return +1;
			} else {
				return 0;
			}
		} else if (other instanceof UntypedDouble) {
			double d0 = doubleValue();
			double d1 = ((UntypedDouble) other).doubleValue();
			if (d0 < d1) {
				return -1;
			} else if (d0 > d1) {
				return +1;
			} else {
				return 0;
			}
		} else {
			throw new ClassCastException("");
		}
	}

	public int intValue() {
		return (int) primitiveValue;
	}

	public float floatValue() {
		return (float) primitiveValue;
	}

	public double doubleValue() {
		return (double) primitiveValue;
	}

	public long longValue() {
		return primitiveValue;
	}

	public String toString() {
		return primitiveValue + "";
	}

	public boolean equals(Object other) {
		if (other instanceof UntypedLong) {
			return primitiveValue == ((UntypedLong) other).longValue();
		} else if (other instanceof UntypedNumber) {
			return doubleValue() == ((UntypedNumber) other).doubleValue();
		} else {
			return super.equals(other);
		}
	}

}
