package de.tudortmund.cs.iltis.utils.collections;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Provides operations on sets such as union, intersection, complement, etc. */
public class SetOperations {

    /**
     * Computes the complement of the given set regarding a given superset.
     *
     * @param set the set to get the complement from
     * @param supersetIterator iterator over the elements of the superset
     * @param <T> the type of elements the sets contain
     * @return the complement of the given set regarding the given superset
     */
    public static <T> Set<T> getComplement(Iterator<T> supersetIterator, Set<T> set) {
        Set<T> superset =
                StreamSupport.stream(
                                Spliterators.spliteratorUnknownSize(
                                        supersetIterator, Spliterator.ORDERED),
                                false)
                        .collect(Collectors.toSet());
        return getComplement(superset, set);
    }

    /**
     * Computes the complement of the given set regarding a given superset.
     *
     * @param set the set to get the complement from
     * @param superset the superset
     * @param <T> the type of elements the sets contain
     * @return the complement of the given set regarding the given superset
     */
    public static <T> Set<T> getComplement(Set<T> superset, Set<T> set) {
        return superset.stream()
                .filter(element -> !set.contains(element))
                .collect(Collectors.toSet());
    }

    /**
     * Computes the union of the given sets.
     *
     * @param first one set
     * @param others the other sets
     * @param <T> the type of elements the sets contain
     * @return the union of the given sets
     */
    public static <T> Set<T> getUnion(Set<T> first, Set<T>... others) {
        return getUnion(first, Arrays.asList(others));
    }

    /**
     * Computes the union of the given sets.
     *
     * @param first one set
     * @param others the other sets
     * @param <T> the type of elements the sets contain
     * @return the union of the given sets
     */
    public static <T> Set<T> getUnion(Set<T> first, Collection<Set<T>> others) {
        Stream<T> unionStream = StreamSupport.stream(first.spliterator(), false);
        for (Set<T> otherSet : others) {
            Stream<T> otherStream = StreamSupport.stream(otherSet.spliterator(), false);
            unionStream = Stream.concat(unionStream, otherStream);
        }
        return unionStream.collect(Collectors.toSet());
    }

    /**
     * Computes the intersection of the given sets.
     *
     * @param first one set
     * @param others the other sets
     * @param <T> the type of elements the sets contain
     * @return the union of the given sets
     */
    public static <T> Set<T> getIntersection(Set<T> first, Set<T>... others) {
        return getIntersection(first, Arrays.asList(others));
    }

    /**
     * Computes the intersection of the given sets.
     *
     * @param first one set
     * @param others the other sets
     * @param <T> the type of elements the sets contain
     * @return the union of the given sets
     */
    public static <T> Set<T> getIntersection(Set<T> first, Collection<Set<T>> others) {
        Stream<T> firstCopyStream = StreamSupport.stream(first.spliterator(), false);
        Set<T> intersection = firstCopyStream.collect(Collectors.toSet());
        for (Set<T> otherSet : others) {
            intersection.retainAll(otherSet);
        }
        return intersection;
    }

    /**
     * Compute the intersection of the complements of the given sets.
     *
     * @param supersetIterator iterator over the elements of the superset
     * @param first one set
     * @param others the other sets
     * @param <T> the type of elements the sets contain
     * @return the intersection of the complements of the given sets
     */
    public static <T> Set<T> getComplementIntersection(
            Iterator<T> supersetIterator, Set<T> first, Set<T>... others) {
        Set<T> superset =
                StreamSupport.stream(
                                Spliterators.spliteratorUnknownSize(
                                        supersetIterator, Spliterator.ORDERED),
                                false)
                        .collect(Collectors.toSet());
        Set<T> firstComplement = getComplement(superset, first);
        List<Set<T>> otherComplements =
                Arrays.stream(others)
                        .map(set -> getComplement(superset, set))
                        .collect(Collectors.toList());
        return getIntersection(firstComplement, otherComplements);
    }

    /**
     * Get one element of the set, no matter which.
     *
     * @param set Set to select an element from
     * @return An element of the set or null if the set is empty
     */
    public static <T> T getAnyElement(Set<T> set) {
        if (set.isEmpty()) {
            return null;
        } else {
            Iterator<T> iterator = set.iterator();
            return iterator.next();
        }
    }
}
