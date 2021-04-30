package graph.isomorphism.invariants;

import graph.isomorphism.graph.Graph;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.linear.*;

import java.math.BigDecimal;
import java.util.*;

public class Spectrum extends Invariant<String> {

    private static final String INVARIANT_NAME = "Spectrum";
    private static boolean FAILED = false;

    public Spectrum() {
        super(String.class);
    }

    public boolean isFAILED() {
        return FAILED;
    }

    @Override
    public String computeInvariantValue(Graph graph) {

        if (FAILED) {
            return "";
        }
        double[][] adjacencyMatrix = graph.getAdjacencyMatrix();

        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(adjacencyMatrix);
        EigenDecomposition eigenDecomposition;
        try {
            eigenDecomposition = new EigenDecomposition(matrix);
        } catch (Exception e) {
            System.out.println("Couldn't compute spectrum. Returning empty string");
            FAILED = true;
            return "";
        }

        double[] eigenvalues = eigenDecomposition.getRealEigenvalues();

        ArrayList<Double> spectrum = new ArrayList<>();

        for (double eigenvalue : eigenvalues) {
            spectrum.add(BigDecimal.valueOf(eigenvalue).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
        }

        Collections.sort(spectrum);

        StringBuilder spectrumStr = new StringBuilder();
        for (double value : spectrum) {
            spectrumStr.append(" ").append(value);
        }
        return spectrumStr.toString();
    }

    @Override
    public String getInvariantName() {
        return INVARIANT_NAME;
    }
}
