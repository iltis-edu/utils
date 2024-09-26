package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import de.tudortmund.cs.iltis.utils.io.parsable.Parsable;
import de.tudortmund.cs.iltis.utils.io.parsable.ParsableInteger;
import de.tudortmund.cs.iltis.utils.io.parsable.ParsableString;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TestObject")
public class TestObjectSpecification extends Parsable<TestObject, Object> {
    @XmlAttribute public ParsableInteger number = new ParsableInteger();

    @XmlAttribute public ParsableString name = new ParsableString();

    @XmlElement(name = "Reference")
    public List<TestReferenceSpecification> references = null;

    @XmlElement(name = "Required")
    public RequiredElementSpecification required = null;

    @XmlElement(name = "Elements")
    public ListOfRequiredElementsSpecification list = new ListOfRequiredElementsSpecification();

    @Override
    public TestObject parse(Optional<Object> context) {
        TestObject result =
                new TestObject(
                        number.withDefault(123).value(),
                        name.required().value(),
                        parseList("references"),
                        wrap("required", RequiredElement.class, RequiredElementSpecification.class)
                                .required()
                                .value());
        // This property is not set via the constructor
        // -- just to illustrate that all kinds of additional
        // processing can be done in `parse()`.
        // This can even include a deeper semantic analysis
        // of the input -- think of graph properties ...
        result.setList(
                wrap(
                                "list",
                                ListOfRequiredElements.class,
                                ListOfRequiredElementsSpecification.class)
                        .withDefault(new ListOfRequiredElements())
                        .value());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        // FOR UNIT TESTS ONLY
        if (o == this) return true;
        if (!(o instanceof TestObjectSpecification)) return false;
        TestObjectSpecification other = (TestObjectSpecification) o;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.number, other.number)
                && Objects.equals(this.references, other.references)
                && Objects.equals(this.required, other.required)
                && Objects.equals(this.list, other.list);
    }
}
