package de.tudortmund.cs.iltis.utils.io.parser.parentheses;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultCollection;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Triple;

public interface ParenthesesChecker {

    /**
     * Checks whether all parentheses, brackets, braces, etc. are balanced. Depending on the
     * implementing class, If any faults are found, they can be "repaired" by replacing or adding
     * tokens. For efficiency reasons, implementing classes <b>may modify the given {@code
     * tokenStream}</b>.
     *
     * @param tokenList the original token stream, which may be modified
     * @return a triple containing the new list of tokens (never null), a {@link
     *     ParsingFaultCollection}-object (never null), an indicator whether the parsing should stop
     *     (bailing out)
     */
    Triple<List<Token>, ParsingFaultCollection, Boolean> checkForImbalancedParentheses(
            List<Token> tokenList);

    Triple<List<Token>, ParsingFaultCollection, Boolean> checkForDisallowedParentheses(
            List<Token> tokenList);

    default Triple<List<Token>, ParsingFaultCollection, Boolean> check(List<Token> tokenList) {
        Triple<List<Token>, ParsingFaultCollection, Boolean> resultOfDisallowedCheck =
                checkForDisallowedParentheses(tokenList);
        Triple<List<Token>, ParsingFaultCollection, Boolean> resultOfImbalancedCheck =
                checkForImbalancedParentheses(resultOfDisallowedCheck.a);
        return new Triple<>(
                resultOfImbalancedCheck.a,
                resultOfDisallowedCheck.b.withFaults(resultOfImbalancedCheck.b),
                resultOfDisallowedCheck.c || resultOfImbalancedCheck.c);
    }
}
