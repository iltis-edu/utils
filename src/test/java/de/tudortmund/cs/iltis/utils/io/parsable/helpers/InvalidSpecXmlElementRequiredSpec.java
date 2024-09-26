package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import de.tudortmund.cs.iltis.utils.io.parsable.Parsable;
import de.tudortmund.cs.iltis.utils.io.parsable.ParsableInteger;
import java.util.Objects;
import java.util.Optional;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "Spec")
public class InvalidSpecXmlElementRequiredSpec extends Parsable<OnlyAttributeNumber, Object> {
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(ParsableInteger.Adapter.class)
    public ParsableInteger number = new ParsableInteger();

    @Override
    public OnlyAttributeNumber parse(Optional<Object> context) {
        return new OnlyAttributeNumber(number.withDefault(123).value());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof InvalidSpecXmlElementRequiredSpec)) return false;
        InvalidSpecXmlElementRequiredSpec other = (InvalidSpecXmlElementRequiredSpec) o;
        return Objects.equals(this.number, other.number);
    }
}
