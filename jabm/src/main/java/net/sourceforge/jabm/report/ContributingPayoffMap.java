package net.sourceforge.jabm.report;

import net.sourceforge.jabm.strategy.Strategy;
import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;

import java.util.List;

/**
 * (C) Steve Phelps 2015
 */
public class ContributingPayoffMap extends PayoffMap {

    /**
     * The aggregate payoff map to which this one contributes.
     */
    protected AggregatePayoffMap aggregatePayoffMap;

    public ContributingPayoffMap(AggregatePayoffMap aggregatePayoffMap, List<Strategy> strategies) {
        super();
        this.aggregatePayoffMap = aggregatePayoffMap;
        this.strategies = strategies;
        initialise();
    }

    @Override
    public StatisticalSummary createStatisticalSummary(Strategy s) {
        AggregateSummaryStatistics aggregateStats =
                (AggregateSummaryStatistics) aggregatePayoffMap.getPayoffDistribution(s);
        return aggregateStats.createContributingStatistics();
    }
}
