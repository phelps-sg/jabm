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

package net.sourceforge.jabm.test.util;

import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.jabm.util.SummaryStats;

/**
 * @author Steve Phelps
 * @version $Revision: 1.1 $
 */

public class CummulativeDistributionTest extends TestCase {

	/**
	 * @uml.property name="testSeries"
	 * @uml.associationEnd
	 */
	SummaryStats testSeries;

	public CummulativeDistributionTest(String name) {
		super(name);
	}

	public void setUp() {
		testSeries = new SummaryStats("test series");
	}

	public void testMean() {
		Random randGenerator = new Random();

		for (int i = 0; i < 1000000; i++) {
			testSeries.newData(randGenerator.nextDouble());
		}
		System.out.println(testSeries);
		assertTrue(Math.abs(testSeries.getMean() - 0.5) < 0.01);
	}

	public void testStdDev() {
		double series[][] = new double[][] {
		    { 0.21631395553352, 0.41537486044322, 0.21396333159617,
		        0.68333232433848, 0.45142482676248, 0.60854036122399,
		        0.08407906075044, 0.12104711303641, 0.23189431811233 },
		    { 0.1947, 0.30499867700349, 0.64349228788535, 0.21255986433874,
		        0.04389532534714, 0.01575981791975, 0.45435514975555,
		        0.45075394097939, 0.23931256446899 } };

		testSeries.reset();
		for (int j = 0; j < series.length; j++) {
			for (int i = 1; i < series[j].length; i++) {
				testSeries.newData(series[j][i]);
			}
			System.out.println(testSeries);
			System.out.println("target stdev = " + series[j][0]);
			assertTrue(approxEqual(testSeries.getStdDev(), series[j][0]));

			int n = testSeries.getN();
			double total = 0;
			for (int i = 1; i < series[j].length; i++) {
				total += series[j][i];
			}
			double mean = total / n;
			double variance = 0;
			for (int i = 1; i < series[j].length; i++) {
				double t = series[j][i] - mean;
				variance += t * t;
			}
			variance = variance / (n - 1);
			System.out.println("recalc stdev = " + Math.sqrt(variance));
		}
	}

	public boolean approxEqual(double x, double y) {
		return Math.abs(x - y) < 0.05;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(CummulativeDistributionTest.class);
	}

}