package de.tudortmund.cs.iltis.utils.io.parser.set;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.io.parser.general.SymbolToken;
import java.util.LinkedList;
import java.util.List;

public class SetOfSymbolsConstructionVisitor
        extends SetOfSymbolsParserBaseVisitor<List<IndexedSymbol>>
        implements SetOfSymbolsParserVisitor<List<IndexedSymbol>> {

    @Override
    public List<IndexedSymbol> visitInitSet(SetOfSymbolsParser.InitSetContext ctx) {
        return visit(ctx.set());
    }

    @Override
    public List<IndexedSymbol> visitSet(SetOfSymbolsParser.SetContext ctx) {
        return visit(ctx.content());
    }

    @Override
    public List<IndexedSymbol> visitContent(SetOfSymbolsParser.ContentContext ctx) {
        ISymbolConstructionVisitor symbolVisitor = new ISymbolConstructionVisitor();
        List<IndexedSymbol> parsedSet = new LinkedList<>();
        for (SetOfSymbolsParser.SymbolContext symbolContext : ctx.symbol()) {
            IndexedSymbol symbol = symbolVisitor.visit(symbolContext);
            parsedSet.add(symbol);
        }
        return parsedSet;
    }

    private static class ISymbolConstructionVisitor
            extends SetOfSymbolsParserBaseVisitor<IndexedSymbol> {

        @Override
        public IndexedSymbol visitSymbol(SetOfSymbolsParser.SymbolContext ctx) {
            return ((SymbolToken) ctx.symb).getSymbol();
        }
    }

    @Override
    public List<IndexedSymbol> visitSymbol(SetOfSymbolsParser.SymbolContext ctx) {
        throw new RuntimeException("Use the inner class for ISymbol parsing!");
    }
}
