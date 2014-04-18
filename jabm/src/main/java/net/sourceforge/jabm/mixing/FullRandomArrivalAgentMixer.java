package net.sourceforge.jabm.mixing;

import java.util.ArrayList;

import net.sourceforge.jabm.SimulationController;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.agent.AgentList;
import net.sourceforge.jabm.event.AgentArrivalEvent;

public class FullRandomArrivalAgentMixer extends RandomArrivalAgentMixer {

	@Override
	public void fireAgentArrivalEvent(Agent agent, AgentList group,
			SimulationController simulation) {
		ArrayList<Agent> others = new ArrayList<Agent>(group.size() - 1);
		for (Agent i : group.getAgents()) {
			if (i != agent) {
				others.add(i);
			}
		}
		simulation.fireEvent(new AgentArrivalEvent(simulation, agent, others));
	}

}
