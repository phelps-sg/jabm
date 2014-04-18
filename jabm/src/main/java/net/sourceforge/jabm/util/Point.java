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

public class Point implements Serializable, Comparable<Point> {

	protected double coordinates[];

	public Point(double[] coordinates) {
		super();
		this.coordinates = coordinates;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			for(int i=0; i<coordinates.length; i++) {
				if (!MathUtil.approxEqual(this.coordinates[i], ((Point) obj).coordinates[i], 1e-3)) {
					return false;
				} 
			}
			return true;
		} else {
			return false;
		}
	}

	public int hashCode() {
		float sum = 0;
		for(int i=0; i<coordinates.length; i++) {
			sum += coordinates[i] * Math.pow(10, i);
		}
		return Math.round(sum);
	}
	
	public String toString() {
		StringBuffer result = new StringBuffer("(");
		for(int i=0; i<coordinates.length; i++) {
			result.append(coordinates[i] + "");
			if (i<coordinates.length-1) {
				result.append(",");
			}
		}
		result.append(")");
		return result.toString();
	}
	
	public double[] getCoordinates() {
		return coordinates;
	}

	public int compareTo(Point o) {
		Point other = (Point) o;
		if (this.equals(other)) {
			return 0;
		} else {
			if (this.hashCode() < other.hashCode()) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
