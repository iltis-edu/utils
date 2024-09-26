package de.tudortmund.cs.iltis.utils.io.parser.general;

import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.Token;

public class TokenUtils {
    /**
     * Creates a new token with the same properties as oldToken but with type newType. The
     * tokenIndex of the newly created token is set to -1.
     */
    public static Token copyTokenWithType(Token oldToken, int newType) {
        return CommonTokenFactory.DEFAULT.create(
                new org.antlr.v4.runtime.misc.Pair<>(
                        oldToken.getTokenSource(), oldToken.getInputStream()),
                newType,
                oldToken.getText(),
                oldToken.getChannel(),
                oldToken.getStartIndex(),
                oldToken.getStopIndex(),
                oldToken.getLine(),
                oldToken.getCharPositionInLine());
    }

    /**
     * Creates a new token with the same properties as oldToken but with type newType, text newText
     * and the starting point is shifted by newStartOffset. The tokenIndex of the newly created
     * token is set to -1.
     */
    public static Token copyTokenWithTypePosText(
            Token oldToken, int newType, String newText, int newStartOffset) {
        return CommonTokenFactory.DEFAULT.create(
                new org.antlr.v4.runtime.misc.Pair<>(
                        oldToken.getTokenSource(), oldToken.getInputStream()),
                newType,
                newText,
                oldToken.getChannel(),
                oldToken.getStartIndex() + newStartOffset,
                oldToken.getStartIndex() + newStartOffset + newText.length(),
                oldToken.getLine(),
                oldToken.getCharPositionInLine() + newStartOffset);
    }
}
