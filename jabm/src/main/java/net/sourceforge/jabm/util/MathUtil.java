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
 * Miscalleneous mathematical functions.
 * 
 * @author Steve Phelps
 * @version $Revision: 16 $
 */

public class MathUtil {

	public static final double DEFAULT_ERROR = 0.0000001;

	/**
	 * Calculate the square of x.
	 */
	public static double squared(double x) {
		return x * x;
	}

	/**
	 * Calculate the squared difference of x and y.
	 */
	public static double diffSq(double x, double y) {
		return squared(x - y);
	}

	/**
	 * Returns true if the difference between x and y is less than error.
	 */
	public static boolean approxEqual(double x, double y, double error) {
		return Math.abs(x - y) <= error;
	}

	public static boolean approxEqual(double x, double y) {
		return MathUtil.approxEqual(x, y, DEFAULT_ERROR);
	}

	public static double sum(double[] series) {
		double total = 0;
		for (int i = 0; i < series.length; i++) {
			total += series[i];
		}
		return total;
	}

	/**
	 * Use cern.jet.random.Arithmetic.factorial instead.
	 * 
	 * @deprecated
	 * 
	 */
	public static long factorial(int n) {
		long result = 1;
		for (int i = 0; i < n - 1; i++) {
			result *= n - i;
		}
		return result;
	}

}