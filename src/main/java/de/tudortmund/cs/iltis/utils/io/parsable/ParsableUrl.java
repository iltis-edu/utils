package de.tudortmund.cs.iltis.utils.io.parsable;

import de.tudortmund.cs.iltis.utils.explainedresult.ExplainedResult;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/** Provides validation checks for url values. */
@XmlJavaTypeAdapter(ParsableUrl.Adapter.class)
public class ParsableUrl extends ParsableEntry<URL> {
    { // for every created object:
        trim();
    }

    /** Create a parsable url with a {@code null} reference. */
    public ParsableUrl() {
        super();
    }

    /**
     * Create a parsable integer with the provided {@code input}.
     *
     * @param input the input string for this {@code ParsableUrl} instance
     */
    public ParsableUrl(String input) {
        super(input);
    }

    @XmlValue
    public String getSource() {
        return super.getSource();
    }

    public void setSource(String source) {
        super.setSource(source);
    }

    @Override
    protected ExplainedResult<Boolean, String> isValid(URL value) {
        return new ExplainedResult<>(true);
    }

    @Override
    protected URL parse(Optional<Object> context) {
        URL url;
        try {
            url = new URL(this.getSource());
        } catch (MalformedURLException e) {
            throw new ParsableInvalidValue(
                    this, "Cannot parse '" + this.getSource() + "' as a url", e);
        }
        return url;
    }

    public static class Adapter extends XmlAdapter<String, ParsableUrl> {
        public ParsableUrl unmarshal(String input) {
            return new ParsableUrl(input);
        }

        public String marshal(ParsableUrl entry) throws RuntimeException {
            throw new UnsupportedOperationException();
        }
    }
}
