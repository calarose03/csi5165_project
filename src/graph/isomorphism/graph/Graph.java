package graph.isomorphism.graph;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    private final LinkedHashMap<Integer, ArrayList<Integer>> adjacencyList;
    private LinkedHashMap<Integer, Graph> subgraphs;
    private List<ConnectedComponent> connectedComponents;
    private double[][] distanceMatrix;
    private double[][] adjacencyMatrix;
    private int numOfNodes;
    private int numOfEdges;
    // For subgraphs to remember how many nodes there actually are in the original graph.
    private int n;

    public Graph(String filename) throws IOException {
        this.adjacencyList = new LinkedHashMap<>();
        this.connectedComponents = new ArrayList<>();
        buildAdjacencyList(filename);
        buildSubGraphs();
    }

    // Used only for subgraphs
    public Graph(LinkedHashMap<Integer, ArrayList<Integer>> adjacencyList, int n) {
        this.adjacencyList = adjacencyList;
        this.subgraphs = null;
        this.numOfNodes = adjacencyList.size();
        this.n = n;


        ConnectedComponent connectedComponent = new ConnectedComponent();
        numOfEdges = 0;
        for (int node : adjacencyList.keySet()) {
            connectedComponent.addNode(node);
            numOfEdges += adjacencyList.get(node).size();
        }
        this.connectedComponents = new ArrayList<>();
        connectedComponents.add(connectedComponent);
        numOfEdges  = numOfEdges / 2;
    }

    public Graph getSubgraph(int node) {
        return subgraphs.get(node);
    }

    public int getNumOfNodes() {
        return numOfNodes;
    }

    public int getNumOfEdges() {
        return numOfEdges;
    }

    public double[][] getDistanceMatrix() {
        if (distanceMatrix == null) {
            computeDistanceMatrix();
        }
        return distanceMatrix;
    }

    public double[][] getAdjacencyMatrix(){
        if (adjacencyMatrix == null) {
            computeAdjacencyMatrix();
        }
        return adjacencyMatrix;
    }

    public List<ConnectedComponent> getConnectedComponents() {
        if (connectedComponents == null) {
            computeConnectedComponents();
        }
        return connectedComponents;
    }

    public void addEdge(int u, int v) {
        adjacencyList.get(u).add(v);
        adjacencyList.get(v).add(u);
    }

    public void removeEdge(int u, int v) {
        adjacencyList.get(u).remove(v);
        adjacencyList.get(v).remove(u);
    }

    public Set<Integer> getNodes() {
        return adjacencyList.keySet();
    }

    public boolean isEdge(int u, int v) {
        return adjacencyList.get(u).contains(v);
    }

    public ArrayList<Integer> getAdjacentNodes(int u) {
        return adjacencyList.get(u);
    }

    private void buildAdjacencyList(String filename) throws IOException {

        FileReader fileReader = new FileReader(filename);
        BufferedReader stdin = new BufferedReader(fileReader);

        String fileLine = stdin.readLine();
        String[] lineValues = fileLine.split(" ");

        numOfEdges = Integer.parseInt(lineValues[lineValues.length-1]);
        for (int i = 0; i < numOfEdges; i++) {
            fileLine = stdin.readLine();
            lineValues = fileLine.split(" ");

            Set<Integer> keyset = adjacencyList.keySet();
            int u = Integer.parseInt(lineValues[1]);
            int v = Integer.parseInt(lineValues[2]);

            if (!keyset.contains(u)) {
                adjacencyList.put(u, new ArrayList<>());
            }
            adjacencyList.get(u).add(v);

            if (!keyset.contains(v)) {
                adjacencyList.put(v, new ArrayList<>());
            }
            adjacencyList.get(v).add(u);
        }

        numOfNodes = adjacencyList.size();
    }

    private void buildSubGraphs() {
        subgraphs = new LinkedHashMap<>();
        for (int node : getNodes()) {
            LinkedHashMap<Integer, ArrayList<Integer>> nodeAdjacencyList = new LinkedHashMap<>();
            nodeAdjacencyList.put(node, adjacencyList.get(node));
            for (int u : adjacencyList.get(node)) {
                ArrayList<Integer> neighbours = new ArrayList<>();
                neighbours.add(node);
                for (int v: adjacencyList.get(node)) {
                    if (isEdge(u,v)) {
                        neighbours.add(v);
                    }
                }
                nodeAdjacencyList.put(u, neighbours);
            }
            subgraphs.put(node, new Graph(nodeAdjacencyList, numOfNodes));
        }
    }

    private void computeDistanceMatrix() {
        Set<Integer> nodes = getNodes();
        distanceMatrix = new double[n][n];

        for (double[] row: distanceMatrix){
            Arrays.fill(row, 0);
        }

        for (int node: nodes) {
            for (int neighbour: getAdjacentNodes(node)) {
                if (node != neighbour && distanceMatrix[node][neighbour] != 1) {
                    distanceMatrix[node][neighbour] = 1;
                    distanceMatrix[neighbour][node] = 1;
                }
            }
        }

        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (distanceMatrix[j][k] > distanceMatrix[j][i] + distanceMatrix[i][k]) {
                        distanceMatrix[j][k] = distanceMatrix[j][i] + distanceMatrix[i][k];
                    }
                }
            }
        }
        //Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(distanceMatrix);
        //System.out.println("Graph: " + getNodes());
        //System.out.println("Distance matrix\n" + matrix.toString());
    }

    private void computeAdjacencyMatrix() {
        Set<Integer> nodes = getNodes();
        adjacencyMatrix = new double[n][n];

        for (int node: nodes) {
            for (int neighbour : getAdjacentNodes(node)) {
                if (node != neighbour && adjacencyMatrix[node][neighbour] != 1) {
                    adjacencyMatrix[node][neighbour] = 1;
                    adjacencyMatrix[neighbour][node] = 1;
                }
            }
        }
    }

    private void computeConnectedComponents(){
        Set<Integer> nodes = getNodes();
        int maxNode = Collections.max(nodes);
        boolean[] visited = new boolean[maxNode+1];

        for (int node: nodes) {
            if (!visited[node]) {
                connectedComponents.add(DFS(new ConnectedComponent(), node, visited));
            }
        }

    }

    private ConnectedComponent DFS(ConnectedComponent connectedComponent, int node, boolean[] visited) {
        connectedComponent.addNode(node);
        visited[node] = true;

        for (int neighbour: getAdjacentNodes(node)) {
            if (!visited[neighbour]) {
                DFS(connectedComponent, neighbour, visited);
            }
        }
        return connectedComponent;
    }


}
