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
import java.util.Iterator;

/**
 * <p>
 * An iterator that enumerates the base N representation of every non-negative
 * integer that can be represented within the specified number of digits.
 * </p>
 * 
 * @author Steve Phelps
 * @version $Revision: 104 $
 */

public class BaseNIterator implements Iterator<int[]>, Serializable {

	protected int currentNumber = 0;

	protected int base;

	protected int numDigits;

	protected int maximumNumber;

	public BaseNIterator(int base, int numDigits) {
		this.base = base;
		this.numDigits = numDigits;
		maximumNumber = ((int) Math.pow(base, numDigits)) - 1;
	}

	public int[] next() {
		int[] digits = convert();
		currentNumber++;
		return digits;
	}

	public boolean hasNext() {
		return currentNumber <= maximumNumber;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	protected int[] convert() {
		int n = currentNumber;
		int[] digits = new int[numDigits];
		for (int i = 0; i < numDigits; i++) {
			digits[i] = n % base;
			n /= base;
		}
		return digits;
	}

}