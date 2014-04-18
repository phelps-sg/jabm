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

package net.sourceforge.jabm.learning;

/**
 * A learner that attempts to adjust its output to match a training signal.
 * 
 * @author Steve Phelps
 * @version $Revision: 16 $
 */

public interface MimicryLearner extends ContinuousLearner {

	/**
	 * Provide a training signal to the learning algorithm.
	 */
	public void train(double target);

	/**
	 * Initialise the learning algorithm to output the supplied value.
	 */
	public void setOutputLevel(double currentOutput);

	/**
	 * Initialise with random values for free parameters
	 */
	public void randomInitialise();

}
