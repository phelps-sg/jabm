package net.sourceforge.jabm.strategy;

import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.event.SimEvent;

import java.util.List;

/**
 * Created by sphelps on 09/07/15.
 */
public class MockStrategy implements Strategy {

    protected String name;

    public MockStrategy(String name) {
        this.name = name;
    }

    @Override
    public void setAgent(Agent agent) {

    }

    @Override
    public Agent getAgent() {
        return null;
    }

    @Override
    public void subscribeToEvents(EventScheduler scheduler) {

    }

    @Override
    public void execute(List<Agent> otherAgents) {

    }

    @Override
    public void unsubscribeFromEvents() {

    }

    @Override
    public void eventOccurred(SimEvent event) {

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return this.clone();
    }

}
