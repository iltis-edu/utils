package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import de.tudortmund.cs.iltis.utils.io.parsable.Parsable;
import de.tudortmund.cs.iltis.utils.io.parsable.ParsableInteger;
import java.util.Objects;
import java.util.Optional;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Spec")
public class ValidSpecXmlElementRequiredSpec extends Parsable<OnlyAttributeNumber, Object> {
    @XmlElement public ParsableInteger number = new ParsableInteger();

    @Override
    public OnlyAttributeNumber parse(Optional<Object> context) {
        return new OnlyAttributeNumber(number.required().value());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ValidSpecXmlElementRequiredSpec)) return false;
        ValidSpecXmlElementRequiredSpec other = (ValidSpecXmlElementRequiredSpec) o;
        return Objects.equals(this.number, other.number);
    }
}
