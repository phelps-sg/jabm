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

import java.io.Serializable;

import net.sourceforge.jabm.report.DataWriter;

/**
 * @author Steve Phelps
 * @version $Revision: 189 $
 */

public class MetaLearner extends AbstractLearner implements
    StimuliResponseLearner, Serializable {
	
	protected int currentLearner;
	
	protected StimuliResponseLearner[] subLearners;
	
	protected StimuliResponseLearner masterLearner;

	public MetaLearner() {
	}

	public MetaLearner(int numLearners) {
		subLearners = new StimuliResponseLearner[numLearners];
	}

//	public void setup(ParameterDatabase parameters, Parameter base) {
//
//		masterLearner = (StimuliResponseLearner) parameters
//		    .getInstanceForParameter(base.push(P_MASTER), null,
//		        StimuliResponseLearner.class);
//		if (masterLearner instanceof Parameterizable) {
//			((Parameterizable) masterLearner).setup(parameters, base.push(P_MASTER));
//		}
//
//		int numLearners = parameters.getInt(base.push(P_N), null, 1);
//
//		subLearners = new StimuliResponseLearner[numLearners];
//
//		for (int i = 0; i < numLearners; i++) {
//
//			StimuliResponseLearner sub = (StimuliResponseLearner) parameters
//			    .getInstanceForParameter(base.push(i + ""), null,
//			        StimuliResponseLearner.class);
//
//			if (sub instanceof Parameterizable) {
//				((Parameterizable) sub).setup(parameters, base.push(i + ""));
//			}
//
//			subLearners[i] = sub;
//		}
//	}

	public int act() {
		currentLearner = masterLearner.act();
		return subLearners[currentLearner].act();
	}

	public void reward(double reward) {
		masterLearner.reward(reward);
		subLearners[currentLearner].reward(reward);
	}

	public double getLearningDelta() {
		return masterLearner.getLearningDelta();
	}

	public int getNumberOfActions() {
		return subLearners.length;
	}

	public void dumpState(DataWriter out) {
		// TODO
	}

	@Override
	public int bestAction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int worstAction() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
