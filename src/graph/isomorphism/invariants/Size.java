package graph.isomorphism.invariants;


import graph.isomorphism.graph.Graph;

public class Size extends Invariant<Integer> {

    private static final String INVARIANT_NAME = "Size";

    public Size() {
        super(Integer.class);
    }

    @Override
    public Integer computeInvariantValue(Graph graph) {
        return graph.getNumOfEdges();
    }

    @Override
    public String getInvariantName() {
        return INVARIANT_NAME;
    }
}
