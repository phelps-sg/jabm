package net.sourceforge.jabm.strategy;

import java.io.Serializable;

import net.sourceforge.jabm.agent.Agent;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractRlStrategy extends AbstractStrategy 
		implements Serializable {

	protected Strategy[] actions;
	
	protected Strategy currentStrategy;
	
	protected transient ObjectFactory<Strategy> strategyFactory;
	
	protected double[] initialPropensities;

	public AbstractRlStrategy(Agent agent) {
		super(agent);
	}
	
	public AbstractRlStrategy() {
	}

	@Override
	public void unsubscribeFromEvents() {
		for(int i=0; i<actions.length; i++) {
			actions[i].unsubscribeFromEvents();
		}
		super.unsubscribeFromEvents();
	}

	public ObjectFactory<Strategy> getStrategyFactory() {
		return strategyFactory;
	}

	@Required
	public void setStrategyFactory(ObjectFactory<Strategy> strategyFactory) {
		this.strategyFactory = strategyFactory;
	}

	public double[] getInitialPropensities() {
		return initialPropensities;
	}

	public void initialise() {
		int numActions = getNumberOfActions();
		actions = new Strategy[numActions];
		for(int i=0; i<numActions; i++) {
			Strategy strategy = strategyFactory.getObject();
			strategy.setAgent(agent);
			actions[i] = strategy;
		}
	}
	
	public abstract int getNumberOfActions();
	
}
