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

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;

/**
 * <p>
 * A utility class for cumulative tracking of stats for a set of doubles.
 * Moments are incremented dynamically, rather than keeping the actual cases in
 * memory.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <p>
 * <code>
 * Distribution series1 = new SummaryStats("series1");<br>
 * series1.newData(4.5);<br>
 * series1.newData(5.6);<br>
 * series1.newData(9.0);<br>
 * System.out.println("Standard deviation of series1 = " + series1.getStdDev());<br>
 * series1.newData(5.56);<br>
 * series1.newData(12);<br>
 * System.out.println("And now the standard deviation = " + series1.getStdDev());<br>
 * </code>
 * </p>
 * 
 * @author Steve Phelps
 * @version $Revision: 230 $
 */

public class SummaryStats implements Serializable, Cloneable,
    Resetable, Distribution {

	protected SummaryStatistics stats;
	
	/**
	 * The name of this data set.
	 */
	protected String varName;

	static Logger logger = Logger.getLogger(SummaryStats.class);

	public SummaryStats(String varName) {
		this.varName = varName;
		initialise();
	}

	public SummaryStats() {
		this("");
	}

	public void initialise() {
		stats = new SummaryStatistics();
	}

	/**
	 * Add a new datum to the set.
	 */
	public void newData(double i) {
		stats.addValue(i);
	}

	/**
	 * Get the number of items in the set.
	 */
	public int getN() {
		return (int) stats.getN();
	}

	/**
	 * Get the mean of the data.
	 */
	public double getMean() {
		return stats.getMean();
	}

	/**
	 * Get the variance about the mean.
	 */
	public double getVariance() {
		return stats.getVariance();
	}

	/**
	 * Get the standard deviation from the mean.
	 */
	public double getStdDev() {
		return stats.getStandardDeviation();
	}

	/**
	 * Get the minimum datum.
	 */
	public double getMin() {
		return stats.getMin();
	}

	/**
	 * Get the maximum datum.
	 */
	public double getMax() {
		return stats.getMax();
	}

	/**
	 * Get the total of the data
	 */
	public double getTotal() {
		return stats.getSum();
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void reset() {
		initialise();
	}

	public String getName() {
		return varName;
	}

	public String toString() {
		return "(" + getClass() + " varName:" + varName + " n:" + getN()
				+ " min:" + getMin() + " max:" + getMax() + " mean:"
				+ getMean() + " stdev:" + getStdDev() + ")";
	}

	public void log() {
		logger.info(getName());
		logger.info("\tn:\t" + getN());
		logger.info("\tmin:\t" + getMin());
		logger.info("\tmax:\t" + getMax());
		logger.info("\tmean:\t" + getMean());
		logger.info("\tstdev:\t" + getStdDev());
	}


}