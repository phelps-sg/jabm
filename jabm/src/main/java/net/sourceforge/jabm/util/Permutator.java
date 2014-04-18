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
import java.util.Stack;

/**
 * 
 * @author Steve Phelps
 * @version $Revision: 104 $
 */

public class Permutator implements Iterator<int[]>, Serializable {

	protected int[] goal;

	protected int n;

	protected Stack<PermutatorState> stack;

	public Permutator(int[] goal) {
		n = 0;
		for (int i = 0; i < goal.length; i++) {
			n += goal[i];
		}
		this.goal = goal;
		stack = new Stack<PermutatorState>();
		stack.push(new PermutatorState(new int[n], 0));
	}

	public boolean hasNext() {
		return !stack.isEmpty();
	}

	public int[] next() {
		return permutate();
	}

	public void remove() {
		throw new IllegalArgumentException("method remove() not implemented");
	}

	protected int[] permutate() {
		while (!stack.isEmpty()) {
			PermutatorState state = stack.pop();
			if (state.j == n) {
				return state.solution;
			}
			int[] k = new int[goal.length];
			for (int i = 0; i < state.j; i++) {
				k[state.solution[i]]++;
			}
			for (int i = 0; i < goal.length; i++) {
				if (k[i] < goal[i]) {
					int[] solution1 = (int[]) state.solution.clone();
					solution1[state.j] = i;
					stack.push(new PermutatorState(solution1, state.j + 1));
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		Permutator p = new Permutator(new int[] { 2, 1, 3 });
		while (p.hasNext()) {
			int[] s = (int[]) p.next();
			for (int i = 0; i < s.length; i++) {
				System.out.print(s[i] + " ");
			}
			System.out.println("");
		}
	}
}

class PermutatorState {

	protected int[] solution;

	protected int j;

	public PermutatorState(int[] solution, int j) {
		this.solution = solution;
		this.j = j;
	}

}
