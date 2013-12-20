package net.sourceforge.jabm.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationEvent;
import net.sourceforge.jabm.util.HashCodeComparator;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;

import edu.uci.ics.jung.algorithms.metrics.TriadicCensus;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;

public class WeightedGraphMetrics extends AbstractReportVariables {

	protected RelationshipTracker relationshipTracker;
	
	protected Map<Agent, Double> clusteringCoefficients;
	
	protected SummaryStatistics clusteringStats = new SummaryStatistics();
	
	protected long timeStamp;

	protected SummaryStatistics degreeStats = new SummaryStatistics();

	protected HashMap<Agent,Integer> inDegreeByAgent;
	
	protected HashMap<Agent, Integer> outDegreeByAgent;
	
	protected long[] triadCounts = new long[16];
	
	protected double diameter;
	
	protected double averagePathLength;

	private Map<Agent, Double> vertexStrengthByAgent = 
		new HashMap<Agent, Double>();
	
	static Logger logger = Logger.getLogger(WeightedGraphMetrics.class);
	
	public WeightedGraphMetrics(RelationshipTracker relationshipTracker) {
		super("graphmetrics");
		this.relationshipTracker = relationshipTracker;
	}
	
	public WeightedGraphMetrics() {
		this(null);
	}

	@Override
	public Map<Object, Number> getVariableBindings() {
		Map<Object, Number> result = super.getVariableBindings();
		recordSummaryStatistics("clusteringcoefficient", result,
									clusteringStats);
		result.put("averagePathLength", this.averagePathLength);
		result.put("diameter", this.diameter);
//		LinkedList<Agent> agents = 
//			new LinkedList<Agent>(vertexStrengthByAgent.keySet());
//		Collections.sort(agents, new HashCodeComparator<Agent>());
//		for (Agent agent : agents) {
//			result.put("agent" + agent.hashCode(), inDegreeByAgent.get(agent)
//					+ outDegreeByAgent.get(agent));
//		}
		for(int i=0; i<16; i++) {
			result.put("triad" + i, this.triadCounts[i]);
		}
		return result;
	}
	
	@Override
	public void compute(SimEvent event) {
		super.compute(event);		
		computeClusteringStats();
		computeDegreeStats();
		computeDiameter();
		computeAveragePathLength();
		computeTriads();
	}
	
	public Graph<Agent, WeightedEdge> getGraph() {
		return relationshipTracker.getGraph();
	}
	
	public void computeDegreeStats() {
		this.degreeStats = new SummaryStatistics();
		inDegreeByAgent = new HashMap<Agent, Integer>();
		outDegreeByAgent = new HashMap<Agent, Integer>();
		vertexStrengthByAgent = new HashMap<Agent, Double>();
		Graph<Agent, WeightedEdge> graph = getGraph();
		for (Agent vertex : graph.getVertices()) {
			int inDegree = graph.inDegree(vertex);
			int outDegree = graph.outDegree(vertex);
			degreeStats.addValue(inDegree + outDegree);
			inDegreeByAgent.put(vertex, inDegree);
			outDegreeByAgent.put(vertex, outDegree);
			vertexStrengthByAgent.put(vertex, 
							relationshipTracker.vertexStrength(vertex));
		}
	}
	
	public void computeDiameter() {
		Graph<Agent, WeightedEdge> graph = getGraph();
		this.diameter = DistanceStatistics.diameter(graph,
				new UnweightedShortestPath<Agent, WeightedEdge>(graph), true);
	}
	
	public void computeAveragePathLength() {
		SummaryStatistics distanceStats = new SummaryStatistics();
		Graph<Agent, WeightedEdge> graph = getGraph();
		Transformer<Agent,Double> distances = 
			DistanceStatistics.averageDistances(graph, 
					new UnweightedShortestPath<Agent, WeightedEdge>(graph));
		for(Agent agent : graph.getVertices()) {
			double d = distances.transform(agent);
			distanceStats.addValue(d);
		}
		this.averagePathLength = distanceStats.getMean();
	}

	public void computeClusteringStats() {
		clusteringStats = new SummaryStatistics();
		for (Agent vertex : getGraph().getVertices()) {
			if (degree(vertex) > 0) {
				double clusteringCoefficient = 
						computeWeightedClusteringCoefficient(vertex);
				clusteringStats.addValue(clusteringCoefficient);
				logger.debug("cc(" + vertex.hashCode() + ") = "
						+ clusteringCoefficient);
			}
		}
	}
	
	public void computeTriads() {
		this.triadCounts = 
				TriadicCensus.getCounts(
						(DirectedGraph<Agent, WeightedEdge>) getGraph());
	}
	
	public double outDegree(Agent vertex) {
		return relationshipTracker.outDegree(vertex);
	}
	
	public double degree(Agent vertex) {
		return relationshipTracker.outDegree(vertex) + 
					relationshipTracker.inDegree(vertex);
	}
	
	public double vertexStrength(Agent vertex) {
		return relationshipTracker.vertexStrength(vertex);
	}
	
	public double edgeStrength(Agent i, Agent j) {
		return relationshipTracker.edgeStrength(i, j)
				+ relationshipTracker.edgeStrength(j, i);
	}
	
	public double computeWeightedClusteringCoefficient(Agent vertex) {
		double c = 0.0;
		ArrayList<Agent> neighbors = new ArrayList<Agent>(relationshipTracker
				.getGraph().getNeighbors(vertex));
		int n = neighbors.size();
		if (n < 2) {
			return 0.0;
		}
		for (int i = 0; i < n; i++) {
			Agent w = neighbors.get(i);
			for (int j = 0; j < n; j++) {
				if (i != j) {
					Agent x = neighbors.get(j);
					if (relationshipTracker.getGraph().isNeighbor(w, x)) {
						c += (edgeStrength(vertex, w) + edgeStrength(vertex, x)) / 2.0;
					}
				}
			}
		}
		double totalStrength = vertexStrength(vertex);
		if (totalStrength < 10E-10) {
			return 0.0;
		}
		double result = c / (totalStrength * (n - 1));
		if (Double.isNaN(result)) {
			logger.error(result);
		}
		return result;
	}

	public RelationshipTracker getRelationshipTracker() {
		return relationshipTracker;
	}

	public void setRelationshipTracker(RelationshipTracker relationshipTracker) {
		this.relationshipTracker = relationshipTracker;
	}

	public void reset(SimulationEvent event) {
		// TODO Auto-generated method stub
	}

	public Map<Agent, Double> getClusteringCoefficients() {
		return clusteringCoefficients;
	}

	public SummaryStatistics getClusteringStats() {
		return clusteringStats;
	}

	
	   /**
     * Returns a <code>Map</code> of vertices to their clustering coefficients.
     * The clustering coefficient cc(v) of a vertex v is defined as follows:
     * <ul>
     * <li/><code>degree(v) == {0,1}</code>: 0
     * <li/><code>degree(v) == n, n &gt;= 2</code>: given S, the set of neighbors
     * of <code>v</code>: cc(v) = (the sum over all w in S of the number of 
     * other elements of w that are neighbors of w) / ((|S| * (|S| - 1) / 2).
     * Less formally, the fraction of <code>v</code>'s neighbors that are also
     * neighbors of each other. 
     * <p><b>Note</b>: This algorithm treats its argument as an undirected graph;
     * edge direction is ignored. 
     * @param graph the graph whose clustering coefficients are to be calculated
     * @see "The structure and function of complex networks, M.E.J. Newman, aps.arxiv.org/abs/cond-mat/0303516"
     */
    public static <V,E> Map<V, Double> clusteringCoefficients(Graph<V,E> graph)
    {
        Map<V,Double> coefficients = new HashMap<V,Double>();
        
        for (V v : graph.getVertices())
        {
            int n = graph.getNeighborCount(v);
            if (n < 2)
                coefficients.put(v, new Double(0));
            else
            {
                // how many of v's neighbors are connected to each other?
                ArrayList<V> neighbors = new ArrayList<V>(graph.getNeighbors(v));
                double edge_count = 0;
                for (int i = 0; i < n; i++)
                {
                    V w = neighbors.get(i);
                    for (int j = i+1; j < n; j++ )
                    {
                        V x = neighbors.get(j);
                        edge_count += graph.isNeighbor(w, x) ? 1 : 0;
                    }
                }
                double possible_edges = (n * (n - 1))/2.0;
                coefficients.put(v, new Double(edge_count / possible_edges));
            }
        }
        
        return coefficients;
    }
    
    public static <V, E> int triangles(Graph<V, E> graph, V v) {
    	int n = graph.getNeighborCount(v);
    	ArrayList<V> neighbors = new ArrayList<V>(graph.getNeighbors(v));
        int edge_count = 0;
        for (int i = 0; i < n; i++)
        {
            V w = neighbors.get(i);
            for (int j = i+1; j < n; j++ )
            {
                V x = neighbors.get(j);
                edge_count += graph.isNeighbor(w, x) ? 1 : 0;
            }
        }
    	return edge_count;
    }
    
}
