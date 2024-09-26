package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import de.tudortmund.cs.iltis.utils.io.parsable.Parsable;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.annotation.XmlElement;

public class ListOfRequiredElementsSpecification extends Parsable<ListOfRequiredElements, Object> {
    @XmlElement(name = "Element")
    public List<RequiredElementSpecification> elements;

    @Override
    protected ListOfRequiredElements parse(Optional<Object> context) {
        return new ListOfRequiredElements(parseList("elements"));
    }
}
