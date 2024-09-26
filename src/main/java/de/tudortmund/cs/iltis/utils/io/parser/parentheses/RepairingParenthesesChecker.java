package de.tudortmund.cs.iltis.utils.io.parser.parentheses;

import de.tudortmund.cs.iltis.utils.collections.SerializablePair;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFault;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultCollection;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsableSymbol;
import de.tudortmund.cs.iltis.utils.io.parser.general.TokenUtils;
import java.util.*;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Triple;

public class RepairingParenthesesChecker implements ParenthesesChecker {

    private ParenthesesCheckPropertiesProvidable props;

    public RepairingParenthesesChecker(ParenthesesCheckPropertiesProvidable props) {
        Objects.requireNonNull(props, "props may not be null");
        this.props = props.clone();
    }

    public ParenthesesCheckPropertiesProvidable getProps() {
        return props;
    }

    /**
     * @see ParenthesesChecker#checkForImbalancedParentheses(java.util.List)
     */
    @Override
    public Triple<List<Token>, ParsingFaultCollection, Boolean> checkForImbalancedParentheses(
            List<Token> tokenList) {
        List<ParsingFault> faults = new ArrayList<>();
        // stack which saves, which constellation of ( and [ and { have been read
        Deque<ParenthesesType> openedParens = new LinkedList<>();
        // list to collect the new tokens in
        List<Token> newTokenList = new ArrayList<>();

        // recognize missing opening brackets and parens and braces
        // therefore iterate through token list (without <EOF>)
        for (Token token : tokenList) {
            int parenId = token.getType();

            if (isOpeningParen(parenId)) {
                // if next token is a opening paren
                newTokenList.add(token);
                openedParens.addFirst(getParenType(parenId));
            } else if (isClosingParen(parenId)) {
                // if next token is a closing paren
                Triple<Token, Token, ParsingFault> result =
                        checkForImbalancedClosingParentheses(openedParens.peekFirst(), token);
                if (result.a != null) newTokenList.add(0, result.a);
                newTokenList.add(result.b);
                if (result.c != null) faults.add(result.c);
                openedParens.pollFirst();
            } else {
                // if next token is not a paren
                newTokenList.add(token);
            }
        }

        int lastPosition = 0;
        int lastLine = 0;
        if (tokenList.size() >= 2) {
            // last token is <EOF>
            // therefore: get last character position of second last token
            // usage of position of EOF token leads to strange error off by one
            // maybe caused by Lexer#emitEOF()
            Token lastToken = tokenList.get(tokenList.size() - 2);
            lastPosition =
                    lastToken.getCharPositionInLine()
                            + lastToken.getStopIndex()
                            - lastToken.getStartIndex();
            // correct different line count (start with 1) in ANTLR
            lastLine = lastToken.getLine() - 1;
        }

        // recognize missing closing parens
        while (!openedParens.isEmpty()) {
            faults.add(
                    new ParenthesesParsingFault(
                            ParenthesesParsingFaultReason.CLOSING_PAREN_MISSING,
                            lastLine,
                            lastPosition,
                            "",
                            "",
                            openedParens.getLast()));
            if (repairMissingMode == RepairMissingMode.REPAIR)
                // add at second last position because last one is EOF
                newTokenList.add(
                        newTokenList.size() - 2,
                        CommonTokenFactory.DEFAULT.create(
                                getParenIdOfClosing(openedParens.getLast()),
                                "< missing " + openedParens.getLast().getClosingText() + " >"));
            openedParens.pollFirst();
        }

        return new Triple<>(newTokenList, new ParsingFaultCollection(faults), false);
    }

    /**
     * Checks whether the given paren may be closed at this point and returns a fault if it may not.
     * If any faults are found, they are "repaired" by specifying new tokens.
     *
     * @param lastOpenedParen the paren that should be closed now
     * @param token the currently read token
     * @return a triple containing a token to add before any previous token (or null), a token to
     *     add behind any previous token (not null), a fault (or null)
     */
    private Triple<Token, Token, ParsingFault> checkForImbalancedClosingParentheses(
            ParenthesesType lastOpenedParen, Token token) {

        Token firstToken = null;
        Token lastToken = token;
        ParsingFault fault = null;
        ParenthesesType currentlyClosedParen = getParenType(token.getType());

        if (lastOpenedParen == null) {
            fault =
                    new ParenthesesParsingFault(
                            ParenthesesParsingFaultReason.OPENING_PAREN_MISSING,
                            token.getLine() - 1,
                            token.getCharPositionInLine(),
                            token.getText(),
                            token.getText(),
                            currentlyClosedParen);
            // repair
            if (repairMissingMode == RepairMissingMode.REPAIR)
                firstToken =
                        CommonTokenFactory.DEFAULT.create(
                                getParenIdOfOpening(currentlyClosedParen),
                                "< missing " + currentlyClosedParen.getOpeningText() + " >");

        } else if (lastOpenedParen == currentlyClosedParen) {
            // correct: do nothing
        } else {
            // if last read symbol was opening paren of unexpected type
            fault =
                    new ParenthesesMismatchParsingFault(
                            token.getLine() - 1,
                            token.getCharPositionInLine(),
                            token.getText(),
                            token.getText(),
                            currentlyClosedParen,
                            lastOpenedParen);
            // repair: our default assumption is:
            //         A paren was closed by the wrong paren type.
            //         We replace the paren with the wrong type by one with the correct type and
            // continue.
            if (repairMismatchedMode == RepairMismatchedMode.REPLACE)
                lastToken =
                        TokenUtils.copyTokenWithType(token, getParenIdOfClosing(lastOpenedParen));
        }
        return new Triple<>(firstToken, lastToken, fault);
    }

    @Override
    public Triple<List<Token>, ParsingFaultCollection, Boolean> checkForDisallowedParentheses(
            List<Token> tokenList) {
        List<ParsingFault> faults = new ArrayList<>();
        List<Token> newTokenList = new ArrayList<>();

        boolean bailout = true;
        ParenthesesType defaultType = null;
        if (repairDisallowedMode == RepairDisallowedMode.REPAIR
                && !props.getAllowedParenthesesTypes().isEmpty()) {
            bailout = false;
            defaultType = props.getAllowedParenthesesTypes().iterator().next();
        }

        for (Token token : tokenList) {
            ParenthesesType parenType = getParenType(token.getType());
            Token newToken = token;

            if (parenType != null && !props.getAllowedParenthesesTypes().contains(parenType)) {
                faults.add(
                        new ParenthesesParsingFault(
                                ParenthesesParsingFaultReason.PARENTHESES_GENERALLY_NOT_ALLOWED,
                                token.getLine() - 1,
                                token.getCharPositionInLine(),
                                token.getText(),
                                token.getText(),
                                parenType));
                if (repairDisallowedMode == RepairDisallowedMode.REPAIR)
                    newToken =
                            TokenUtils.copyTokenWithType(
                                    token,
                                    getParenId(defaultType, isOpeningParen(token.getType())));
            }

            newTokenList.add(newToken);
        }

        return new Triple<>(
                newTokenList, new ParsingFaultCollection(faults), !faults.isEmpty() && bailout);
    }

    /* ********
     * HELPER *
     **********/

    /**
     * Translates a parentheses type. Outputs the opening symbol.
     *
     * @param type the parentheses type
     * @return the opening symbol of this type or {@link Integer#MIN_VALUE} if this parentheses type
     *     is not supported
     */
    private int getParenIdOfOpening(ParenthesesType type) {
        return getParenId(type, true);
    }

    /**
     * Translates a parentheses type. Outputs the closing symbol.
     *
     * @param type the parentheses type
     * @return the closing symbol of this type or {@link Integer#MIN_VALUE} if this parentheses type
     *     is not supported
     */
    private int getParenIdOfClosing(ParenthesesType type) {
        return getParenId(type, false);
    }

    /**
     * Translates a parentheses type. Outputs the symbol.
     *
     * @param type the parentheses type
     * @param opening whether an opening paren shall be returned
     * @return the symbol or {@link Integer#MIN_VALUE} if this parentheses type is not supported
     */
    private int getParenId(ParenthesesType type, boolean opening) {
        int result;
        SerializablePair<ParsableSymbol, ParsableSymbol> parenSymbols =
                props.getParenthesesSymbols().get(type);
        if (parenSymbols != null)
            if (opening) result = parenSymbols.first().getTokenType();
            else result = parenSymbols.second().getTokenType();
        else result = Integer.MIN_VALUE;
        return result;
    }

    /**
     * Returns whether this paren id describes an opening paren.
     *
     * @param parenId the id
     * @return true iff the id describes an opening paren
     */
    private boolean isOpeningParen(int parenId) {
        return isParen(parenId, true);
    }

    /**
     * Returns whether this paren id describes a closing paren.
     *
     * @param parenId the id
     * @return true iff the id describes a closing paren
     */
    private boolean isClosingParen(int parenId) {
        return isParen(parenId, false);
    }

    /**
     * Returns whether this paren id describes a paren, as specified in the second argument.
     *
     * @param parenId the id
     * @param opening whether for an opening parentheses shall be tested, else for a closing one is
     *     tested
     * @return true iff the id describes a paren that is opening/closing depending on the second
     *     argument
     */
    private boolean isParen(int parenId, boolean opening) {
        for (Map.Entry<ParenthesesType, SerializablePair<ParsableSymbol, ParsableSymbol>>
                parenEntry : props.getParenthesesSymbols().entrySet()) {
            int openingParen = parenEntry.getValue().first().getTokenType();
            int closingParen = parenEntry.getValue().second().getTokenType();
            if (opening && openingParen == parenId) return true;
            if (!opening && closingParen == parenId) return true;
        }
        return false;
    }

    /**
     * Translates a parentheses id to the parentheses type.
     *
     * @param parenId the id
     * @return the type or null if parenId is not a parentheses id
     */
    private ParenthesesType getParenType(int parenId) {
        for (Map.Entry<ParenthesesType, SerializablePair<ParsableSymbol, ParsableSymbol>>
                parenEntry : props.getParenthesesSymbols().entrySet()) {
            int openingParen = parenEntry.getValue().first().getTokenType();
            int closingParen = parenEntry.getValue().second().getTokenType();
            if (openingParen == parenId || closingParen == parenId) return parenEntry.getKey();
        }
        return null;
    }

    /*********
     * MODES *
     *********/

    private RepairMissingMode repairMissingMode = RepairMissingMode.REPAIR;

    private RepairDisallowedMode repairDisallowedMode = RepairDisallowedMode.REPAIR;
    private RepairMismatchedMode repairMismatchedMode = RepairMismatchedMode.REPLACE;

    enum RepairMissingMode {
        REPAIR,
        BAILOUT
    }

    enum RepairMismatchedMode {
        REPLACE,
        BAILOUT
    }

    enum RepairDisallowedMode {
        REPAIR,
        BAILOUT
    }

    public RepairMissingMode getRepairMissingMode() {
        return repairMissingMode;
    }

    public void setRepairMissingMode(RepairMissingMode repairMissingMode) {
        this.repairMissingMode = repairMissingMode;
    }

    public RepairDisallowedMode getRepairDisallowedMode() {
        return repairDisallowedMode;
    }

    public void setRepairDisallowedMode(RepairDisallowedMode repairDisallowedMode) {
        this.repairDisallowedMode = repairDisallowedMode;
    }

    public RepairMismatchedMode getRepairMismatchedMode() {
        return repairMismatchedMode;
    }

    public void setRepairMismatchedMode(RepairMismatchedMode repairMismatchedMode) {
        this.repairMismatchedMode = repairMismatchedMode;
    }
}
