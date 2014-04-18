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
package net.sourceforge.jabm.report;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import net.sourceforge.jabm.strategy.Strategy;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;

public class PayoffMap implements Serializable, Cloneable {

	protected LinkedHashMap<Strategy, SummaryStatistics> payoffs 
		= new LinkedHashMap<Strategy, SummaryStatistics>();

	protected Vector<Strategy> strategyIndex = new Vector<Strategy>();

	public PayoffMap() {
	}
	
	public PayoffMap(List<Strategy> strategies,
			ObjectFactory<SummaryStatistics> summaryStatisticsFactory) {
		super();
		for(Strategy strategy : strategies) {
			payoffs.put(strategy, summaryStatisticsFactory.getObject());
			strategyIndex.add(strategy);
		}
	}

	public PayoffMap(List<Strategy> strategies) {
		this(strategies, new ObjectFactory<SummaryStatistics>() {
			@Override
			public SummaryStatistics getObject() throws BeansException {
				return new SummaryStatistics();
			}
		});
	}

	public void updatePayoff(Strategy strategy, double fitness) {
		SummaryStatistics stats = payoffs.get(strategy);
		if (stats == null) {
			stats = new SummaryStatistics();
			payoffs.put(strategy, stats);
			strategyIndex.add(strategy);
		}
		stats.addValue(fitness);
	}

	public Set<Strategy> getStrategies() {
		return payoffs.keySet();
	}

	public double getMeanPayoff(Strategy strategy) {
		return payoffs.get(strategy).getMean();
	}

	public SummaryStatistics getPayoffDistribution(Strategy strategy) {
		return payoffs.get(strategy);
	}
	
	public SummaryStatistics getPayoffDistribution(int i) {
		return getPayoffDistribution(strategyIndex.get(i));
	}
	
	public double getMeanPayoff(int i) {
		return getMeanPayoff(strategyIndex.get(i));
	}

	public String toString() {
		StringBuffer result = new StringBuffer("[");
		Iterator<Strategy> i = payoffs.keySet().iterator();
		while (i.hasNext()) {
			Strategy s = i.next();
			double meanPayoff = getMeanPayoff(s);
			result.append(s + "=" + meanPayoff);
			if (i.hasNext()) {
				result.append(" ");
			}
		}
		result.append("]");
		return result.toString();
	}

	public int size() {
		return strategyIndex.size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object clone() throws CloneNotSupportedException {
		PayoffMap result = (PayoffMap) super.clone();
		result.payoffs = (LinkedHashMap<Strategy, SummaryStatistics>) this.payoffs
				.clone();
		result.strategyIndex = (Vector<Strategy>) this.strategyIndex.clone();
		return result;
	}

	public void initialise() {
		for(Strategy s : strategyIndex) {
			payoffs.put(s, new SummaryStatistics());
		}
		
	}
	
	// public String toString() {
	// return payoffs.toString();
	// }
}
