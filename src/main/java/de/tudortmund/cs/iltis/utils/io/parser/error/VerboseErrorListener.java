package de.tudortmund.cs.iltis.utils.io.parser.error;

import java.util.BitSet;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfig;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;

/**
 * General ErrorListener whose only purpose is to print out every reported error to the (error)
 * command line. Does something very similar to {@link ConsoleErrorListener}, but prints a little
 * more details. Furthermore, ambiguities are reported as well. This part is copied from {@link
 * DiagnosticErrorListener}, but ambiguities are just printed out and not sent to all listeners as
 * errors.
 */
public class VerboseErrorListener extends BaseErrorListener {

    @Override
    public void syntaxError(
            @NotNull Recognizer<?, ?> recognizer,
            @Nullable Object offendingSymbol,
            int line,
            int charPositionInLine,
            @NotNull String msg,
            @Nullable RecognitionException e) {
        System.err.println(
                "VerboseErrorListener: msg: "
                        + msg
                        + "; ex: "
                        + e
                        + "; symbol: "
                        + offendingSymbol
                        + "; line: "
                        + line
                        + "; pos: "
                        + charPositionInLine);
    }

    @Override
    public void reportAmbiguity(
            @NotNull Parser recognizer,
            @NotNull DFA dfa,
            int startIndex,
            int stopIndex,
            boolean exact,
            @Nullable BitSet ambigAlts,
            @NotNull ATNConfigSet configs) {
        String decision = getDecisionDescription(recognizer, dfa);
        BitSet conflictingAlts = getConflictingAlts(ambigAlts, configs);
        String text = recognizer.getTokenStream().getText(Interval.of(startIndex, stopIndex));
        String message =
                "reportAmbiguity d="
                        + decision
                        + ": ambigAlts="
                        + conflictingAlts
                        + ", input='"
                        + text
                        + "'";
        System.err.println("VerboseErrorListener: " + message);
    }

    @Override
    public void reportAttemptingFullContext(
            @NotNull Parser recognizer,
            @NotNull DFA dfa,
            int startIndex,
            int stopIndex,
            @Nullable BitSet conflictingAlts,
            @NotNull ATNConfigSet configs) {
        String decision = getDecisionDescription(recognizer, dfa);
        String text = recognizer.getTokenStream().getText(Interval.of(startIndex, stopIndex));
        String message = "reportAttemptingFullContext d=" + decision + ", input='" + text + "'";
        System.err.println("VerboseErrorListener: " + message);
    }

    @Override
    public void reportContextSensitivity(
            @NotNull Parser recognizer,
            @NotNull DFA dfa,
            int startIndex,
            int stopIndex,
            int prediction,
            @NotNull ATNConfigSet configs) {
        String decision = getDecisionDescription(recognizer, dfa);
        String text = recognizer.getTokenStream().getText(Interval.of(startIndex, stopIndex));
        String message = "reportContextSensitivity d=" + decision + ", input='" + text + "'";
        System.err.println("VerboseErrorListener: " + message);
    }

    protected String getDecisionDescription(@NotNull Parser recognizer, @NotNull DFA dfa) {
        int decision = dfa.decision;
        int ruleIndex = dfa.atnStartState.ruleIndex;

        String[] ruleNames = recognizer.getRuleNames();
        if (ruleIndex < 0 || ruleIndex >= ruleNames.length) {
            return String.valueOf(decision);
        }

        String ruleName = ruleNames[ruleIndex];
        if (ruleName == null || ruleName.isEmpty()) {
            return String.valueOf(decision);
        }

        return decision + " (" + ruleName + ")";
    }

    /**
     * Computes the set of conflicting or ambiguous alternatives from a configuration set, if that
     * information was not already provided by the parser.
     *
     * @param reportedAlts The set of conflicting or ambiguous alternatives, as reported by the
     *     parser.
     * @param configs The conflicting or ambiguous configuration set.
     * @return Returns {@code reportedAlts} if it is not {@code null}, otherwise returns the set of
     *     alternatives represented in {@code configs}.
     */
    @NotNull protected BitSet getConflictingAlts(
            @Nullable BitSet reportedAlts, @NotNull ATNConfigSet configs) {
        if (reportedAlts != null) {
            return reportedAlts;
        }

        BitSet result = new BitSet();
        for (ATNConfig config : configs) {
            result.set(config.alt);
        }

        return result;
    }
}
