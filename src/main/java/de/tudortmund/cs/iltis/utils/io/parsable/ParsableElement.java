package de.tudortmund.cs.iltis.utils.io.parsable;

import java.util.Optional;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3c.dom.Element;

@XmlRootElement
@XmlJavaTypeAdapter(ParsableElement.Adapter.class)
public class ParsableElement extends ParsableSource<Element, Element, Object> {
    private Element source;

    public ParsableElement() {
        this(null);
    }

    public ParsableElement(Element source) {
        this.source = source;
    }

    @Override
    protected Element parse(Optional<Object> context) {
        return getSource();
    }

    @Override
    public Element getSource() {
        return source;
    }

    public static class Adapter extends XmlAdapter<Object, ParsableElement> {
        public ParsableElement unmarshal(Object obj) throws ParsableInvalidValue {
            if (obj instanceof Element) {
                return new ParsableElement((Element) obj);
            } else {
                // TODO: Improve
                throw new ParsableInvalidValue(null, "Expected element, got '" + obj + "'");
            }
        }

        public Element marshal(ParsableElement entry) throws RuntimeException {
            throw new UnsupportedOperationException();
        }
    }
}
