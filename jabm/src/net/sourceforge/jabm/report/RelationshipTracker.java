package net.sourceforge.jabm.report;

import net.sourceforge.jabm.agent.Agent;
import edu.uci.ics.jung.graph.Graph;

public interface RelationshipTracker {

	public Graph<Agent, WeightedEdge> getGraph();

	public double vertexStrength(Agent vertex);

	public int outDegree(Agent vertex);

	public int inDegree(Agent vertex);
	
	public double edgeStrength(Agent i, Agent j);

}
