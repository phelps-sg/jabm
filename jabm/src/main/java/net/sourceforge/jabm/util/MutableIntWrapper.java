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

/**
 * <p>
 * A simple wrapper for a primitive <code>int</code> value which is publically
 * mutable.
 * </p>
 */
public class MutableIntWrapper {

	public int value;

	public MutableIntWrapper() {
		this(0);
	}

	public MutableIntWrapper(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}