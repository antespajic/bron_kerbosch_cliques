package hr.fer.projekt.clique.utility;

import java.util.*;

/**
 * Simple utility for collection manipulation which offers
 * set of basic operations while ensuring that original
 * collections are unmodified.
 */
public class CollectionUtility {

    /**
     * Forms union collection from two given collections. Resulting
     * collections will not have duplicates.
     *
     * @param firstCollection  first collection
     * @param secondCollection second collection
     * @param <V>              type parameter of given collections
     * @return resulting union collection
     */
    public static <V> Collection<V> union(Collection<V> firstCollection, Collection<V> secondCollection) {
        Set<V> union = new HashSet<>();
        union.addAll(firstCollection);
        union.addAll(secondCollection);
        return union;
    }

    /**
     * Forms collection which consists of elements that are present
     * in both passed collections.
     *
     * @param firstCollection  first collection
     * @param secondCollection second collection
     * @param <V>              type parameter of given collections
     * @return resulting collection with elements on 'intersection'
     */
    public static <V> Collection<V> intersection(Collection<V> firstCollection, Collection<V> secondCollection) {
        List<V> intersection = new ArrayList<V>();
        for (V element : firstCollection) {
            if (secondCollection.contains(element)) {
                intersection.add(element);
            }
        }
        return intersection;
    }

    /**
     * Forms collection which consists of all elements from first
     * collection which are not present in the second collection.
     *
     * @param firstCollection  first collection
     * @param secondCollection second collection
     * @param <V>              type parameter of given collections
     * @return resulting partition
     */
    public static <V> Collection<V> removeAll(Collection<V> firstCollection, Collection<V> secondCollection) {
        List<V> partition = new ArrayList<V>();
        partition.addAll(firstCollection);
        partition.removeAll(secondCollection);
        return partition;
    }
}
