package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import de.tudortmund.cs.iltis.utils.io.parsable.Parsable;
import de.tudortmund.cs.iltis.utils.io.parsable.ParsableString;
import java.util.Optional;
import javax.xml.bind.annotation.XmlAttribute;

public class RequiredElementSpecification extends Parsable<RequiredElement, Object> {
    @XmlAttribute public ParsableString required = new ParsableString();

    @XmlAttribute public ParsableString optional = new ParsableString();

    @Override
    public RequiredElement parse(Optional<Object> context) {
        return new RequiredElement(required.nonempty().value(), optional.value());
    }
}
