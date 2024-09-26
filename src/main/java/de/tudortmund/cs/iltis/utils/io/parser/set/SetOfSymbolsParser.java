// Generated from
// ../src/main/java/de/tudortmund/cs/iltis/logiclib/io/parser/set/SetOfSymbolsParser.g4 by ANTLR 4.4
package de.tudortmund.cs.iltis.utils.io.parser.set;

import de.tudortmund.cs.iltis.utils.io.parser.general.AbstractParser;
import java.util.List;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SetOfSymbolsParser extends AbstractParser {
    static {
        RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int OBRACE = 1, CBRACE = 2, COMMA = 3, WHITESPACE = 4, INDEXED_SYMBOL = 5;
    public static final String[] tokenNames = {
        "<INVALID>", "OBRACE", "CBRACE", "COMMA", "WHITESPACE", "INDEXED_SYMBOL"
    };
    public static final int RULE_initSet = 0, RULE_set = 1, RULE_content = 2, RULE_symbol = 3;
    public static final String[] ruleNames = {"initSet", "set", "content", "symbol"};

    @Override
    public String getGrammarFileName() {
        return "SetOfSymbolsParser.g4";
    }

    @Override
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public SetOfSymbolsParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class InitSetContext extends ParserRuleContext {
        public TerminalNode EOF() {
            return getToken(SetOfSymbolsParser.EOF, 0);
        }

        public SetContext set() {
            return getRuleContext(SetContext.class, 0);
        }

        public InitSetContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_initSet;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SetOfSymbolsParserVisitor)
                return ((SetOfSymbolsParserVisitor<? extends T>) visitor).visitInitSet(this);
            else return visitor.visitChildren(this);
        }
    }

    public final InitSetContext initSet() throws RecognitionException {
        InitSetContext _localctx = new InitSetContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_initSet);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(8);
                set();
                setState(9);
                match(EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SetContext extends ParserRuleContext {
        public TerminalNode OBRACE() {
            return getToken(SetOfSymbolsParser.OBRACE, 0);
        }

        public TerminalNode CBRACE() {
            return getToken(SetOfSymbolsParser.CBRACE, 0);
        }

        public ContentContext content() {
            return getRuleContext(ContentContext.class, 0);
        }

        public SetContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_set;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SetOfSymbolsParserVisitor)
                return ((SetOfSymbolsParserVisitor<? extends T>) visitor).visitSet(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SetContext set() throws RecognitionException {
        SetContext _localctx = new SetContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_set);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(11);
                match(OBRACE);
                setState(12);
                content();
                setState(13);
                match(CBRACE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ContentContext extends ParserRuleContext {
        public SymbolContext symbol(int i) {
            return getRuleContext(SymbolContext.class, i);
        }

        public List<TerminalNode> COMMA() {
            return getTokens(SetOfSymbolsParser.COMMA);
        }

        public List<SymbolContext> symbol() {
            return getRuleContexts(SymbolContext.class);
        }

        public TerminalNode COMMA(int i) {
            return getToken(SetOfSymbolsParser.COMMA, i);
        }

        public ContentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_content;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SetOfSymbolsParserVisitor)
                return ((SetOfSymbolsParserVisitor<? extends T>) visitor).visitContent(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ContentContext content() throws RecognitionException {
        ContentContext _localctx = new ContentContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_content);
        try {
            int _alt;
            setState(25);
            switch (_input.LA(1)) {
                case INDEXED_SYMBOL:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(20);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                        while (_alt != 2
                                && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                            if (_alt == 1) {
                                {
                                    {
                                        setState(15);
                                        symbol();
                                        setState(16);
                                        match(COMMA);
                                    }
                                }
                            }
                            setState(22);
                            _errHandler.sync(this);
                            _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                        }
                        setState(23);
                        symbol();
                    }
                    break;
                case CBRACE:
                    enterOuterAlt(_localctx, 2);
                    {
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SymbolContext extends ParserRuleContext {
        public Token symb;

        public TerminalNode INDEXED_SYMBOL() {
            return getToken(SetOfSymbolsParser.INDEXED_SYMBOL, 0);
        }

        public SymbolContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_symbol;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof SetOfSymbolsParserVisitor)
                return ((SetOfSymbolsParserVisitor<? extends T>) visitor).visitSymbol(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SymbolContext symbol() throws RecognitionException {
        SymbolContext _localctx = new SymbolContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_symbol);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(27);
                ((SymbolContext) _localctx).symb = match(INDEXED_SYMBOL);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static final String _serializedATN =
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\7 \4\2\t\2\4\3\t"
                    + "\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\7\4\25\n\4"
                    + "\f\4\16\4\30\13\4\3\4\3\4\5\4\34\n\4\3\5\3\5\3\5\2\2\6\2\4\6\b\2\2\35"
                    + "\2\n\3\2\2\2\4\r\3\2\2\2\6\33\3\2\2\2\b\35\3\2\2\2\n\13\5\4\3\2\13\f\7"
                    + "\2\2\3\f\3\3\2\2\2\r\16\7\3\2\2\16\17\5\6\4\2\17\20\7\4\2\2\20\5\3\2\2"
                    + "\2\21\22\5\b\5\2\22\23\7\5\2\2\23\25\3\2\2\2\24\21\3\2\2\2\25\30\3\2\2"
                    + "\2\26\24\3\2\2\2\26\27\3\2\2\2\27\31\3\2\2\2\30\26\3\2\2\2\31\34\5\b\5"
                    + "\2\32\34\3\2\2\2\33\26\3\2\2\2\33\32\3\2\2\2\34\7\3\2\2\2\35\36\7\7\2"
                    + "\2\36\t\3\2\2\2\4\26\33";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
