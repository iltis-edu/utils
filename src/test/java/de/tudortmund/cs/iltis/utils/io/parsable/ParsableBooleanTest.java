package de.tudortmund.cs.iltis.utils.io.parsable;

import static de.tudortmund.cs.iltis.utils.test.AdvancedTest.assertThrows;
import static org.junit.Assert.*;

import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.Test;

public class ParsableBooleanTest {
    public static ParsableBoolean pbool(String input) {
        return new ParsableBoolean(input);
    }

    @Test
    public void testRequiredOnPresent() {
        assertTrue(pbool("true").required().value());
        assertFalse(pbool("false").required().value());
    }

    @Test
    public void testOptionalOnPresent() {
        assertTrue(pbool("true").optional().value());
        assertFalse(pbool("false").optional().value());
    }

    @Test
    public void testOptionalOnNull() {
        assertNull(pbool(null).optional().value());
        assertTrue(pbool(null).withDefault(true).value());
        assertFalse(pbool(null).withDefault(false).value());
    }

    @Test
    public void testExceptionOnRequiredNull() {
        assertThrows(ParsableMissingRequired.class, () -> pbool(null).required().value());
    }

    @Test
    public void testInputsTrueAndFalse() {
        Function<ParsableBoolean, ParsableBoolean> mod = pb -> pb;
        Stream.of("yes", "no", "1", "0", "on", "off")
                .forEach(input -> assertThrowsParsableInvalidValue(mod, input));
    }

    @Test
    public void testInputsYesAndNo() {
        Function<ParsableBoolean, ParsableBoolean> mod = pb -> pb.allowInputsYesAndNo();
        assertTrueOn(mod, "true");
        assertTrueOn(mod, "yes");

        assertFalseOn(mod, "false");
        assertFalseOn(mod, "no");
    }

    @Test
    public void testInputsOneAndZero() {
        Function<ParsableBoolean, ParsableBoolean> mod = pb -> pb.allowInputsOneAndZero();
        assertTrueOn(mod, "true");
        assertTrueOn(mod, "1");

        assertFalseOn(mod, "false");
        assertFalseOn(mod, "0");
    }

    @Test
    public void testInputsOnAndOff() {
        Function<ParsableBoolean, ParsableBoolean> mod = pb -> pb.allowInputsOnAndOff();
        assertTrueOn(mod, "true");
        assertTrueOn(mod, "on");

        assertFalseOn(mod, "false");
        assertFalseOn(mod, "off");
    }

    @Test
    public void testInputsGenerously() {
        Function<ParsableBoolean, ParsableBoolean> mod = pb -> pb.allowInputsGenerously();
        assertTrueOn(mod, "true");
        assertTrueOn(mod, "yes");
        assertTrueOn(mod, "1");
        assertTrueOn(mod, "on");

        assertFalseOn(mod, "false");
        assertFalseOn(mod, "no");
        assertFalseOn(mod, "0");
        assertFalseOn(mod, "off");
    }

    @Test
    public void testInputsOuiAndNon() {
        Function<ParsableBoolean, ParsableBoolean> mod =
                pb -> pb.disallowAllInputs().allowInputs("oui", "non");
        assertTrueOn(mod, "oui");
        assertFalseOn(mod, "non");
    }

    @Test
    public void testSomeUntrimmedInputs() {
        Function<ParsableBoolean, ParsableBoolean> mod = pb -> pb;
        Stream.of(" true", "true ", " true ").forEach(input -> assertTrueOn(mod, input));
        Stream.of(" false", "false ", " false ").forEach(input -> assertFalseOn(mod, input));
    }

    @Test
    public void testSomeNonLowerCaseInputs() {
        Function<ParsableBoolean, ParsableBoolean> mod = pb -> pb;
        Stream.of("TRUE", "True").forEach(input -> assertTrueOn(mod, input));
        Stream.of("FALSE", "False").forEach(input -> assertFalseOn(mod, input));
    }

    // HELPERS =======================================================
    private void assertTrueOn(Function<ParsableBoolean, ParsableBoolean> modifier, String input) {
        ParsableBoolean pbool = new ParsableBoolean(input);
        pbool = modifier.apply(pbool);
        assertTrue(pbool.value());
    }

    private void assertFalseOn(Function<ParsableBoolean, ParsableBoolean> modifier, String input) {
        ParsableBoolean pbool = new ParsableBoolean(input);
        pbool = modifier.apply(pbool);
        assertFalse(pbool.value());
    }

    private void assertThrowsParsableInvalidValue(
            Function<ParsableBoolean, ParsableBoolean> modifier, String input) {
        ParsableBoolean pbool = new ParsableBoolean(input);
        assertThrows(ParsableInvalidValue.class, () -> modifier.apply(pbool).value());
    }
}
