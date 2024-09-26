package de.tudortmund.cs.iltis.utils.io.xml;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.dom4j.io.DOMReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlTextWriter {
    /**
     * Transforms the element {@code element} and its child nodes recursively into XML in text form,
     * using a compact formatting.
     *
     * @param element starting element
     */
    public static String toCompactText(Element element) throws IOException {
        Document doc = elementToDocument(element);

        StringWriter textWriter = new StringWriter();
        OutputFormat format = OutputFormat.createCompactFormat();
        format.setTrimText(false);
        format.setSuppressDeclaration(true);
        XMLWriter xmlWriter = new XMLWriter(textWriter, format);
        DOMReader domReader = new DOMReader();
        xmlWriter.write(domReader.read(doc));
        xmlWriter.flush();

        return textWriter.toString();
    }

    private static Document elementToDocument(Element element) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException("Failed to create DocumentBuilder.", e);
        }
        Document doc = db.newDocument();
        Element adoptedElement = (Element) doc.adoptNode(element);
        doc.appendChild(adoptedElement);
        return doc;
    }

    private XmlTextWriter() {
        // Function collection, no initialization necessary/desired
    }
}
