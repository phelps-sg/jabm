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
package net.sourceforge.jabm.examples.elfarolbar;

import java.util.Vector;

import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.event.RoundFinishedEvent;
import net.sourceforge.jabm.event.RoundStartingEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.strategy.AbstractStrategy;
import net.sourceforge.jabm.util.MathUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * A strategy for inductively predicting future attendance at the bar
 * based on previous attendance values.
 * 
 * @author Steve Phelps
 *
 */
public abstract class AbstractPredictionStrategy extends AbstractStrategy {

	/**
	 * The bar tender who is keeping track of historical attendance.
	 */
	protected BarTender barTender;
	
	/**
	 * The history of our previous predictions.
	 */
	protected Vector<Double> previousPredictions;

	/**
	 * The current forecast error for this strategy.
	 */
	protected double forecastError = 0.0;

	/**
	 * The most recent attendance prediction made by this rule.
	 */
	protected double currentPrediction;
	
	/**
	 * The recency parameter determines how much weight to put
	 * and more recent forecast errors. 
	 */
	protected double recency = 0.8;
	
	static Logger logger = Logger.getLogger(AbstractPredictionStrategy.class);
	
	public AbstractPredictionStrategy() {
		super();
		previousPredictions = new Vector<Double>(100);
	}
	
	@Override
	public void subscribeToEvents(EventScheduler scheduler) {
		super.subscribeToEvents();
		scheduler.addListener(RoundFinishedEvent.class, this);
		scheduler.addListener(RoundStartingEvent.class, this);
	}
	
	@Override
	public void eventOccurred(SimEvent event) {
		super.eventOccurred(event);
		if (event instanceof RoundFinishedEvent) {
			onWeekFinished();
		} else if (event instanceof RoundStartingEvent) {
			onWeekStarting();
		}
	}
	
	public void onWeekStarting() {
		makePrediction();
	}
	
	public void onWeekFinished() {
		previousPredictions.add(getCurrentPrediction());
		updateForecastError();
	}
	
	public BarTender getBarTender() {
		return barTender;
	}

	@Required
	public void setBarTender(BarTender barTender) {
		this.barTender = barTender;
	}
	
	public int getCurrentWeek() {
		return (int) getScheduler().getSimulationTime().getTicks();
	}

	/**
	 * Recalculate the forecast error based on our our most recent prediction.
	 */
	public void updateForecastError() {
		int t = getCurrentWeek();
		double predicted = previousPredictions.get(t);
		double actual = barTender.getAttendance(t);
		double difference = MathUtil.squared(predicted - actual);
		// Use an exponential moving average to put more weight on recent
		//  errors.
		forecastError = recency * difference + (1 - recency) * forecastError;
	}


	/**
	 * Get the current forecast error for this rule.
	 */
	public double getForecastError() {
		return forecastError;
	}

	
	/**
	 * Get the most recent prediction.
	 * 
	 * @return  The last prediction made regarding attendance.
	 */
	public double getCurrentPrediction() {
		return currentPrediction;
	}

	public double getRecency() {
		return recency;
	}

	/**
	 * Set the recency parameter which determines how much weight to put
	 * on more recent forecast errors.
	 * 
	 * @param recency  The parameter used to calculate EMA of forecast errors.
	 */
	public void setRecency(double recency) {
		this.recency = recency;
	}
	
	public abstract void makePrediction();

}
