// Generated from
// ../src/main/java/de/tudortmund/cs/iltis/logiclib/io/parser/set/SetOfSymbolsParser.g4 by ANTLR 4.4
package de.tudortmund.cs.iltis.utils.io.parser.set;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link SetOfSymbolsParserVisitor}, which can be
 * extended to create a visitor which only needs to handle a subset of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for operations with no return
 *     type.
 */
public class SetOfSymbolsParserBaseVisitor<T> extends AbstractParseTreeVisitor<T>
        implements SetOfSymbolsParserVisitor<T> {
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
     * ctx}.
     */
    @Override
    public T visitSymbol(@NotNull SetOfSymbolsParser.SymbolContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
     * ctx}.
     */
    @Override
    public T visitInitSet(@NotNull SetOfSymbolsParser.InitSetContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
     * ctx}.
     */
    @Override
    public T visitSet(@NotNull SetOfSymbolsParser.SetContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
     * ctx}.
     */
    @Override
    public T visitContent(@NotNull SetOfSymbolsParser.ContentContext ctx) {
        return visitChildren(ctx);
    }
}
