package graph.isomorphism;

import graph.isomorphism.graph.Graph;
import graph.isomorphism.invariants.*;

import java.io.*;
import java.util.*;

public class RunGraphIsomorphismTest {
    private static final String FILEPATH = new File("").getAbsolutePath();
    private static final String INVARIANTS = FILEPATH.concat("\\data\\invariant_lists.txt");

    public static void main(String[] args) throws IOException {
        String dirPath =  FILEPATH.concat("\\dataset\\");
        File dir = new File(dirPath);

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            FileReader fileReader = new FileReader(file);
            BufferedReader stdin = new BufferedReader(fileReader);
            String fileLine;
            while ((fileLine = stdin.readLine()) !=null && fileLine.trim().length()>0) {
                System.out.println(fileLine);
                String[] lineValues = fileLine.split(" ");
                String filepathX = FILEPATH.concat("\\data\\" + lineValues[0] + ".txt");
                String filepathY = FILEPATH.concat("\\data\\" + lineValues[1] + ".txt");

                Graph graphX = new Graph(filepathX);
                Graph graphY = new Graph(filepathY);
                computeIsomorphism(lineValues[0] + "_" + lineValues[1] + "_results.txt",graphX, graphY);
                System.out.println("Over\n");
            }
        }
    }

    private static void computeIsomorphism(String resultFilename, Graph graphX, Graph graphY) throws IOException {

        FileReader fileReader = new FileReader(INVARIANTS);
        BufferedReader stdin = new BufferedReader(fileReader);
        String fileLine;
        while ((fileLine = stdin.readLine()) != null && fileLine.trim().length() > 0) {
            int[] lineValues = Arrays.stream(fileLine.split(" ")).mapToInt(Integer::parseInt).toArray();
            List<Invariant> invariantList = buildInvariantList(lineValues);
            GraphIsomorphism graphIsomorphism = new GraphIsomorphism(graphX, graphY);
            List<Integer> assignment = graphIsomorphism.checkIsomorphism(invariantList);
            printIsomorphismResults(resultFilename, assignment, invariantList, graphIsomorphism.getFailedInvariant(), graphX.getNumOfNodes());
        }
    }

    private static void printIsomorphismResults(String filename, List<Integer> assignment, List<Invariant> invariants, String failedInvariant, int numOfNodes) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
        String invariantStr = "";
        for (Invariant invariant : invariants) {
            invariantStr = invariantStr + " | " + invariant.getInvariantName();
        }
        writer.append("Invariant(s) applied in order: ").append(invariantStr).append("\n");

        for(Invariant invariant : invariants) {
            if (invariant instanceof Spectrum) {
                Spectrum spectrumInvariant = (Spectrum) invariant;
                if ( spectrumInvariant.isFAILED()) {
                    writer.append("Computation of spectrum failed, do not count results.\n\n");
                    writer.close();
                    return;
                }
            }
        }

        if (assignment == null && failedInvariant != null) {
            writer.append("Invariant [").append(failedInvariant).append("] couldn't partition the graphs equally.\n");
        } else if (assignment == null) {
            writer.append("Could not map the vertices.\n");
        } else if (assignment.size() == 1) {

            if (assignment.get(0) == 0) {
                writer.append("The partition refinement could not divide the vertices.");
            } else {
                writer.append("Ran out of time while mapping the vertices (tree is too big)");
            }
        } else {
            String assignStr = "";
            for (int i = 0 ; i < assignment.size(); i++) {
                assignStr = assignStr + i + " -> " + assignment.get(i) + "\n";
            }
            writer.append("Assignment (X -> Y)\n").append(assignStr);
        }
        writer.append("\n\n");
        writer.close();
    }

    private static List<Invariant> buildInvariantList(int[] invariants) {
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
