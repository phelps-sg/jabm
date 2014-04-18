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
 * A queue with fixed length, which can be useful when tracking a sliding window
 * on a data series
 * </p>
 * 
 * @author Jinzhong Niu
 * @version $Revision: 16 $
 */

public class FixedLengthQueue implements Resetable {

	protected double list[];

	protected int curIndex;

	protected double sum;

	protected int count;

	public FixedLengthQueue(int length) {
		assert (length >= 0);
		list = new double[length];
		initialize();
	}

	public void initialize() {
		for (int i = 0; i < list.length; i++) {
			list[i] = 0;
		}
		curIndex = 0;
		sum = 0;
		count = 0;
	}

	public void reset() {
		initialize();
	}

	public void newData(double value) {
		sum -= list[curIndex];
		list[curIndex] = value;
		sum += value;

		curIndex++;
		curIndex %= list.length;

		if (count < list.length) {
			count++;
		}
	}

	public int count() {
		return count;
	}

	public double getMean() {
		return sum / count();
	}

	public String toString() {
		String s = "[";
		int start;
		if (count() < list.length) {
			start = 0;
		} else {
			start = curIndex;
		}

		for (int i = 0; i < count; i++) {
			s += list[(start + i) % list.length];
			if (i < count - 1) {
				s += ", ";
			} else {
				s += "]";
			}
		}
		return s;
	}
}
