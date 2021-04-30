package graph.isomorphism.invariants;

import graph.isomorphism.graph.Graph;

import java.util.Set;

public class WienerIndex extends Invariant<Integer> {

    private static final String INVARIANT_NAME = "Wiener Index";

    public WienerIndex() {
        super(Integer.class);
    }

    @Override
    public Integer computeInvariantValue(Graph graph) {

        double[][] distanceMatrix = graph.getDistanceMatrix();

        double sum = 0;
        Set<Integer> nodes = graph.getNodes();
        for (int u : nodes) {
            for (int v : nodes) {
                sum += distanceMatrix[u][v];
            }
        }
        return (int) sum/2;
    }

    @Override
    public String getInvariantName() {
        return INVARIANT_NAME;
    }
}
