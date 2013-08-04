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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

/**
 * A class that iterates over all numerical partitions of n into k distinct
 * parts including commutative duplications and parts containing zero.
 * 
 * @author Steve Phelps
 * @version $Revision: 16 $
 */

public class Partitioner implements Iterator<int[]>, Serializable {

	protected Stack<PartitionerState> stack;

	protected HashSet<PartitionerState> visitedStates;

	public Partitioner(int n, int k) {
		stack = new Stack<PartitionerState>();
		visitedStates = new HashSet<PartitionerState>();
		stack.push(new PartitionerState(new int[k], n));
	}

	public boolean hasNext() {
		return !stack.isEmpty();
	}

	public int[] next() {
		return partition();
	}

	public void remove() {
		throw 
		new IllegalArgumentException("method remove() not implemented");
	}

	protected int[] partition() {
		while (!stack.isEmpty()) {
			PartitionerState state = stack.pop();
			if (state.n == 0) {
				return state.p;
			}
			int[] p = state.p;
			for (int i = 0; i < p.length; i++) {
				int[] p1 = (int[]) p.clone();
				p1[i]++;
				PartitionerState newState = new PartitionerState(p1, state.n - 1);
				if (!visitedStates.contains(newState)) {
					stack.push(newState);
					visitedStates.add(newState);
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		Partitioner p = new Partitioner(20, 3);
		while (p.hasNext()) {
			int[] partition = (int[]) p.next();
			for (int i = 0; i < partition.length; i++) {
				System.out.print(partition[i] + " ");
			}
			System.out.println("");
		}
	}
}

class PartitionerState {

	protected int n;

	protected int[] p;

	public PartitionerState(int[] p, int n) {
		this.n = n;
		this.p = p;
	}

	public boolean equals(Object other) {
		PartitionerState s = (PartitionerState) other;
		if (this.n != s.n) {
			return false;
		}
		for (int i = 0; i < this.p.length; i++) {
			if (this.p[i] != s.p[i]) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		int hash = n;
		int m = 1;
		for (int i = 0; i < p.length; i++) {
			hash += m * p[i];
			m <<= 1;
		}
		return hash;
	}

}