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

package net.sourceforge.jabm.gametheory;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.jabm.report.AggregatePayoffMap;
import net.sourceforge.jabm.report.DataWriter;
import net.sourceforge.jabm.report.PayoffMap;
import net.sourceforge.jabm.strategy.Strategy;
import net.sourceforge.jabm.util.BaseNIterator;
import net.sourceforge.jabm.util.MathUtil;
import net.sourceforge.jabm.util.Partitioner;

import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;

import cern.jet.math.Arithmetic;

/**
 * @author Steve Phelps
 * @version $Revision: 325 $
 */

public class CompressedPayoffMatrix  implements Serializable {
	
	protected HashMap<Entry,PayoffMap> matrix;

	protected int numRoles;

	protected int numStrategiesPerRole[];

	protected int numPlayersPerRole[];

	protected int numPlayers;

	protected int numStrategies;

	protected int numEntries;

	static Logger logger = Logger.getLogger(CompressedPayoffMatrix.class);

	public CompressedPayoffMatrix(int numRoles, int[] numStrategiesPerRole,
	    int[] numPlayersPerRole, List<Strategy> strategies) {
		numEntries = 0;
		this.numRoles = numRoles;
		this.numPlayersPerRole = numPlayersPerRole;
		this.numStrategiesPerRole = numStrategiesPerRole;
		for (int i = 0; i < numRoles; i++) {
			numPlayers += numPlayersPerRole[i];
			numStrategies += numStrategiesPerRole[i];
		}
		matrix = new HashMap<Entry,PayoffMap>();
		OrderedEntryIterator i = new OrderedEntryIterator();
		while (i.hasNext()) {
			Entry e = i.next();
			matrix.put(e, new AggregatePayoffMap(strategies));
		}
	}

	public CompressedPayoffMatrix(List<Strategy> strategies, int numPlayers) {
		this(1, new int[] { strategies.size() }, new int[] { numPlayers },
				strategies);
	}
	
	public int size() {
		return matrix.keySet().size();
	}

	public PayoffMap getCompressedPayoffs(Entry entry) {
		return matrix.get(entry);
	}

//	public void updateWithPayoffs(Entry entry, PayoffMap compressedPayoffs) {
//        //TODO
//	}
//
//	public void reset() {
//		Iterator<Entry> i = compressedEntryIterator();
//		while (i.hasNext()) {
//			Entry e = i.next();
//			PayoffMap p = getCompressedPayoffs(e);
//			p.reset();
//		}
//	}

	public Iterator<Entry> compressedEntryIterator() {
		return matrix.keySet().iterator();
	}

	public Iterator<Entry> orderedEntryIterator() {
		return new OrderedEntryIterator();
	}

	public Iterator<FullEntry> fullEntryIterator() {
		final CompressedPayoffMatrix matrix = this;
		return new Iterator<FullEntry>() {

			BaseNIterator b = new BaseNIterator(numStrategies, numPlayers);

			public boolean hasNext() {
				return b.hasNext();
			}

			public FullEntry next() {
				return new FullEntry(b.next(), matrix);
			}

			public void remove() {
			}

		};
	}

	public double[] getFullPayoffs(FullEntry entry) {
		Entry compressedEntry = entry.compress();
		PayoffMap p = getCompressedPayoffs(compressedEntry);
		double[] fullPayoffs = new double[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			fullPayoffs[i] = p.getMeanPayoff(entry.getStrategy(i));
		}
		return fullPayoffs;
	}

	public double payoff(double[] mixedStrategy) {
		double payoff = 0;
		for (int s = 0; s < mixedStrategy.length; s++) {
			payoff += mixedStrategy[s] * payoff(s, mixedStrategy);
		}
		return payoff;
	}

	public double payoff(int strategy, double[] mixedStrategy) {
		
		if (mixedStrategy[strategy] == 0) {
			return 0;
		}
		
		assert MathUtil.approxEqual(MathUtil.sum(mixedStrategy), 1, 1E-10);
		
		@SuppressWarnings("all")
		double totalProbability = 0;
		double payoff = 0;
		
		Iterator<Entry> entries = compressedEntryIterator();
		iterating: while (entries.hasNext()) {
			Entry entry = entries.next();
			// double[] payoffs = getCompressedPayoffs(entry).getPayoffs();
			PayoffMap p = getCompressedPayoffs(entry);

			if (entry.getNumAgents(strategy) == 0) {
				continue iterating;
			}

			entry = entry.removeSingleAgent(strategy);

			double probability = 1;
			for (int s = 0; s < numStrategies; s++) {
				probability *= Math.pow(mixedStrategy[s], entry.getNumAgents(s));
			}
			probability *= entry.permutations();
			assert probability <= 1 && probability >= 0;
			totalProbability += probability;

			double expectedPayoffToStrategy = p.getMeanPayoff(strategy);
			if (Double.isNaN(expectedPayoffToStrategy)) {
				expectedPayoffToStrategy = 0;
			}
			payoff += probability * expectedPayoffToStrategy;

		}

		return payoff;
	}
	
	public static CompressedPayoffMatrix readFromFile(String fileName) {
		CompressedPayoffMatrix result = null;
		try {
			ObjectInputStream ois;
			ois = new ObjectInputStream(new FileInputStream(fileName));
			result = (CompressedPayoffMatrix) ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public void export(DataWriter out) {
		Iterator<Entry> entries = orderedEntryIterator();
		while (entries.hasNext()) {
			Entry entry = entries.next();
			for (int s = 0; s < numStrategies; s++) {
				out.newData(entry.getNumAgents(s));
			}
			PayoffMap payoffs = getCompressedPayoffs(entry);
			for (int i = 0; i < payoffs.size(); i++) {
				StatisticalSummary payoffStats = payoffs
						.getPayoffDistribution(i);
				out.newData(payoffStats.getMean());
				out.newData(payoffStats.getStandardDeviation());
				out.newData(payoffStats.getN());
			}
		}
	}

	public void exportToGambit(PrintWriter nfgOut) {
		exportToGambit(nfgOut, "JASA NFG");
	}

	public void exportToGambit(PrintWriter nfgOut, String title) {
		nfgOut.print("NFG 1 R \"" + title + "\" { ");
		for (int i = 0; i < numPlayers; i++) {
			nfgOut.print("\"Player" + (i + 1) + "\" ");
		}
		nfgOut.println("}");
		nfgOut.println();

		nfgOut.print("{ ");
		for (int i = 0; i < numPlayers; i++) {
			nfgOut.print("{ ");
			for (int j = 0; j < numStrategies; j++) {
				nfgOut.print("\"Strategy" + j + "\" ");
			}
			nfgOut.println("}");
		}
		nfgOut.println("}");

		nfgOut.println("\"\"");
		nfgOut.println();

		nfgOut.println("{");
		int numEntries = 0;
		Iterator<FullEntry> entries = fullEntryIterator();
		while (entries.hasNext()) {
			nfgOut.print("{ \"");
			FullEntry fullEntry = entries.next();
			for (int i = numPlayers - 1; i >= 0; i--) {
				nfgOut.print(fullEntry.getStrategy(i) + 1);
			}
			nfgOut.print("\" ");
			double[] outcome = getFullPayoffs(fullEntry);
			for (int i = 0; i < outcome.length; i++) {
				double payoff = outcome[i];
				if (Double.isNaN(payoff)) {
					nfgOut.print("0");
				} else {
					nfgOut.print(payoff);
				}
				if (i < outcome.length - 1) {
					nfgOut.print(",");
				}
				nfgOut.print(" ");
			}
			nfgOut.println("}");
			numEntries++;
		}
		nfgOut.println("}");
		for (int i = 1; i <= numEntries; i++) {
			nfgOut.print(i);
			if (i < numEntries) {
				nfgOut.print(" ");
			}
		}
		nfgOut.flush();
	}

	public int getNumStrategies() {
		return numStrategies;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public int getNumPlayersPerRole(int role) {
		return numPlayersPerRole[role];
	}

	public int getNumStrategiesPerRole(int role) {
		return numStrategiesPerRole[role];
	}

	public int getNumEntries() {
		return numEntries;
	}
	

	class OrderedEntryIterator implements Iterator<Entry>, Serializable {

		private Partitioner[] p;

		ArrayList<int[]> state;

		public OrderedEntryIterator() {
			p = new Partitioner[numRoles];
			state = new ArrayList<int[]>(numRoles);
			for (int i = 0; i < numRoles; i++) {
				p[i] = new Partitioner(numPlayersPerRole[i], numStrategiesPerRole[i]);
				state.add(i, null);
			}
		}

		public boolean hasNext() {
			for (int i = 0; i < numRoles; i++) {
				if (p[i].hasNext()) {
					return true;
				}
			}
			return false;
		}

		public Entry next() {
			for (int i = 0; i < numRoles - 1; i++) {
				if (state.get(i) == null) {
					state.set(i, p[i].next());
				}
			}
			nextState(numRoles - 1);
			int[] entry = new int[numStrategies];
			int strategy = 0;
			for (int i = 0; i < numRoles; i++) {
				for (int s = 0; s < numStrategiesPerRole[i]; s++) {
					entry[strategy++] = state.get(i)[s];
				}
			}
			return new Entry(entry);
		}

		public void remove() {
		}

		protected void nextState(int role) {
			if (p[role].hasNext()) {
				state.set(role, p[role].next());
			} else {
				p[role] = new Partitioner(numPlayersPerRole[role],
				    numStrategiesPerRole[role]);
				state.set(role, p[role].next());
				if (role > 0) {
					nextState(role - 1);
				}
			}
		}

	}

	public static class Entry implements Cloneable, Serializable {

		protected int[] numAgentsPerStrategy;

		public Entry(int[] numAgentsPerStrategy) {
			this.numAgentsPerStrategy = numAgentsPerStrategy;
		}

		public int getNumAgents(int strategy) {
			return numAgentsPerStrategy[strategy];
		}

		public Object clone() throws CloneNotSupportedException {
			Entry newEntry = (Entry) super.clone();
			newEntry.numAgentsPerStrategy = this.numAgentsPerStrategy.clone();
			return newEntry;
		}

		public Entry removeSingleAgent(int strategy) {
			try {
				Entry entry = (Entry) clone();
				if (numAgentsPerStrategy[strategy] > 0) {
					entry.numAgentsPerStrategy[strategy]--;
				}
				return entry;
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);
			}
		}

		public String toString() {
			StringBuilder result = new StringBuilder("");
			int numStrategies = numAgentsPerStrategy.length;
			for (int i = 0; i < numStrategies - 1; i++) {
				result.append(numAgentsPerStrategy[i]).append("/");
			}
			result.append(numAgentsPerStrategy[numStrategies - 1]);
			return result.toString();
		}

		public long permutations() {
			int numAgents = 0;
			for (int i = 0; i < numAgentsPerStrategy.length; i++) {
				numAgents += numAgentsPerStrategy[i];
			}
			long r = 1;
			for (int i = 0; i < numAgentsPerStrategy.length; i++) {
				r *= Arithmetic.factorial(numAgentsPerStrategy[i]);
			}
			return ((long) Arithmetic.factorial(numAgents)) / r;
		}

		public boolean equals(Object other) {
			for (int i = 0; i < numAgentsPerStrategy.length; i++) {
				if (this.numAgentsPerStrategy[i] != ((Entry) other).numAgentsPerStrategy[i]) {
					return false;
				}
			}
			return true;
		}

		public int hashCode() {
			int radix = 0;
			for (int i = 0; i < numAgentsPerStrategy.length; i++) {
				radix += numAgentsPerStrategy[i];
			}
			int hash = 0;
			int base = 1;
			for (int i = 0; i < numAgentsPerStrategy.length; i++) {
				hash += numAgentsPerStrategy[i] * base;
				base *= radix;
			}
			return hash;
		}
	}

	public static class FullEntry implements Serializable {

		protected int[] pureStrategyProfile;

		protected CompressedPayoffMatrix matrix;

		public FullEntry(int[] pureStrategyProfile, CompressedPayoffMatrix matrix) {
			this.pureStrategyProfile = pureStrategyProfile;
			this.matrix = matrix;
		}

		public int getStrategy(int player) {
			return pureStrategyProfile[player];
		}

		public Entry compress() {
			int[] numAgentsPerStrategy = new int[matrix.getNumStrategies()];
			for (int i = 0; i < matrix.getNumPlayers(); i++) {
				numAgentsPerStrategy[getStrategy(i)]++;
			}
			return new Entry(numAgentsPerStrategy);
		}

	}

}
