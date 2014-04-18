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

import cern.jet.random.AbstractContinousDistribution;

/**
 * @author Steve Phelps
 * @version $Revision: 307 $
 */
public class WidrowHoffLearnerWithMomentum extends WidrowHoffLearner implements
		Serializable {

	/**
	 * cumulative discounted delta
	 */
	protected double gamma;

	protected double momentum;

	public WidrowHoffLearnerWithMomentum(
			AbstractContinousDistribution randomParamDistribution) {
		super(randomParamDistribution);
	}

	public WidrowHoffLearnerWithMomentum(double learningRate,
			AbstractContinousDistribution randomParamDistribution) {
		super(learningRate, randomParamDistribution);
	}

	public WidrowHoffLearnerWithMomentum() {
		super();
	}

	public void initialise() {
		super.initialise();
		gamma = 0;
	}

	public void train(double target) {
		gamma = momentum * gamma + (1 - momentum) * delta(target);
		currentOutput += gamma;
	}

	public void randomInitialise() {
		super.randomInitialise();
		gamma = 0;
		momentum = randomParamDistribution.nextDouble();
	}

	public double getMomentum() {
		return momentum;
	}

	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}

	public String toString() {
		return "(" + getClass().getSimpleName() + " learningRate:" + learningRate
		    + " momentum=" + momentum + ")";
	}
}
