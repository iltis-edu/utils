// Generated from
// ../src/main/java/de/tudortmund/cs/iltis/logiclib/io/parser/set/SetOfSymbolsParser.g4 by ANTLR 4.4
package de.tudortmund.cs.iltis.utils.io.parser.set;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced by {@link
 * SetOfSymbolsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for operations with no return
 *     type.
 */
public interface SetOfSymbolsParserVisitor<T> extends ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by {@link SetOfSymbolsParser#symbol}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSymbol(@NotNull SetOfSymbolsParser.SymbolContext ctx);

    /**
     * Visit a parse tree produced by {@link SetOfSymbolsParser#initSet}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitInitSet(@NotNull SetOfSymbolsParser.InitSetContext ctx);

    /**
     * Visit a parse tree produced by {@link SetOfSymbolsParser#set}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSet(@NotNull SetOfSymbolsParser.SetContext ctx);

    /**
     * Visit a parse tree produced by {@link SetOfSymbolsParser#content}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitContent(@NotNull SetOfSymbolsParser.ContentContext ctx);
}
