package graph.isomorphism;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class SetSizeComparator implements Comparator<Set<Integer>> {

    @Override
    public int compare(Set<Integer> o1, Set<Integer> o2) {
        return Integer.compare(o1.size(), o2.size());
    }
}
