package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import de.tudortmund.cs.iltis.utils.io.parsable.Parsable;
import de.tudortmund.cs.iltis.utils.io.parsable.ParsableInteger;
import java.util.Objects;
import java.util.Optional;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Spec")
public class InvalidSpecXmlAttributeRequiredSpec extends Parsable<OnlyAttributeNumber, Object> {
    @XmlAttribute(required = true)
    public ParsableInteger number = new ParsableInteger();

    @Override
    public OnlyAttributeNumber parse(Optional<Object> context) {
        return new OnlyAttributeNumber(number.withDefault(123).value());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof InvalidSpecXmlAttributeRequiredSpec)) return false;
        InvalidSpecXmlAttributeRequiredSpec other = (InvalidSpecXmlAttributeRequiredSpec) o;
        return Objects.equals(this.number, other.number);
    }
}
