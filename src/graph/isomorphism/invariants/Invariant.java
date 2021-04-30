package graph.isomorphism.invariants;

import graph.isomorphism.graph.Graph;

import java.util.List;

public abstract class Invariant<E> {

    private final Class<E> type;

    public Invariant(Class<E> type) {
        this.type = type;
    }

    public Class<E> getType() {
        return type;
    }

    public abstract E computeInvariantValue(Graph graph);

    public abstract String getInvariantName();



}
