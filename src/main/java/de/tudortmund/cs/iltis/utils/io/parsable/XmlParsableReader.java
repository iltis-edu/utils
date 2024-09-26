package de.tudortmund.cs.iltis.utils.io.parsable;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Node;

/**
 * Read <tt>Parsable</tt> classes from XML, check the specification and return the result. XML can
 * be read from a file, specified via a path or directly from a <tt>Node</tt>.
 *
 * @param <R> the result type
 * @param <S> the specification type
 */
public class XmlParsableReader<R, S extends Parsable<R, C>, C> {
    private final Class<S> specificationClazz;
    private LocationListener locationListener;

    /**
     * Set up object to read XML resource via JAXB into the specification class <tt>S</tt> and then
     * try to parse to an object of the result class <tt>R</tt>.
     *
     * @param specificationClazz the specification class
     */
    public XmlParsableReader(Class<S> specificationClazz) {
        this.specificationClazz = Objects.requireNonNull(specificationClazz);
        this.locationListener = new LocationListener();
    }

    /**
     * Read the specification from an XML file at the given <tt>path</tt> and return its value.
     *
     * @param path to the resource
     * @param context evaluation context
     * @return the parsed object of the result type
     * @throws ParsableException
     */
    public R read(String path, Optional<C> context) throws ParsableException {
        return readBy(() -> readSpecification(path), context);
    }

    /**
     * Read the specification from an XML file at the given <tt>path</tt> and return its value --
     * without context.
     *
     * @param path to the resource
     * @return the parsed object of the result type
     * @throws ParsableException
     */
    public R read(String path) throws ParsableException {
        return read(path, Optional.<C>empty());
    }

    /**
     * Read the specification from an XML file at the given <tt>absolutePath</tt> and return its
     * value.
     *
     * @param absolutePath to the resource
     * @param context evaluation context
     * @return the parsed object of the result type
     * @throws ParsableException
     */
    public R read(Path absolutePath, Optional<C> context) throws ParsableException {
        return readBy(() -> readSpecification(absolutePath), context);
    }

    /**
     * Read the specification from an XML file at the given <tt>absolutePath</tt> and return its
     * value -- without context.
     *
     * @param absolutePath to the resource
     * @return the parsed object of the result type
     * @throws ParsableException
     */
    public R read(Path absolutePath) throws ParsableException {
        return read(absolutePath, Optional.<C>empty());
    }

    /**
     * Read the specification from an XML <tt>Node</tt> and return its value.
     *
     * @param node the XML node
     * @param context evaluation context
     * @return the parsed object of the result type
     * @throws ParsableException
     */
    public R read(Node node, Optional<C> context) throws ParsableException {
        return readBy(() -> readSpecification(node), context);
    }

    public R read(Node node, Optional<C> context, Parsable<?, ?> parent) throws ParsableException {
        return readBy(
                () -> {
                    S spec = readSpecification(node);
                    spec.getMeta().setParent(parent);
                    return spec;
                },
                context);
    }

    /**
     * Read the specification from an XML <tt>Node</tt> and return its value -- without context.
     *
     * @param node the XML node
     * @return the parsed object of the result type
     * @throws ParsableException
     */
    public R read(Node node) throws ParsableException {
        return read(node, Optional.<C>empty());
    }

    public R read(Node node, Parsable<?, ?> parent) throws ParsableException {
        return read(node, Optional.<C>empty(), parent);
    }

    private R readBy(Supplier<S> readSpec, Optional<C> context) throws ParsableException {
        S specification = readSpec.get();
        try {
            return specification.value(context);
        } catch (ParsableException pe) {
            addLocationDetails(pe);
            throw pe;
        }
    }

    /**
     * Only read the specification from the XML file at the given <tt>path</tt>.
     *
     * @param path to the XML file
     * @return the <tt>Parsable</tt> specification
     * @throws ParsableException
     */
    public S readSpecification(String path) throws ParsableException {
        return readSpecification(prepareForPath(path));
    }

    /**
     * Only read the specification from the given XML <tt>node</tt>.
     *
     * @param node the XML node
     * @return the <tt>Parsable</tt> specification
     * @throws ParsableException
     */
    public S readSpecification(Node node) throws ParsableException {
        return readSpecification(prepareForNode(node));
    }

    /**
     * Only read the specification from the XML file at the given <tt>absolutePath</tt>.
     *
     * @param absolutePath to the XML file
     * @return the <tt>Parsable</tt> specification
     * @throws ParsableException
     */
    public S readSpecification(Path absolutePath) throws ParsableException {
        return readSpecification(prepareForAbsolutePath(absolutePath));
    }

    private Callable<S> prepareForPath(String path) {
        JAXBContext context = createBindingContext(specificationClazz);
        XMLStreamReader xsr = createXmlStreamReaderFromResourcePath(path);
        Unmarshaller unmarshaller = createUnmarshaller(context);
        registerLocationListener(xsr, unmarshaller);
        return () -> (S) unmarshaller.unmarshal(xsr);
    }

    private Callable<S> prepareForNode(Node node) {
        JAXBContext context = createBindingContext(specificationClazz);
        Unmarshaller unmarshaller = createUnmarshaller(context);
        return () -> (S) unmarshaller.unmarshal(node);
    }

    private Callable<S> prepareForAbsolutePath(Path absolutePath) {
        JAXBContext context = createBindingContext(specificationClazz);
        XMLStreamReader xsr = createXmlStreamReaderFromAbsolutePath(absolutePath);
        Unmarshaller unmarshaller = createUnmarshaller(context);
        registerLocationListener(xsr, unmarshaller);
        return () -> (S) unmarshaller.unmarshal(xsr);
    }

    private S readSpecification(Callable<S> unmarshalFunc) {
        try {
            S result = unmarshalFunc.call();
            return result;
        } catch (ParsableException pe) {
            addLocationDetails(pe);
            throw pe;
        } catch (Exception e) {
            throw new ParsableException("Failed to parse object from XML", e);
        }
    }

    private <T> JAXBContext createBindingContext(Class<T> clazz) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(clazz);
        } catch (JAXBException e) {
            throw new ParsableInvalidSpecification("Cannot initialize binding context", e);
        }
        return context;
    }

    private XMLStreamReader createXmlStreamReaderFromAbsolutePath(Path absolutePath) {
        return createXmlStreamReader(absolutePath.toString());
    }

    private XMLStreamReader createXmlStreamReaderFromResourcePath(String resourcePath) {
        InputStream inputStream = getResourceInputStream(resourcePath);
        return createXmlStreamReaderFromInputStream(inputStream);
    }

    private XMLStreamReader createXmlStreamReader(String path) {
        XMLInputFactory xif = XMLInputFactory.newFactory();
        FileInputStream xml = null;
        try {
            xml = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            RuntimeException re = new RuntimeException("Cannot find XML file.");
            re.addSuppressed(e);
            throw re;
        }

        XMLStreamReader xsr = null;
        try {
            xsr = xif.createXMLStreamReader(xml);
        } catch (XMLStreamException e) {
            RuntimeException re = new RuntimeException("Failed to create stream reader.");
            re.addSuppressed(e);
            throw re;
        }
        return xsr;
    }

    private XMLStreamReader createXmlStreamReaderFromInputStream(InputStream inputStream) {
        XMLInputFactory xif = XMLInputFactory.newFactory();
        XMLStreamReader xsr = null;
        try {
            xsr = xif.createXMLStreamReader(inputStream);
        } catch (XMLStreamException e) {
            RuntimeException re = new RuntimeException("Failed to create stream reader.");
            re.addSuppressed(e);
            throw re;
        }
        return xsr;
    }

    private Unmarshaller createUnmarshaller(JAXBContext context) {
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            RuntimeException re = new RuntimeException();
            re.addSuppressed(e);
            throw re;
        }
        return unmarshaller;
    }

    private void registerLocationListener(XMLStreamReader xsr, Unmarshaller unmarshaller) {
        locationListener = new LocationListener(xsr);
        unmarshaller.setListener(locationListener);
    }

    private InputStream getResourceInputStream(String path) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

        if (inputStream == null)
            throw new IllegalArgumentException("Resource '" + path + "' not found");

        return inputStream;
    }

    private void addLocationDetails(ParsableException pe) {
        Parsable<?, ?> parsable = pe.getParsable();
        Parsable<?, ?> parent = (parsable == null) ? null : parsable.getMeta().parent;

        String details = "";
        if (pe.provideLocationDetailsSelf() && parsable != null) {
            String self = locationToString(parsable);
            if (!self.isEmpty()) details += self + " ";
        }
        while (parent != null) {
            details +=
                    "in parent "
                            + parent.getMeta().toString()
                            + " "
                            + locationToString(parent)
                            + "\n";
            parent = parent.getMeta().parent;
        }

        pe.setLocationDetails(details);
    }

    private String locationToString(Parsable<?, ?> parsable) {
        if (!parsable.getMeta().isElement() && !parsable.getMeta().isRootElement())
            return ""; // only elements can be located

        Location location = locationListener.getLocation(parsable);
        if (location == null) return "at unknown position";
        return "at line "
                + location.getLineNumber()
                + ", column "
                + location.getColumnNumber()
                + " (offset "
                + location.getCharacterOffset()
                + ")";
    }

    static class LocationListener extends Unmarshaller.Listener {
        private final XMLStreamReader xsr;
        private final Map<Object, Location> locations;

        public LocationListener() {
            this(null);
        }

        public LocationListener(XMLStreamReader xsr) {
            this.xsr = xsr;
            this.locations = new HashMap<>();
        }

        @Override
        public void beforeUnmarshal(Object target, Object parent) {
            if (xsr != null) {
                locations.put(target, xsr.getLocation());
            }
        }

        public Location getLocation(Object obj) {
            return locations.get(obj);
        }
    }
}
