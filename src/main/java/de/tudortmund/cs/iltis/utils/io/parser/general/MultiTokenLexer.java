package de.tudortmund.cs.iltis.utils.io.parser.general;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

/**
 * Lexer which can emit multiple tokens in one rule evaluation. Furthermore, bailing out errors is
 * customizable and a method for notifying all error listeners is added.
 */
public abstract class MultiTokenLexer extends Lexer {

    private List<Token> _tokens = new ArrayList<>();
    private boolean bailOut = false;

    public MultiTokenLexer(CharStream input) {
        super(input);
    }

    @Override
    public void reset() {
        _tokens.clear();
        super.reset();
    }

    @Override
    public void emit(Token token) {
        _tokens.add(token);
        _token = token;
    }

    @Override
    public Token nextToken() {
        if (_tokens.isEmpty()) {
            // many tokens can (but need not) be emitted, i.e. added to the _tokens list
            Token newToken = super.nextToken();
            if (!_tokens.contains(newToken)) emit(newToken);
        }
        Token tokenToReturn = _tokens.get(0);
        _tokens.remove(0);
        return tokenToReturn;
    }

    // In JavaDoc of Lexer#getToken() it is recommended to overwrite #getToken() as well,
    // but I don't see any reason for that, since for generating tokens only #nextToken() is called.

    /**
     * Determines, if this lexer should hand errors to the error listener (bailOut = false) or bail
     * them out as exceptions which can be caught (bailOut = true). Default value is {@code false}.
     */
    public boolean isBailOut() {
        return bailOut;
    }

    /**
     * Determines, if this lexer should hand errors to the error listener (bailOut = false) or bail
     * them out as exceptions which can be caught (bailOut = true). Default value is {@code false}.
     */
    public void setBailOut(boolean bailOut) {
        this.bailOut = bailOut;
    }

    // bail out at first error and do not try to recover, if bailOut is set
    @Override
    public void recover(LexerNoViableAltException e) {
        if (bailOut) throw new ParseCancellationException(e);
        else super.recover(e);
    }

    // bail out at first error and do not try to recover, if bailOut is set
    @Override
    public void recover(RecognitionException e) {
        if (bailOut) throw new ParseCancellationException(e);
        else super.recover(e);
    }

    public void notifyErrorListeners(Token token, String msg, RecognitionException e) {
        ANTLRErrorListener listener = getErrorListenerDispatch();
        listener.syntaxError(
                this, token, token.getLine() - 1, token.getCharPositionInLine(), msg, e);
    }
}
