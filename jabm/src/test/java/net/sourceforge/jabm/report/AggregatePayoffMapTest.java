package net.sourceforge.jabm.report;

import junit.framework.TestCase;
import net.sourceforge.jabm.strategy.MockStrategy;
import net.sourceforge.jabm.strategy.Strategy;

import java.util.LinkedList;

/**
 * Created by sphelps on 09/07/15.
 */
public class AggregatePayoffMapTest extends TestCase {

    AggregatePayoffMap aggregatePayoffMap;

    ContributingPayoffMap contributingPayoffMap1, contributingPayoffMap2;

    Strategy strategy1, strategy2;

    public void setUp() {
        strategy1 = new MockStrategy("strategy1");
        strategy2 = new MockStrategy("strategy2");
        LinkedList<Strategy> strategies = new LinkedList<Strategy>();
        strategies.add(strategy1);
        strategies.add(strategy2);

        aggregatePayoffMap = new AggregatePayoffMap(strategies);
        contributingPayoffMap1 = new ContributingPayoffMap(aggregatePayoffMap, strategies);
        contributingPayoffMap2 = new ContributingPayoffMap(aggregatePayoffMap, strategies);
    }

    public void testUpdate() {
        assertTrue(Double.isNaN(aggregatePayoffMap.getMeanPayoff(strategy1)));
        contributingPayoffMap1.updatePayoff(strategy1, 1.0);
        assertEquals(1.0, contributingPayoffMap1.getMeanPayoff(strategy1));
        assertEquals(1.0, aggregatePayoffMap.getMeanPayoff(strategy1));
        contributingPayoffMap1.updatePayoff(strategy1, 2.0);
        assertEquals(1.5, aggregatePayoffMap.getMeanPayoff(strategy1));
        assertTrue(Double.isNaN(aggregatePayoffMap.getMeanPayoff(strategy2)));
    }
}