package de.tudortmund.cs.iltis.utils.io.parser.parentheses;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultCollection;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Triple;

public class NotCheckingParenthesesChecker implements ParenthesesChecker {

    @Override
    public Triple<List<Token>, ParsingFaultCollection, Boolean> checkForImbalancedParentheses(
            List<Token> tokenList) {
        return new Triple<>(tokenList, new ParsingFaultCollection(), false);
    }

    @Override
    public Triple<List<Token>, ParsingFaultCollection, Boolean> checkForDisallowedParentheses(
            List<Token> tokenList) {
        return new Triple<>(tokenList, new ParsingFaultCollection(), false);
    }
}
