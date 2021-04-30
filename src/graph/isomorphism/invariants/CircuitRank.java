package graph.isomorphism.invariants;

import graph.isomorphism.graph.Graph;

public class CircuitRank extends Invariant<Integer> {

    private static final String INVARIANT_NAME = "Circuit Rank";

    public CircuitRank() {
        super(Integer.class);
    }

    @Override
    public Integer computeInvariantValue(Graph graph) {
        return graph.getNumOfEdges() - graph.getNumOfNodes() + graph.getConnectedComponents().size();
    }

    @Override
    public String getInvariantName() {
        return INVARIANT_NAME;
    }
}
