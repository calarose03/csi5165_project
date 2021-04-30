package graph.isomorphism;

import graph.isomorphism.graph.Graph;
import graph.isomorphism.invariants.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RunGraphIsomorphism {

    public static void main(String[] args) throws IOException {
        String filepath = new File("").getAbsolutePath();
        String filepathX = filepath.concat("\\data\\" + args[0]);
        String filepathY = filepath.concat("\\data\\" + args[1]);

        Graph graphX = new Graph(filepathX);
        Graph graphY = new Graph(filepathY);

        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter how many invariants to consider (from 1 to 8):");
        int numOfInvariants = scan.nextInt();
        while (numOfInvariants > 8 || numOfInvariants < 1) {
            System.out.println("Please enter how many invariants to consider (from 1 to 8):");
            numOfInvariants = scan.nextInt();
        }

        ArrayList<Integer> invariants = new ArrayList<>();
        System.out.println("Please enter the numbers corresponding to the invariants you'd like.");
        System.out.println("1 - Order (number of vertices)");
        System.out.println("2 - Size (number of edges)");
        System.out.println("3 - Diameter (length of longest simple path)");
        System.out.println("4 - Degree Sequence");
        System.out.println("5 - Circuit Rank (minimum number of edges to remove to have no cycles)");
        System.out.println("6 - Spectrum (eigenvalue set of the graph)");
        System.out.println("7 - Wiener Index");
        System.out.println("8 - Balaban Index");
        for (int i = 0 ; i < numOfInvariants; i++) {
            System.out.println("Pick an invariant (" + (i+1) + "/" + numOfInvariants +"): ");
            int invariant = scan.nextInt();
            if (!invariants.contains(invariant)) {
                invariants.add(invariant);
            }
        }

        List<Invariant> invariantList = buildInvariantList(invariants);

        GraphIsomorphism graphIsomorphism = new GraphIsomorphism(graphX, graphY);
        List<Integer> assignment = graphIsomorphism.checkIsomorphism(invariantList);

        String invariantStr = "";
        for (Invariant invariant : invariantList) {
            invariantStr = invariantStr + " | " + invariant.getInvariantName();
        }

        System.out.println("Invariant(s) applied in order: " + invariantStr);

        for(Invariant invariant : invariantList) {
            if (invariant instanceof Spectrum) {
                Spectrum spectrumInvariant = (Spectrum) invariant;
                if ( spectrumInvariant.isFAILED()) {
                    System.out.println("Computation of spectrum failed, do not count results.\n\n");
                    return;
                }
            }
        }

        if (assignment == null && graphIsomorphism.getFailedInvariant() != null) {
            System.out.println("Invariant [" + graphIsomorphism.getFailedInvariant() + "] couldn't partition the graphs equally.\n");
        } else if (assignment == null) {
            System.out.println("Could not map the vertices.\n");
        } else if (assignment.size() == 1) {

            if (assignment.get(0) == 0) {
                System.out.println("The partition refinement could not divide the vertices.");
            } else {
                System.out.println("Ran out of time while mapping the vertices (tree is too big)");
            }
        } else {
            String assignStr = "";
            for (int i = 0 ; i < assignment.size(); i++) {
                assignStr = assignStr + i + " -> " + assignment.get(i) + "\n";
            }
            System.out.println("Assignment (X -> Y)\n" + assignStr);
        }

    }

    private static List<Invariant> buildInvariantList(List<Integer> invariants) {
        ArrayList<Invariant> invariantList = new ArrayList<>();

        for (int invariant: invariants) {
            switch(invariant) {
                case 1:
                    invariantList.add(new Order());
                    break;
                case 2:
                    invariantList.add(new Size());
                    break;
                case 3:
                    invariantList.add(new Diameter());
                    break;
                case 4:
                    invariantList.add(new DegreeSequence());
                    break;
                case 5:
                    invariantList.add(new CircuitRank());
                    break;
                case 6:
                    invariantList.add(new Spectrum());
                    break;
                case 7:
                    invariantList.add(new WienerIndex());
                    break;
                case 8:
                    invariantList.add(new BalabanIndex());
                    break;
            }
        }
        return invariantList;
    }
}
