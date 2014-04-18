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
package net.sourceforge.jabm.learning;

/**
 * <p>
 * An action selection policy for a reinforcement-learning algorithm. The job of
 * the action selector is to select from the available actions in a particular
 * state in such a way as to balance exploitation against exploration.
 * </p>
 * 
 * <p>
 * See:<br>
 * Sutton, R. S., Barto, A. G., 1998. Reinforcement Learning: An Introduction.
 * MIT Press.<br>
 * </p>
 * 
 * @author Steve Phelps
 * 
 */
public interface ActionSelector {

	/**
	 * Choose an action according to the current state and the 
	 * current value estimates for each action.
	 * 
	 * @param state  	The current state of the MDP.
	 * @param learner  The algorithm used to update the value estimates.
	 * 
	 * @return An integer representing the action chosen (indexed from 0).
	 */
	public int act(int state, MDPLearner learner);

}