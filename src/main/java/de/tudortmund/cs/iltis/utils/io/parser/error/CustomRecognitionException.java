package de.tudortmund.cs.iltis.utils.io.parser.error;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFault;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * A shallow wrapper for a {@link ParsingFault}-object. This exception subclasses {@link
 * RecognitionException} to interact with ANTLR's exception handling inside the parser.
 */
public class CustomRecognitionException extends RecognitionException {

    private ParsingFault fault;

    /** Creates a new {@link CustomRecognitionException} with the given parser and fault. */
    public CustomRecognitionException(@NotNull Parser recognizer, ParsingFault fault) {
        super(recognizer, recognizer.getInputStream(), recognizer.getContext());
        this.fault = fault;
    }

    /** Creates a new {@link CustomRecognitionException} with the given lexer and fault. */
    public CustomRecognitionException(@NotNull Lexer recognizer, ParsingFault fault) {
        super(recognizer, recognizer.getInputStream(), null);
        this.fault = fault;
    }

    /** Returns the stored fault. */
    public ParsingFault getFault() {
        return fault;
    }

    /** for serialization */
    private static final long serialVersionUID = 1L;

    /** For serialization */
    @SuppressWarnings("unused")
    protected CustomRecognitionException() {
        super(null, null, null);
    }
}
