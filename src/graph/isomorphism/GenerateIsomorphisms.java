package graph.isomorphism;

import graph.isomorphism.graph.Graph;

import java.io.*;
import java.util.*;

public class GenerateIsomorphisms {

    private static final String FILEPATH = new File("").getAbsolutePath();

    public static void main(String[] args) throws IOException {
        String filename =  FILEPATH.concat("\\data\\filesToGenerateIsos.txt");

        FileReader fileReader = new FileReader(filename);
        BufferedReader stdin = new BufferedReader(fileReader);

        String fileLine;
        while ((fileLine = stdin.readLine()) !=null && fileLine.trim().length()>0) {
            String permFile =  FILEPATH.concat("\\data\\perm_"+fileLine);
            Map<Integer, Integer> perm = getPerm(permFile);


            String filepath2 = FILEPATH.concat("\\data\\" + fileLine);
            FileReader fileReader2 = new FileReader(filepath2);
            BufferedReader stdin2 = new BufferedReader(fileReader2);
            String fileLine2 = stdin2.readLine();
            String lineValues[] = fileLine2.split(" ");
            System.out.println(fileLine2);

            int numOfEdges = Integer.parseInt(lineValues[lineValues.length-1]);
            BufferedWriter writer = new BufferedWriter(new FileWriter("new_"+fileLine, true));
            writer.append(fileLine2).append("\n");
            for (int i = 0; i < numOfEdges; i++) {
                fileLine2 = stdin2.readLine();
                lineValues = fileLine2.split(" ");
                int u = Integer.parseInt(lineValues[1]);
                int v = Integer.parseInt(lineValues[2]);
                System.out.println(fileLine2);
                int new_u = perm.get(u);
                int new_v = perm.get(v);
                writer.append("e ").append(String.valueOf(new_u)).append(" ").append(String.valueOf(new_v)).append("\n");
            }
            writer.close();

        }
    }

    private static Map<Integer, Integer> getPerm(String filename) throws IOException {

        Map<Integer, Integer> perm = new HashMap<>();

        FileReader fileReader = new FileReader(filename);
        BufferedReader stdin = new BufferedReader(fileReader);
        String fileLine;
        while ((fileLine = stdin.readLine()) !=null && fileLine.trim().length()>0) {
            String[] lineValues = fileLine.split(" ");
            int u = Integer.parseInt(lineValues[0]);
            int v = Integer.parseInt(lineValues[1]);
            perm.put(u,v);
        }
        return perm;
    }
}
