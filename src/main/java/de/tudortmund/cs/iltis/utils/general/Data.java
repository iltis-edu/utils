package de.tudortmund.cs.iltis.utils.general;

import de.tudortmund.cs.iltis.utils.collections.ListSet;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Data {
    /** Handles the case that {@code items} is {@code null}. */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(@SuppressWarnings("unchecked") T... items) {
        ArrayList<T> newList = new ArrayList<T>();
        if (items != null) addAll(newList, items);
        return newList;
    }

    /** Handles the case that {@code items} is {@code null}. */
    public static <T> ArrayList<T> newArrayList(Iterable<? extends T> items) {
        ArrayList<T> newList = new ArrayList<T>();
        if (items != null) addAll(newList, items);
        return newList;
    }

    /** Handles the case that {@code items} or {@code item} is {@code null}. */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList1(T item, T... items) {
        ArrayList<T> newList = new ArrayList<T>();
        if (item != null) newList.add(item);
        if (items != null) addAll(newList, items);
        return newList;
    }

    /** Handles the case that {@code items} or {@code item} is {@code null}. */
    public static <T> ArrayList<T> newArrayList1(T item, Iterable<? extends T> items) {
        ArrayList<T> newList = new ArrayList<T>();
        if (item != null) newList.add(item);
        if (items != null) addAll(newList, items);
        return newList;
    }

    /** Handles the case that {@code items} or {@code item} is {@code null}. */
    @SafeVarargs
    public static <T> ListSet<T> newListSet1(T item, T... items) {
        ListSet<T> newSet = new ListSet<T>();
        if (item != null) newSet.add(item);
        if (items != null) addAll(newSet, items);
        return newSet;
    }

    /** Handles the case that {@code items} or {@code item} is {@code null}. */
    public static <T> ListSet<T> newListSet1(T item, Iterable<? extends T> items) {
        ListSet<T> newSet = new ListSet<T>();
        if (item != null) newSet.add(item);
        if (items != null) addAll(newSet, items);
        return newSet;
    }

    /**
     * Handles the case that {@code items} or {@code item} is {@code null}. {@code eqtester} may not
     * be null.
     */
    @SafeVarargs
    public static <T> ListSet<T> newListSet1(
            SerializableBiFunction<T, T, Boolean> eqtester, T item, T... items) {
        ListSet<T> newSet = new ListSet<T>(eqtester);
        if (item != null) newSet.add(item);
        if (items != null) addAll(newSet, items);
        return newSet;
    }

    /**
     * Handles the case that {@code items} or {@code item} is {@code null}. {@code eqtester} may not
     * be null.
     */
    public static <T> ListSet<T> newListSet1(
            SerializableBiFunction<T, T, Boolean> eqtester, T item, Iterable<? extends T> items) {
        ListSet<T> newSet = new ListSet<T>(eqtester);
        if (item != null) newSet.add(item);
        if (items != null) addAll(newSet, items);
        return newSet;
    }

    /** Neither {@code collection} nor {@code items} may be null. */
    public static <T> void addAll(
            Collection<T> collection, @SuppressWarnings("unchecked") T... items) {
        for (T item : items) collection.add(item);
    }

    /** Neither {@code collection} nor {@code items} may be null. */
    public static <T> void addAll(Collection<T> collection, Iterable<? extends T> items) {
        for (T item : items) collection.add(item);
    }

    /** Calculates the number of elements of the given Iterable. */
    @SuppressWarnings("unused")
    public static <T> int getSize(Iterable<T> iterable) {
        int size = 0;
        for (T element : iterable) size++;
        return size;
    }

    /**
     * Inserts elLeft and elRight (in this order) between the elements of listLeft and listRight.
     *
     * <p>If listLeft is null a new empty list is created. Otherwise the given list is modified. If
     * any of elLeft or elRight or listRight is null, this element / these elements is/are not
     * inserted.
     */
    public static <T> List<T> join(List<T> listLeft, T elLeft, T elRight, List<T> listRight) {
        if (listLeft == null) listLeft = new ArrayList<>();
        if (elLeft != null) listLeft.add(elLeft);
        if (elRight != null) listLeft.add(elRight);
        if (listRight != null) listLeft.addAll(listRight);
        return listLeft;
    }

    /** uses {@link Data#join(List, Object, Object, List)} */
    public static <T> List<T> join(T elLeft, List<T> listRight) {
        return join(null, elLeft, null, listRight);
    }

    /** uses {@link Data#join(List, Object, Object, List)} */
    public static <T> List<T> join(T elLeft, T elRight) {
        return join(null, elLeft, elRight, null);
    }

    public static List<Integer> range(int start, int endExclusively) {
        int size = endExclusively - start;
        if (size <= 0) return Data.<Integer>newArrayList();
        List<Integer> range = new ArrayList<Integer>(size);
        for (int i = start; i < endExclusively; i++) range.add(i);
        return range;
    }

    public static <R, T> List<R> map(List<T> list, Function<T, R> fmap) {
        return list.stream().map(fmap).collect(Collectors.toList());
    }

    public static <R, T> List<R> filteredMap(
            List<T> list, Predicate<T> ffilter, Function<T, R> fmap) {
        return list.stream().filter(ffilter).map(fmap).collect(Collectors.toList());
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> ffilter) {
        return list.stream().filter(ffilter).collect(Collectors.toList());
    }

    public static <R, T> ListSet<R> map(ListSet<T> list, Function<T, R> fmap) {
        return new ListSet<>(list.stream().map(fmap));
    }

    public static <R, T> ListSet<R> filteredMap(
            ListSet<T> list, Predicate<T> ffilter, Function<T, R> fmap) {
        return new ListSet<>(list.stream().filter(ffilter).map(fmap));
    }

    public static <T> ListSet<T> filter(ListSet<T> list, Predicate<T> ffilter) {
        return new ListSet<>(list.stream().filter(ffilter));
    }

    public static <T> ListSet<T> union(Collection<Set<T>> sets) {
        ListSet<T> set = new ListSet<T>();
        for (Set<T> subset : sets) set.addAll(subset);
        return set;
    }

    public static <T> boolean forall(ListSet<T> list, Predicate<T> fpred) {
        for (T item : list) if (!fpred.test(item)) return false;
        return true;
    }

    public static <T> boolean exists(ListSet<T> list, Predicate<T> fpred) {
        for (T item : list) if (fpred.test(item)) return true;
        return false;
    }
}
