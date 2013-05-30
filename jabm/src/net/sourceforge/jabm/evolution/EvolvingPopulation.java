package net.sourceforge.jabm.evolution;

import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.event.InteractionsFinishedEvent;
import net.sourceforge.jabm.event.SimEvent;

/**
 * <p>
 * A population of agents which evolves through reproduction.
 * </p>
 * 
 * @author Steve Phelps
 */
public class EvolvingPopulation extends Population {

	/**
	 * The breeder for this population which specifies how the population
	 *  reproduces.
	 */
	protected Breeder breeder;
	
	protected int breedingInterval = 100;

	/**
	 * Produce the next generation of agents.
	 */
	public void reproduce() {
		agentList = breeder.reproduce(agentList);
	}
	
	public Breeder getBreeder() {
		return breeder;
	}

	/**
	 * Configure the breeder for this population which specifies
	 *  how the agents in this population reproduce.
	 * @param breeder
	 */
	public void setBreeder(Breeder breeder) {
		this.breeder = breeder;
	}

	public int getBreedingInterval() {
		return breedingInterval;
	}

	/**
	 * Configure the breeding interval which specifies the rate at which
	 * reproduction occurs.
	 * 
	 * @param breedingInterval
	 *            The number of simulation ticks between reproduction events.
	 */
	public void setBreedingInterval(int breedingInterval) {
		this.breedingInterval = breedingInterval;
	}

	@Override
	public void eventOccurred(SimEvent event) {
		super.eventOccurred(event);
		if (event instanceof InteractionsFinishedEvent) {
			long t = 
					(((InteractionsFinishedEvent) event).getSimulation().getSimulationTime().getTicks());
			if (((t+1) % breedingInterval) == 0) {
				reproduce();
			}
		}
	}
	
}
