package de.tudortmund.cs.iltis.utils.io.reader.set;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.io.parser.customizable.CustomizableLexingReader;
import de.tudortmund.cs.iltis.utils.io.parser.error.visitor.VisitorErrorHandler;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultTypeMapping;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsingCreator;
import de.tudortmund.cs.iltis.utils.io.parser.parentheses.RepairingParenthesesChecker;
import de.tudortmund.cs.iltis.utils.io.parser.set.SetOfSymbolsConstructionVisitor;
import de.tudortmund.cs.iltis.utils.io.parser.set.SetOfSymbolsParser;
import java.util.List;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class SetOfSymbolsReader
        extends CustomizableLexingReader<
                List<IndexedSymbol>, List<IndexedSymbol>, SetOfSymbolsParser> {

    public SetOfSymbolsReader(SetOfSymbolsReaderProperties props) {
        super(props, new RepairingParenthesesChecker(props));
    }

    @Override
    protected SetOfSymbolsParser prepareParser(TokenStream tokenStream) {
        return new SetOfSymbolsParser(tokenStream);
    }

    @Override
    protected List<IndexedSymbol> executeParser(
            SetOfSymbolsParser parser, AbstractParseTreeVisitor<List<IndexedSymbol>> visitor) {
        SetOfSymbolsParser.InitSetContext ctx = parser.initSet();
        return visitor.visit(ctx);
    }

    @Override
    protected ParsingFaultTypeMapping<List<IndexedSymbol>> convertParserOutputToReaderOutput(
            ParsingFaultTypeMapping<List<IndexedSymbol>> mapping) {
        return mapping;
    }

    @Override
    protected AbstractParseTreeVisitor<List<IndexedSymbol>> prepareParseTreeVisitor(
            ParsingCreator creator, VisitorErrorHandler errorHandler) {
        return new SetOfSymbolsConstructionVisitor();
    }
}
