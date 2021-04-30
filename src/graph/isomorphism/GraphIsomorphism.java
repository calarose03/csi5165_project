package graph.isomorphism;

import graph.isomorphism.graph.Graph;
import graph.isomorphism.invariants.Invariant;

import java.util.*;

public class GraphIsomorphism {

    private final Graph graphX;
    private final Graph graphY;

    private List<Set<Integer>> partitionsX;
    private List<Set<Integer>> partitionsY;

    private final Map<Integer,Integer> W;
    private List<Integer> f;

    private final int MAX_ITER = 10000000;
    private int iter = 0;

    private String failedInvariant;

    public GraphIsomorphism(Graph graphX, Graph graphY) {
        this.graphX = graphX;
        this.graphY = graphY;
        this.partitionsX = new ArrayList<>();
        this.partitionsY = new ArrayList<>();
        this.W = new HashMap<>();
    }

    public String getFailedInvariant() {
        return failedInvariant;
    }

    public List<Integer> checkIsomorphism(List<Invariant> invariants) {

        int N = buildPartitions(invariants);
        if (N == 0) {
            return null;
        }

        /* The vertex sets weren't refined into partitions, and thus the number of bijections is too big.*/
        if (partitionsX.size() == 1) {
            return Collections.singletonList(0);
        }

        for (int i = 0; i < partitionsX.size(); i++) {
            for (int x : partitionsX.get(i)) {
                W.put(x, i);
            }
        }

        findIsomorphism(0, new ArrayList<>());
        /* The backtrack tree is too big to compute in a short amount of time.*/
        if (iter > MAX_ITER) {
            return Collections.singletonList(1);
        }
        return f;
    }

    /*Refine the vertex sets with the invariant inducing functions.*/
    public Integer buildPartitions(List<Invariant> invariants){
        Set<Integer> nodesX = new HashSet<>(graphX.getNodes());
        Set<Integer> nodesY = new HashSet<>(graphY.getNodes());

        Set<Integer> initialPartitionX = new HashSet<>(nodesX);
        Set<Integer> initialPartitionY = new HashSet<>(nodesY);
        partitionsX.add(initialPartitionX);
        partitionsY.add(initialPartitionY);
        int N = 1;
        for (Invariant invariant: invariants) {
            int m = 0;
            ArrayList<Set<Integer>> newPartitionX = new ArrayList<>();
            ArrayList<Set<Integer>> newPartitionY = new ArrayList<>();
            for(int i = 0; i < N; i++) {
                ArrayList<Set<Integer>> newPartX = buildPartitionsX(invariant, i);
                ArrayList<Set<Integer>> tempPartY = buildPartitionsY(invariant, i);
                if (newPartX.size() != tempPartY.size()) {
                    failedInvariant = invariant.getInvariantName();
                    //System.out.println("Invariant [" + failedInvariant + "] couldn't build parts of the same sizes.");
                    return 0;
                }
                ArrayList<Set<Integer>> newPartY = new ArrayList<>();
                for (Set<Integer> partX : newPartX) {
                    int nodeX = partX.iterator().next();
                    boolean found = false;
                    ArrayList<Set<Integer>> newParts = new ArrayList<>();
                    for (Set<Integer> partY : tempPartY) {
                        int nodeY = partY.iterator().next();
                        if (invariant.computeInvariantValue(graphX.getSubgraph(nodeX)).equals(invariant.computeInvariantValue(graphY.getSubgraph(nodeY)))) {
                            if (partX.size() == partY.size()) {
                                found = true;
                                newParts.add(new HashSet<>(partY));
                                break;
                            }
                        }
                    }
                    newPartY.addAll(newParts);
                    tempPartY.removeAll(newParts);
                    if (!found) {
                        failedInvariant = invariant.getInvariantName();
                        //System.out.println("Invariant [" + failedInvariant + "] couldn't reorganize new parts Y");
                        return 0;
                    }
                }
                newPartitionX.addAll(newPartX);
                newPartitionY.addAll(newPartY);
                m += newPartX.size()-1;
            }
            partitionsX = new ArrayList<>(newPartitionX);
            partitionsY = new ArrayList<>(newPartitionY);
            partitionsX.sort(new SetSizeComparator());
            partitionsY.sort(new SetSizeComparator());
            N = N + m;
        }
        return N;
    }

    /*Find a possible mapping with the refined vertex sets.*/
    public void findIsomorphism(int l, List<Integer> f) {
        if (this.f != null || iter >MAX_ITER) {
            return;
        }

        if (l == graphX.getNumOfNodes()) {
            this.f = new ArrayList<>(f);
            //System.out.println(f);
            return;
        }

        int j = W.get(l);
        for (int y : partitionsY.get(j)) {
            boolean matched = true;
            for (int i = 0; i < l ; i++) {
                if ((graphX.isEdge(i,l) && !graphY.isEdge(f.get(i), y))
                        || (!graphX.isEdge(i,l) && graphY.isEdge(f.get(i), y))) {
                    matched = false;
                }
            }
            if (matched && !f.contains(y)) {
                List<Integer> newF = new ArrayList<>(f);
                newF.add(y);
                iter++;
                findIsomorphism(l+1, newF);
            }
        }
    }

    private ArrayList<Set<Integer>> buildPartitionsX(Invariant invariant, int i) {
        ArrayList<Set<Integer>> newPartition = new ArrayList<>();
        Set<Integer> nodeSet = new HashSet<>(partitionsX.get(i));

        while (!nodeSet.isEmpty()) {
            int first = nodeSet.iterator().next();
            nodeSet.remove(first);
            Set<Integer> part = new HashSet<>();
            part.add(first);
            for (int node : nodeSet) {
                if (invariant.computeInvariantValue(graphX.getSubgraph(first)).equals(invariant.computeInvariantValue(graphX.getSubgraph(node)))) {
                    part.add(node);
                }
            }
            nodeSet.removeAll(part);
            newPartition.add(part);
        }

        return newPartition;
    }

    private ArrayList<Set<Integer>> buildPartitionsY(Invariant invariant, int i) {
        ArrayList<Set<Integer>> newPartition = new ArrayList<>();
        Set<Integer> nodeSet = new HashSet<>(partitionsY.get(i));

        while (!nodeSet.isEmpty()) {
            int first = nodeSet.iterator().next();
            nodeSet.remove(first);
            Set<Integer> part = new HashSet<>();
            part.add(first);
            for (int node : nodeSet) {
                if (invariant.computeInvariantValue(graphY.getSubgraph(first)).equals(invariant.computeInvariantValue(graphY.getSubgraph(node)))) {
                    part.add(node);
                }
            }
            nodeSet.removeAll(part);
            newPartition.add(part);
        }

        return newPartition;
    }


}
