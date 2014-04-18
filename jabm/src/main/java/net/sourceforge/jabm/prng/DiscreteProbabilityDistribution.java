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

package net.sourceforge.jabm.prng;


import java.io.Serializable;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.jabm.util.MathUtil;
import net.sourceforge.jabm.util.Resetable;

import org.apache.log4j.Logger;

import cern.jet.random.engine.RandomEngine;

/**
 * A class representing a discrete probability distribution which can used to
 * generate random events according to the specified distribution. The output
 * from a uniform PRNG is used to to select from the different possible events.
 * 
 * @author Steve Phelps
 * @version $Revision: 104 $
 */

public class DiscreteProbabilityDistribution implements Resetable,
    Serializable  {

	/**
	 * The probability distribution.
	 */
	protected TreeSet<ProbabilityActionPair> p;
	
	protected TreeMap<Integer, Double> reverseMap;

	/**
	 * The number of possible events for this distribution.
	 */
	protected int n;
	
	protected RandomEngine prng;

	/**
	 * The log4j logger.
	 */
	static Logger logger = Logger
	    .getLogger(DiscreteProbabilityDistribution.class);

	/**
	 * Construct a new distribution with k possible events.
	 * 
	 * @param k
	 *          The number of possible events for this random variable
	 */
	public DiscreteProbabilityDistribution(RandomEngine prng, int n) {
		this.prng = prng;
		this.n = n;
		initialise();
	}

	public void initialise() {
		p = new TreeSet<ProbabilityActionPair>();
		reverseMap = new TreeMap<Integer,Double>();
		for (int i = 0; i < n; i++) {
			double probability = 1.0 / (double) n;
			p.add(new ProbabilityActionPair(probability, i));
			reverseMap.put(i, probability);
		}
	}
	
	public void initialise(double[] probs) {
		p = new TreeSet<ProbabilityActionPair>();
		reverseMap = new TreeMap<Integer,Double>();
		for (int i = 0; i < n; i++) {
			double probability = probs[i];
			p.add(new ProbabilityActionPair(probability, i));
			reverseMap.put(i, probability);
		}
	}
	
	public DiscreteProbabilityDistribution(RandomEngine prng, double[] p) {
		this.prng = prng;
		this.n = p.length;
		initialise(p);
	}


	/**
	 * Set the probability of the ith event.
	 * 
	 * @param i
	 *          The event
	 * @param probability
	 *          The probability of event i occuring
	 */
	public void setProbability(int i, double probability) {
		double oldProbability = reverseMap.get(i);
		p.remove(new ProbabilityActionPair(oldProbability, i));
		reverseMap.remove(i);
		p.add(new ProbabilityActionPair(probability, i));
		reverseMap.put(i, probability);
	}

	/**
	 * Get the probability of the ith event.
	 * 
	 * @param i
	 *          The event
	 */
	public double getProbability(int i) {
		return reverseMap.get(i);
	}

	/**
	 * Generate a random event according to the probability distribution.
	 * 
	 * @return An integer value representing one of the possible events.
	 */
	public int generateRandomEvent() {
		double rand = prng.raw();
		double cummProb = 0;
		assert MathUtil.approxEqual(getSum(), 1); 
		for(ProbabilityActionPair pair : p) {
			cummProb += pair.probability;
			if (rand <= cummProb) {
				return pair.action;
			}
		}
		throw new ProbabilityError(this);
	}
	
	public double getSum() {
		double sum = 0;
		for(ProbabilityActionPair pair : p) {
			sum += pair.probability;
		}
		return sum;
	}

	public void reset() {
		initialise();
	}
	
	/**
	 * Compute the expected value of the random variable defined by this
	 * distribution.
	 * 
	 * @return The expected value of the distribution
	 */
	public double computeMean() {
		double total = 0;
		for (int i = 0; i < n; i++) {
			total += i * reverseMap.get(i);
		}
		return total;
	}

	public int getN() {
		return n;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer("(" + getClass());
		for (int i = 0; i < n; i++) {
			s.append(" p[" + i + "]:" + reverseMap.get(i));
		}
		s.append(")");
		return s.toString();
	}

	public static class ProbabilityError extends Error {

		public ProbabilityError(DiscreteProbabilityDistribution p) {
			super("Probabilities do not sum to 1: " + p);
		}

	}
	
	public class ProbabilityActionPair implements Comparable<ProbabilityActionPair> {
		
		protected double probability;
		
		protected int action;

		public ProbabilityActionPair(double probability, int action) {
			super();
			this.probability = probability;
			this.action = action;
		}
		
		public ProbabilityActionPair(int action) {
			this.action = action;
		}

		@Override
		public boolean equals(Object obj) {
			ProbabilityActionPair other = (ProbabilityActionPair) obj;
			return this.probability == other.probability && this.action == other.action;
		}

		@Override
		public int compareTo(ProbabilityActionPair o) {
			if (this.probability < o.probability) {
				return -1;
			} else if (this.probability > o.probability) {
				return +1;
			} else if (this.action == o.action) {
				return 0;
			} else if (this.action > o.action) {
				return +1;
			} else {
				return -1;
			}
		}

		@Override
		public String toString() {
			return "ProbabilityActionPair [probability=" + probability
					+ ", action=" + action + "]";
		}

	}

}
