package hr.fer.projekt.clique.utility;

import java.util.*;

public class CollectionUtility {

    public static <V> Collection<V> union(Collection<V> firstCollection, Collection<V> secondCollection) {
        Set<V> union = new HashSet<>();
        union.addAll(firstCollection);
        union.addAll(secondCollection);
        return union;
    }

    public static <V> Collection<V> intersection(Collection<V> firstCollection, Collection<V> secondCollection) {
        List<V> intersection = new ArrayList<V>();
        for (V element : firstCollection) {
            if (secondCollection.contains(element)) {
                intersection.add(element);
            }
        }
        return intersection;
    }

    public static <V> Collection<V> removeAll(Collection<V> firstCollection, Collection<V> secondCollection) {
        List<V> partition = new ArrayList<V>();
        partition.addAll(firstCollection);
        partition.removeAll(secondCollection);
        return partition;
    }
}
