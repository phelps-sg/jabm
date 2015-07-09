package net.sourceforge.jabm.report;

import junit.framework.TestCase;
import net.sourceforge.jabm.strategy.MockStrategy;
import net.sourceforge.jabm.strategy.Strategy;

import java.util.LinkedList;

/**
 * Created by sphelps on 09/07/15.
 */
public class PayoffMapTest extends TestCase {


    PayoffMap payoffs;

    Strategy strategy1;

    Strategy strategy2;

    public void setUp() {
        strategy1 = new MockStrategy("strategy1");
        strategy2 = new MockStrategy("strategy2");
        LinkedList<Strategy> strategies = new LinkedList<Strategy>();
        strategies.add(strategy1);
        strategies.add(strategy2);
        payoffs = new PayoffMap(strategies);
    }

    public void testUpdatePayoff() throws Exception {
        assertTrue(Double.isNaN(payoffs.getMeanPayoff(strategy1)));
        payoffs.updatePayoff(strategy1, 1.0);
        assertEquals(1.0, payoffs.getMeanPayoff(strategy1));
        payoffs.updatePayoff(strategy1, 2.0);
        assertEquals(1.5, payoffs.getMeanPayoff(strategy1));
        assertTrue(Double.isNaN(payoffs.getMeanPayoff(strategy2)));
    }
}