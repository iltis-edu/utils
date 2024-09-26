package de.tudortmund.cs.iltis.utils.io.parser.general;

import de.tudortmund.cs.iltis.utils.io.parser.error.CustomRecognitionException;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFault;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultReason;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public abstract class AbstractParser extends Parser {

    /**
     * Determines, if this parser should bail out custom errors as exceptions which can be caught
     * (customBailOut = true) after handing them to the error listener. Default value is {@code
     * false}.
     */
    protected boolean customBailOut = false;

    protected ParsingCreator creator = new ParsingCreator();

    public AbstractParser(TokenStream input) {
        super(input);
    }

    /**
     * Reports the fault to the specified error handler and bails out afterward if specified by
     * isCustomBailOut.
     *
     * @param fault The fault which shall be delegated to the error listener
     * @param token The token which caused this fault
     * @param msg The error message
     */
    protected void reportFault(ParsingFault fault, Token token, String msg) {
        CustomRecognitionException e = new CustomRecognitionException(this, fault);
        notifyErrorListeners(token, msg, e);

        if (customBailOut) throw new ParseCancellationException();
    }

    /**
     * Reports the fault to the specified error handler and bails out afterward if specified by
     * isCustomBailOut.
     *
     * @param reason The reason this fault occurred
     * @param token The token which caused this fault
     * @param faultText The erroneous part of the input; may be empty but not null
     * @param msg The error message
     */
    protected void reportFault(
            ParsingFaultReason reason, Token token, String faultText, String msg) {
        ParsingFault fault =
                new ParsingFault(
                        reason,
                        token.getLine() - 1,
                        token.getCharPositionInLine(),
                        token.getText(),
                        faultText);
        this.reportFault(fault, token, msg);
    }

    /**
     * Reports the fault to the specified error handler and <b>always</b> bails out afterwards.
     *
     * @param fault The fault which shall be delegated to the error listener
     * @param token The token which caused this fault
     * @param msg The error message
     */
    protected void reportFaultAndAlwaysBailOut(ParsingFault fault, Token token, String msg) {
        CustomRecognitionException e = new CustomRecognitionException(this, fault);
        notifyErrorListeners(token, msg, e);

        throw new ParseCancellationException();
    }

    /**
     * Reports the fault to the specified error handler and <b>always</b> bails out afterwards.
     *
     * @param reason The reason this fault occurred
     * @param token The token which caused this fault
     * @param faultText The erroneous part of the input; may be empty but not null
     * @param msg The error message
     */
    protected void reportFaultAndAlwaysBailOut(
            ParsingFaultReason reason, Token token, String faultText, String msg) {
        ParsingFault fault =
                new ParsingFault(
                        reason,
                        token.getLine() - 1,
                        token.getCharPositionInLine(),
                        token.getText(),
                        faultText);
        this.reportFaultAndAlwaysBailOut(fault, token, msg);
    }

    public void setCreator(ParsingCreator creator) {
        if (creator == null) throw new IllegalArgumentException("creator must not be null");
        this.creator = creator;
    }

    public ParsingCreator getCreator() {
        return creator;
    }

    /**
     * Determines, if this parser should bail out custom errors as exceptions which can be caught
     * (customBailOut = true) after handing them to the error listener. Default value is {@code
     * false}.
     */
    public boolean isCustomBailOut() {
        return customBailOut;
    }

    /**
     * Determines, if this parser should bail out custom errors as exceptions which can be caught
     * (customBailOut = true) after handing them to the error listener. Default value is {@code
     * false}.
     */
    public void setCustomBailOut(boolean customBailOut) {
        this.customBailOut = customBailOut;
    }
}
