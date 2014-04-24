package net.sourceforge.jabm.evolution;

import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.event.InteractionsFinishedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationStartingEvent;
import net.sourceforge.jabm.evolution.FitnessFunction;
import net.sourceforge.jabm.report.Report;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;
import java.util.Map;

/**
 * (c) Steve Phelps 2013
 */
public class EMAFitnessFunction implements FitnessFunction, Report {

    protected FitnessFunction baseFitnessFunction;

    protected HashMap<Agent,Double> fitnesses = new HashMap<Agent,Double>();

    protected double alpha;

    public FitnessFunction getBaseFitnessFunction() {
        return baseFitnessFunction;
    }

    @Required
    public void setBaseFitnessFunction(FitnessFunction baseFitnessFunction) {
        this.baseFitnessFunction = baseFitnessFunction;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public double getFitness(Agent i) {
        if (fitnesses.containsKey(i)) {
            return fitnesses.get(i);
        } else {
            return 0.0;
        }
    }

    @Override
    public Map<Object, Number> getVariableBindings() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void eventOccurred(SimEvent event) {
        if (event instanceof SimulationStartingEvent) {
            initialiseFitnesses((SimulationStartingEvent) event);
        } else if (event instanceof InteractionsFinishedEvent) {
            updateFitnesses();
        }
    }

    protected void updateFitnesses() {
        for(Agent i: fitnesses.keySet()) {
            Double existingFitness = getFitness(i);
            double newFitness = alpha * existingFitness + (1 - alpha) * baseFitnessFunction.getFitness(i);
            fitnesses.put(i, newFitness);
        }
    }

    protected void initialiseFitnesses(SimulationStartingEvent event) {
        this.fitnesses = new HashMap<Agent, Double>();
        for(Agent agent : event.getSimulation().getPopulation().getAgents()) {
        	this.fitnesses.put(agent, 0.0);
        }

    }
}
