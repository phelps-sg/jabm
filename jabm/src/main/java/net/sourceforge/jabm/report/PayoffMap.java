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
import java.util.*;

import net.sourceforge.jabm.strategy.Strategy;

import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;

@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
public class PayoffMap implements Serializable, Cloneable {

	protected LinkedHashMap<Strategy, StatisticalSummary> payoffs
		= new LinkedHashMap<Strategy, StatisticalSummary>();

	protected Vector<Strategy> strategyIndex = new Vector<Strategy>();

	protected List<Strategy> strategies;

	public PayoffMap() {
		this(new LinkedList<Strategy>());
	}
	
	public PayoffMap(List<Strategy> strategies) {
		this.strategies = strategies;
		initialise();
    }

    public void initialise() {
		payoffs = new LinkedHashMap<Strategy, StatisticalSummary>();
		strategyIndex = new Vector<Strategy>();
        for(Strategy strategy : strategies) {
			strategyIndex.add(strategy);
            payoffs.put(strategy, createStatisticalSummary(strategy) );
        }
    }

    public void updatePayoff(Strategy strategy, double fitness) {
		SummaryStatistics stats = (SummaryStatistics) payoffs.get(strategy);
        if (stats == null) {
            stats = (SummaryStatistics) createStatisticalSummary(strategy);
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

	public StatisticalSummary getPayoffDistribution(Strategy strategy) {
		return payoffs.get(strategy);
	}

	public StatisticalSummary getPayoffDistribution(int i) {
		return getPayoffDistribution(strategyIndex.get(i));
	}
	
	public double getMeanPayoff(int i) {
		return getMeanPayoff(strategyIndex.get(i));
	}

	public String toString() {
		StringBuilder result = new StringBuilder("[");
		Iterator<Strategy> i = payoffs.keySet().iterator();
		while (i.hasNext()) {
			Strategy s = i.next();
			double meanPayoff = getMeanPayoff(s);
			result.append(s).append("=").append(meanPayoff);
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
		result.payoffs = (LinkedHashMap<Strategy, StatisticalSummary>) this.payoffs
				.clone();
		result.strategyIndex = (Vector<Strategy>) this.strategyIndex.clone();
		return result;
	}

	public StatisticalSummary createStatisticalSummary(Strategy s) {
		return new SummaryStatistics();
	}
	
}
