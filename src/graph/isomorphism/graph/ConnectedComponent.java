package graph.isomorphism.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ConnectedComponent {

    private int size = 0;
    private Set<Integer> nodes = new HashSet<>();

    public void addNode(int node) {
        nodes.add(node);
        size++;
    }

    public int getSize() {
        return size;
    }

    public Set<Integer> getNodes() {
        return nodes;
    }
}
