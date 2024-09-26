package de.tudortmund.cs.iltis.utils.collections;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BiHashMapTest {

    @Test
    public void test() {
        BiHashMap<String, Integer> stringToInt = new BiHashMap<>();

        // map new keys to new values
        stringToInt.put("A", 1);
        stringToInt.put("B", 2);
        stringToInt.put("C", 3);
        assertTrue(stringToInt.inverse().get(1).equals("A"));
        assertTrue(stringToInt.inverse().get(2).equals("B"));
        assertTrue(stringToInt.inverse().get(3).equals("C"));

        // map an existing key to an existing value
        stringToInt.put("B", 3);
        assertTrue(stringToInt.inverse().get(1).equals("A"));
        assertTrue(stringToInt.inverse().get(2) == null);
        assertTrue(stringToInt.inverse().get(3).equals("B"));
        assertTrue(stringToInt.get("C") == null);

        // map an existing key to a non-existing value
        stringToInt.put("B", 2);
        assertTrue(stringToInt.inverse().get(1).equals("A"));
        assertTrue(stringToInt.inverse().get(2).equals("B"));
        assertTrue(stringToInt.inverse().get(3) == null);
        assertTrue(stringToInt.get("C") == null);

        // map a new key to a existing value
        stringToInt.put("D", 2);
        assertTrue(stringToInt.inverse().get(1).equals("A"));
        assertTrue(stringToInt.inverse().get(2).equals("D"));
        assertTrue(stringToInt.inverse().get(3) == null);
        assertTrue(stringToInt.get("B") == null);
        assertTrue(stringToInt.get("C") == null);
    }
}
