package graph.isomorphism.invariants;

import graph.isomorphism.graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DegreeSequence extends Invariant<String> {

    private static final String INVARIANT_NAME = "Degree Sequence";

    public DegreeSequence() {
        super(String.class);
    }

    @Override
    public String computeInvariantValue(Graph graph) {
        Set<Integer> nodes = graph.getNodes();

        List<Integer> degreeSequence = new ArrayList<>();

        for (int node: nodes) {
            degreeSequence.add(graph.getAdjacentNodes(node).size());
        }

        Collections.sort(degreeSequence);
        String degreeSequenceStr = "";
        for (int degree : degreeSequence) {
            degreeSequenceStr = degreeSequenceStr + " " + degree;
        }

        return degreeSequenceStr;
    }

    @Override
    public String getInvariantName() {
        return INVARIANT_NAME;
    }
}
