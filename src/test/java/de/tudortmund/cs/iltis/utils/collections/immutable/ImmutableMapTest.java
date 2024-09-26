package de.tudortmund.cs.iltis.utils.collections.immutable;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.collections.Pair;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.Test;

public class ImmutableMapTest {

    ImmutableMap<Boolean, String> emptyMap = new ImmutableMap<>();
    ImmutableMap<String, Integer> textToDigit =
            new ImmutableMap<>(
                    new Pair<>("one", 1),
                    new Pair<>("two", 2),
                    new Pair<>("three", 3),
                    new Pair<>("four", 4),
                    new Pair<>("five", 5),
                    new Pair<>("six", 6),
                    new Pair<>("seven", 7),
                    new Pair<>("eight", 8),
                    new Pair<>("nine", 9));
    ImmutableMap<Character, Integer> charToIndex =
            new ImmutableMap<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f'), c -> c - 97);
    ImmutableMap<Character, Integer> charToAscii =
            new ImmutableMap<>(Arrays.asList('a', 'b', 'c'), Integer::valueOf);
    ImmutableMap<String, String> enToDe =
            new ImmutableMap<>(
                    new Pair<>("one", "eins"),
                    new Pair<>("two", "zwei"),
                    new Pair<>("three", "drei"),
                    new Pair<>("four", "vier"),
                    new Pair<>("five", "fuenf"),
                    new Pair<>("six", "sechs"),
                    new Pair<>("seven", "sieben"),
                    new Pair<>("eight", "acht"),
                    new Pair<>("nine", "neun"));

    Function<Integer, Number> timesTen = i -> 10 * i;
    Predicate<ImmutableMap.Entry<Boolean, String>> constTrue = p -> true;
    Predicate<ImmutableMap.Entry<String, String>> shortWord = p -> p.key.length() <= 3;

    @Test
    public void testSize() {
        assertEquals(0, emptyMap.size());
        assertEquals(3, charToAscii.size());
        assertEquals(6, charToIndex.size());
        assertEquals(9, textToDigit.size());
        assertEquals(9, enToDe.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(emptyMap.isEmpty());
        assertFalse(textToDigit.isEmpty());
        assertFalse(charToIndex.isEmpty());
        assertFalse(enToDe.isEmpty());
    }

    @Test
    public void testContainsKey() {
        assertTrue(textToDigit.containsKey("three"));
        assertTrue(charToIndex.containsKey('a'));
        assertTrue(enToDe.containsKey("nine"));
        assertFalse(emptyMap.containsKey(false));
        assertFalse(charToAscii.containsKey('f'));
    }

    @Test
    public void testGet() {
        assertEquals(Integer.valueOf(1), textToDigit.get("one"));
        assertEquals("sieben", enToDe.get("seven"));
        assertEquals(Integer.valueOf(98), charToAscii.get('b'));
        assertNull(charToIndex.get('p'));
    }

    @Test
    public void testGetOrDefault() {
        assertEquals(Integer.valueOf(6), textToDigit.getOrDefault("six", -3614));
        assertEquals(Integer.valueOf(13), textToDigit.getOrDefault("thirteen", 13));
    }

    @Test
    public void testKeySet() {
        assertEquals(new HashSet<>(), emptyMap.keySet());
        assertEquals(new ImmutableSet<>('a', 'b', 'c').toUnmodifiableSet(), charToAscii.keySet());
        assertEquals(
                new ImmutableSet<>('a', 'b', 'c', 'd', 'e', 'f').toUnmodifiableSet(),
                charToIndex.keySet());
    }

    @Test
    public void testValues() {
        assertEquals(new HashSet<>(), new HashSet<>(emptyMap.values()));
        assertEquals(
                new ImmutableSet<>(1, 2, 3, 4, 5, 6, 7, 8, 9),
                new ImmutableSet<>(textToDigit.values()));
    }

    @Test
    public void testIterator() {
        int i = 1;
        for (ImmutableMap.Entry<String, Integer> keyValuePair : textToDigit) {
            assertEquals(i++, keyValuePair.value.intValue());
        }
    }

    @Test
    public void testApply() {
        assertEquals("drei", enToDe.apply("three"));
        assertEquals(Integer.valueOf(99), charToAscii.apply('c'));
        assertNull(charToIndex.apply('q'));
    }

    @Test
    public void testMap() {
        assertEquals(
                new ImmutableSet<>(10, 20, 30, 40, 50, 60, 70, 80, 90),
                new ImmutableSet<>(textToDigit.map(s -> s, timesTen).values()));
        assertEquals(
                new ImmutableSet<>(
                        "eins", "zwei", "drei", "vier", "fuenf", "sechs", "sieben", "acht", "neun"),
                new ImmutableSet<>(textToDigit.map(enToDe, d -> d).keySet()));
    }

    @Test
    public void testFoldl() {
        assertEquals("noResult", emptyMap.foldl("noResult", (acc, keyValuePair) -> "x"));
        assertEquals(
                new ImmutableMap.Entry<>('c', 99),
                charToAscii.foldl(
                        new ImmutableMap.Entry<>('\0', 0),
                        (acc, entry) ->
                                acc.value > entry.value
                                        ? acc
                                        : new ImmutableMap.Entry<>(entry.key, entry.value)));
    }

    @Test
    public void testFilter() {
        assertEquals(emptyMap, emptyMap.filter(constTrue));
        assertEquals(
                new ImmutableSet<>("one", "two", "six"),
                new ImmutableSet<>(enToDe.filter(shortWord).keySet()));
    }

    @Test
    public void testMerge() {
        ImmutableMap<Character, Integer> merged = charToIndex.merge(charToAscii);
        assertEquals(Integer.valueOf(97), merged.get('a'));
        assertEquals(Integer.valueOf(98), merged.get('b'));
        assertEquals(Integer.valueOf(99), merged.get('c'));
        assertEquals(Integer.valueOf(3), merged.get('d'));
        assertEquals(Integer.valueOf(4), merged.get('e'));
        assertEquals(Integer.valueOf(5), merged.get('f'));
    }
}
