package graph.isomorphism.invariants;

import graph.isomorphism.graph.Graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BalabanIndex extends Invariant<Double> {

    private static final String INVARIANT_NAME = "Balaban Index";

    public BalabanIndex() {
        super(Double.class);
    }

    @Override
    public Double computeInvariantValue(Graph graph) {

        double[][] distanceMatrix = graph.getDistanceMatrix();

        double circuitRank = graph.getNumOfEdges() - graph.getNumOfNodes() + graph.getConnectedComponents().size();

        Map<Integer, Double> distanceSums = new HashMap<>();

        Set<Integer> nodes = graph.getNodes();

        for (int node: nodes) {
            double distanceSum = 0;
            for (double distance : distanceMatrix[node]) {
                distanceSum += distance;
            }
            distanceSums.put(node, distanceSum);
        }

        double index = 0;
        for (int u : nodes) {
            for (int v : nodes) {
                if(u != v && graph.isEdge(u, v)) {
                    index += 1/(Math.sqrt(distanceSums.get(u) * distanceSums.get(v)));
                }
            }
        }

        double coefficient =  ((double)graph.getNumOfEdges() / (circuitRank + 1));

        return (coefficient*index*0.5);
    }

    @Override
    public String getInvariantName() {
        return INVARIANT_NAME;
    }
}
