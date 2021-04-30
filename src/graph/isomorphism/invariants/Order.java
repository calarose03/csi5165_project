package graph.isomorphism.invariants;

import graph.isomorphism.graph.Graph;

public class Order extends Invariant<Integer> {

    private static final String INVARIANT_NAME = "Order";

    public Order() {
        super(Integer.class);
    }

    @Override
    public Integer computeInvariantValue(Graph graph) {
        return graph.getNumOfNodes();
    }

    @Override
    public String getInvariantName() {
        return INVARIANT_NAME;
    }
}
