package graph.isomorphism.invariants;

import graph.isomorphism.graph.Graph;

import java.util.Arrays;

public class Diameter extends Invariant<Integer> {

    private static final String INVARIANT_NAME = "Diameter";

    public Diameter() {
        super(Integer.class);
    }

    @Override
    public Integer computeInvariantValue(Graph graph) {

        double[][] distanceMatrix = graph.getDistanceMatrix();

        double max = 0;
        for (double[] row: distanceMatrix) {
            double tempMax = Arrays.stream(row).max().getAsDouble();

            if (tempMax > max) {
                max = tempMax;
            }
        }

        return (int) max;
    }

    @Override
    public String getInvariantName() {
        return INVARIANT_NAME;
    }
}
