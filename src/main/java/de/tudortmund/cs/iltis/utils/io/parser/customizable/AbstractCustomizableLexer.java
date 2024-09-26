package de.tudortmund.cs.iltis.utils.io.parser.customizable;

import de.tudortmund.cs.iltis.utils.io.parser.general.MultiTokenLexer;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsableSymbol;
import java.util.Map.Entry;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;

public abstract class AbstractCustomizableLexer extends MultiTokenLexer {

    public AbstractCustomizableLexer(CharStream input) {
        super(input);
    }

    private CustomizableLexingPropertiesProvidable properties =
            new EmptyCustomizableLexingProperties();

    public void setProperties(CustomizableLexingPropertiesProvidable props) {
        if (props == null) throw new IllegalArgumentException("props must not be null");
        properties = props;
    }

    public CustomizableLexingPropertiesProvidable getProperties() {
        return properties;
    }

    protected void tokenize(String text) {
        int offsetAfterLastSuccess = 0;
        // curOffset is iterated over the input.
        // The procedure: look if you find a separating symbol starting at curOffset.
        int curOffset = 0;

        offsetLoop:
        while (curOffset < text.length()) {
            // test, if text since last match matches a separating symbol
            for (Entry<String, ParsableSymbol> entry :
                    properties.getSeparatingAndSeparationSymbols().entrySet()) {
                String opText = entry.getKey();
                ParsableSymbol op = entry.getValue();
                // search operator text
                if (text.startsWith(opText, curOffset)) {
                    emitPreviousToken(text, offsetAfterLastSuccess, curOffset);
                    emit(op.getTokenType(), opText, curOffset);
                    curOffset += opText.length();
                    offsetAfterLastSuccess = curOffset;
                    continue offsetLoop;
                }
            }
            // if no success => look at next character
            curOffset++;
        }
        emitPreviousToken(text, offsetAfterLastSuccess, curOffset);
    }

    private void emitPreviousToken(String text, int offsetAfterLastSuccess, int curOffset) {
        if (offsetAfterLastSuccess < curOffset)
            emit(
                    properties.getSymbolForNonSeparatedText().getTokenType(),
                    text.substring(offsetAfterLastSuccess, curOffset),
                    offsetAfterLastSuccess);
    }

    public Token emit(int type, String text, int ruleStartOffset) {
        int channel =
                properties.getSymbolForSeparation().getTokenType() == type
                        ? HIDDEN
                        : DEFAULT_TOKEN_CHANNEL;
        return emit(type, text, ruleStartOffset, channel);
    }

    public Token emit(int type, String text, int ruleStartOffset, int channel) {
        Token t =
                _factory.create(
                        _tokenFactorySourcePair,
                        type,
                        text,
                        channel,
                        _tokenStartCharIndex + ruleStartOffset,
                        _tokenStartCharIndex + ruleStartOffset + text.length(),
                        _tokenStartLine,
                        _tokenStartCharPositionInLine + ruleStartOffset);
        emit(t);
        if (text.contains("\n")) {
            _tokenStartLine++;
            _tokenStartCharPositionInLine = 0;
        }
        return t;
    }

    /*
    An alternative implementation:
    - may be faster if operators are more than just one (or a few) characters
    - may lead to a different result, e.g. given the symbols -> and >-< and text ->-<
      the current implementation will read -> and -<, the alternative implementation would read - and >-<
    - pseudocode:
    	create empty list tokenList of tokens, sorted by their startPosition
    	create empty queue subTexts of strings
    	for each operator in operators
    		for each subText in subTexts
    			search all occurrences of operator in subText (e. g. by algorithm by Knuth, Morris and Pratt)
    			add them to tokenList
    		replace subTexts by the "remainders" of the found occurrences
    	make tokens of unknown type of the remaining entries in subTexts and add them to tokenList
    	emit all tokens out of tokenList
    */
    /*
    An way to handle separation characters (whitespace):
    - in loop:
      	// test if current symbol is separation (most commonly whitespace)
    	int offsetAfterNextSeparation = determineOffsetAfterNextSeparation(text, curOffset);
    	if (curOffset < offsetAfterNextSeparation) {
    		emitHiddenToken(text, offsetAfterLastSuccess, curOffset, offsetAfterNextSeparation);
    		curOffset = offsetAfterNextSeparation;
    		offsetAfterLastSuccess = curOffset;
    	}
    - extra methods:
    	private int determineOffsetAfterNextSeparation(String text, int curOffset) {
    		int returnOffset = curOffset;
    		while (properties.getSeparationChars().contains(text.charAt(returnOffset))) {
    			returnOffset++;
    			if (text.charAt(returnOffset - 1) == '\n')
    				break;
    		}
    		return returnOffset;
    	}

    	public void emitHiddenToken(String text, int offsetAfterLastSuccess, int curOffset, int offsetAfterNextSeparation) {
    		emitPreviousToken(text, offsetAfterLastSuccess, curOffset);
    		emit(properties.getSymbolForSeparation().getTokenType(), text.substring(curOffset, offsetAfterNextSeparation), curOffset, HIDDEN);
    	}
    */
}
