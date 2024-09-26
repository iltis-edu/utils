// Generated from
// src/main/java/de/tudortmund/cs/iltis/utils/io/parser/customizable//CustomizableLexer.g4 by ANTLR
// 4.4

package de.tudortmund.cs.iltis.utils.io.parser.customizable;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CustomizableLexer extends AbstractCustomizableLexer {
    static {
        RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int TEXT = 1;
    public static String[] modeNames = {"DEFAULT_MODE"};

    public static final String[] tokenNames = {"'\\u0000'", "'\\u0001'"};
    public static final String[] ruleNames = {"TEXT"};

    public CustomizableLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @Override
    public String getGrammarFileName() {
        return "CustomizableLexer.g4";
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
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    @Override
    public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
        switch (ruleIndex) {
            case 0:
                TEXT_action((RuleContext) _localctx, actionIndex);
                break;
        }
    }

    private void TEXT_action(RuleContext _localctx, int actionIndex) {
        switch (actionIndex) {
            case 0:
                tokenize(getText());
                break;
        }
    }

    public static final String _serializedATN =
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\3\f\b\1\4\2\t\2\3"
                    + "\2\6\2\7\n\2\r\2\16\2\b\3\2\3\2\2\2\3\3\3\3\2\2\f\2\3\3\2\2\2\3\6\3\2"
                    + "\2\2\5\7\13\2\2\2\6\5\3\2\2\2\7\b\3\2\2\2\b\6\3\2\2\2\b\t\3\2\2\2\t\n"
                    + "\3\2\2\2\n\13\b\2\2\2\13\4\3\2\2\2\4\2\b\3\3\2\2";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
