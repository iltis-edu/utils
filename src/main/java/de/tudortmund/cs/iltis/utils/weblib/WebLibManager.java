package de.tudortmund.cs.iltis.utils.weblib;

import java.io.*;
import java.net.URI;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A web lib manager provides access to zero or more web libs which are specified in a configuration
 * file
 *
 * <p>The name of each web library must be unique to avoid confusion and involuntary
 * overwriting/shadowing. The same holds for WebLibFunctions <b>within</b> one web lib, but multiple
 * functions with the same name can coexist in separate web libs.
 */
public class WebLibManager {

    private final Map<String, WebLib> libraries;

    private WebLibManager(Map<String, WebLib> libraries) {
        this.libraries = libraries;
    }

    /**
     * Instantiate a new WebLibManager from the content of a configuration file
     *
     * @param inputStream the content of the configuration file
     * @return a new WebLibManager wrapped in an Optional or Optional.empty if a parsing error
     *     occurs
     */
    public static Optional<WebLibManager> initFromConfig(InputStream inputStream) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();
            return Optional.of(parseConfig(doc.getDocumentElement()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Get a web library by name
     *
     * @param name the name of the library to retrieve
     * @return a web lib wrapped in an Optional or Optional.empty if no such library is known
     */
    public Optional<WebLib> getLibrary(String name) {
        return Optional.ofNullable(libraries.get(name));
    }

    private static WebLibManager parseConfig(Element root) {
        NodeList libraries = root.getElementsByTagName("library");
        ArrayList<WebLib> parsedLibraries = new ArrayList<>();
        for (int i = 0; i < libraries.getLength(); ++i) {
            Element lib = (Element) libraries.item(i);
            WebLib webLib = parseWebLib(lib);
            if (parsedLibraries.stream()
                    .anyMatch(l -> l.getLibraryName().equals(webLib.getLibraryName()))) {
                throw new RuntimeException(
                        "Error: two or more web libs with the same name are specified");
            }
            parsedLibraries.add(parseWebLib(lib));
        }
        Map<String, WebLib> mapping = new HashMap<>();
        parsedLibraries.forEach(l -> mapping.put(l.getLibraryName(), l));
        return new WebLibManager(mapping);
    }

    private static WebLib parseWebLib(Element lib) {
        String name = lib.getAttribute("name");
        if (name.isEmpty()) {
            throw new RuntimeException("Error: no name was specified for web lib");
        }
        NodeList functions = lib.getElementsByTagName("function");
        ArrayList<WebLibFunction> parsedFunctions = new ArrayList<>();
        for (int i = 0; i < functions.getLength(); ++i) {
            Element function = (Element) functions.item(i);
            WebLibFunction webLibFunction = parseWebLibFunction(function);
            if (parsedFunctions.stream()
                    .anyMatch(f -> f.getName().equals(webLibFunction.getName()))) {
                throw new RuntimeException(
                        "Error: two or more functions of the same web lib have identical names");
            }
            parsedFunctions.add(parseWebLibFunction(function));
        }
        return new WebLib(name, parsedFunctions);
    }

    private static WebLibFunction parseWebLibFunction(Element function) {
        String name = function.getAttribute("name");
        if (name.isEmpty()) {
            throw new RuntimeException("Error: no name was specified for web lib function");
        }
        NodeList uris = function.getElementsByTagName("uri");
        if (uris.getLength() != 1) {
            throw new RuntimeException("Error: no URI was specified for web lib function");
        }
        NodeList contentTypes = function.getElementsByTagName("contenttype");
        if (contentTypes.getLength() != 1) {
            throw new RuntimeException("Error: no content type was specified for web lib function");
        }
        String uri = uris.item(0).getTextContent();
        String contentType = contentTypes.item(0).getTextContent();
        if (!isValidContentType(contentType)) {
            throw new RuntimeException(
                    "Error: unknown content type was specified for web lib function");
        }
        // URI.create throws an exception for us if the URI is malformed
        return new WebLibFunction(name, URI.create(uri), contentType);
    }

    private static boolean isValidContentType(String contentType) {
        // This is just a subset of possible content types I consider most likely to be used
        // Feel free to add more if necessary, a complete list can be found here:
        // (https://www.iana.org/assignments/media-types/media-types.xhtml)
        return List.of(
                        "application/json",
                        "application/xml",
                        "application/zip",
                        "application/octet-stream",
                        "text/csv",
                        "text/plain",
                        "text/xml")
                .contains(contentType);
    }
}
