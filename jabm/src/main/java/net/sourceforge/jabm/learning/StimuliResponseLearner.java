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
 * Classes implementing this interface implement myopic stimuli-response
 * reinformcement learning algorithms.
 * 
 * @author Steve Phelps
 * @version $Revision: 16 $
 */

public interface StimuliResponseLearner extends DiscreteLearner {

	/**
	 * Reward the learning algorithm according to the last action it chose.
	 */
	public void reward(double reward);
	
	public int bestAction();
	
	public int worstAction();
	
}
