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
 * Classes implementing this interface implement learning algorithms for Markoff
 * descision processes (MDPs).
 * 
 * @author Steve Phelps
 * @version $Revision: 98 $
 */

public interface MDPLearner extends DiscreteLearner {

	/**
	 * The call-back after performing an action.
	 * 
	 * @param reward
	 *            The reward received from taking the most recently-selected
	 *            action.
	 * 
	 * @param newState
	 *            The new state encountered after taking the most
	 *            recently-selected action.
	 */
	public void newState(double reward, int newState);

	/**
	 * @param state The current state of the MDP.
	 * @return An array representing the Q values indexed by action.
	 */
	public double[] getValueEstimates(int state);

	public int bestAction(int state);

	public int getNumberOfActions();
	
	public int getNumberOfStates();

}
