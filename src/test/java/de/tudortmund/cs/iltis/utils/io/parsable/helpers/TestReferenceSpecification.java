package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import de.tudortmund.cs.iltis.utils.io.parsable.Parsable;
import de.tudortmund.cs.iltis.utils.io.parsable.ParsableString;
import java.util.Optional;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class TestReferenceSpecification extends Parsable<TestReference, Object> {
    @XmlAttribute ParsableString tag = new ParsableString();

    @XmlValue ParsableString name = new ParsableString();

    @Override
    public TestReference parse(Optional<Object> context) {
        return new TestReference(name.nonempty().value(), tag.value());
    }
}
