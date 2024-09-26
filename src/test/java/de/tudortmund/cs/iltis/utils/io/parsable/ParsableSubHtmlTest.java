package de.tudortmund.cs.iltis.utils.io.parsable;

import static org.junit.Assert.*;

import java.util.Objects;
import java.util.Optional;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParsableSubHtmlTest {
    @Test
    public void testMissingDescription() {
        Document actual = readFromFile("io/parsable/doc-noHtml.xml");
        assertEquals(new Document("Opus", 123456, null), actual);
        assertNull(actual.getDescription());
    }

    @Test
    public void testDescriptionWithNestedHtml() {
        Document actual = readFromFile("io/parsable/doc-HtmlString.xml");
        Element description = actual.getDescription();

        Document expected = new Document("Opus", 123456, description);
        assertEquals(expected, actual);

        assertNotNull(description);
        NodeList children = description.getChildNodes();
        Node text = children.item(0);
        Node b = children.item(1);
        assertEquals(2, children.getLength());
        assertEquals("text ", text.getTextContent());
        assertEquals("b", b.getNodeName());
        assertEquals("and more", b.getTextContent());
    }

    @Test
    public void testTextContent() {
        Document actual = readFromFile("io/parsable/doc-HtmlString.xml");
        String textContent = actual.getTextContent();

        assertEquals("text and more", textContent);
    }

    @Test
    public void testXmlAsText() {
        Document actual = readFromFile("io/parsable/doc-HtmlString.xml");
        String xmlAsText = actual.getXmlAsText();

        assertEquals("text <b>and more</b>", xmlAsText);
    }

    private static Document readFromFile(String path) {
        return new XmlParsableReader<>(DocumentSpec.class).read(path);
    }
}

@XmlRootElement(name = "Document")
class DocumentSpec extends Parsable<Document, Object> {
    @XmlAttribute public ParsableString name = new ParsableString();

    public ParsableInteger value = new ParsableInteger();

    @XmlAnyElement public Element description;

    @XmlElement
    @XmlJavaTypeAdapter(ParsableString.TextContentAdapter.class)
    public ParsableString textContent = new ParsableString();

    @XmlElement
    @XmlJavaTypeAdapter(ParsableString.XmlAsTextAdapter.class)
    public ParsableString xmlAsText = new ParsableString();

    @Override
    protected Document parse(Optional<Object> context) {
        Document d = new Document(name.required().value(), value.required().value(), description);
        d.setTextContent(textContent.value());
        d.setXmlAsText(xmlAsText.value());
        return d;
    }
}

class Document {
    private final String name;
    private final Integer value;
    private final Element description;
    private String textContent;
    private String xmlAsText;

    public Document(String name, int value, Element description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public Element getDescription() {
        return description;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getXmlAsText() {
        return xmlAsText;
    }

    public void setXmlAsText(String xmlAsText) {
        this.xmlAsText = xmlAsText;
    }

    /**
     * Only tests equality of {@code name} and {@code value}. That is, the {@code description} is
     * ignored.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(name, document.name) && Objects.equals(value, document.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
