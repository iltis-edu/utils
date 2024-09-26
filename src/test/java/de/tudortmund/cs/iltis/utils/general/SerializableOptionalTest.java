package de.tudortmund.cs.iltis.utils.general;

import org.junit.Assert;
import org.junit.Test;

public class SerializableOptionalTest {

    @Test
    public void testOf() {
        SerializableOptional<String> optional = SerializableOptional.of("test");
        Assert.assertEquals("test", optional.get());
    }

    @Test(expected = NullPointerException.class)
    public void testOfNull() {
        SerializableOptional<String> optional = SerializableOptional.of(null);
    }

    @Test
    public void testOfNullable() {
        SerializableOptional<String> optional = SerializableOptional.of("test");
        Assert.assertEquals("test", optional.get());
    }

    @Test
    public void testOfNullableNull() {
        SerializableOptional<String> optional = SerializableOptional.ofNullable(null);
        Assert.assertEquals(SerializableOptional.empty(), optional);
    }

    @Test
    public void testIsPresent() {
        SerializableOptional<String> optional = SerializableOptional.of("test");
        Assert.assertTrue(optional.isPresent());
    }

    @Test
    public void testGetOrElse() {
        SerializableOptional<String> optional = SerializableOptional.ofNullable("test");
        Assert.assertEquals("test", optional.getOrElse("notTest"));
    }

    @Test
    public void testGetOrElseNull() {
        SerializableOptional<String> optional = SerializableOptional.ofNullable(null);
        Assert.assertEquals("notTest", optional.getOrElse("notTest"));
    }
}
