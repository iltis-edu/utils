package de.tudortmund.cs.iltis.utils.io.reader.set;

import de.tudortmund.cs.iltis.utils.StringUtils;
import de.tudortmund.cs.iltis.utils.io.parser.customizable.CustomizableLexingProperties;
import de.tudortmund.cs.iltis.utils.io.parser.customizable.CustomizableLexingPropertiesProvidable;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsableSymbol;
import de.tudortmund.cs.iltis.utils.io.parser.parentheses.ParenthesesType;
import de.tudortmund.cs.iltis.utils.io.parser.set.SetOfSymbolsParserOperators;
import de.tudortmund.cs.iltis.utils.io.parser.symbol.RegularSymbolSplittingPolicy;
import java.util.stream.Collectors;

public class SetOfSymbolsReaderProperties extends CustomizableLexingProperties {
    @Override
    public ParsableSymbol getSymbolForNonSeparatedText() {
        return SetOfSymbolsParserOperators.INDEXED_SYMBOL;
    }

    @Override
    public ParsableSymbol getSymbolForISymbols() {
        return SetOfSymbolsParserOperators.INDEXED_SYMBOL;
    }

    @Override
    public ParsableSymbol getSymbolForSeparation() {
        return SetOfSymbolsParserOperators.WHITESPACE;
    }

    private void addAsciiSymbols(String valueSeparator) {
        addSeparatingOperator("{", SetOfSymbolsParserOperators.OBRACE);
        addSeparatingOperator("}", SetOfSymbolsParserOperators.CBRACE);
        addSeparatingOperator(valueSeparator, SetOfSymbolsParserOperators.COMMA);

        addSeparationSymbols(" ", "\t", "\n");
    }

    private void addUnicodeSymbols() {
        addSeparationSymbols(
                StringUtils.getUnicodeWhitespaces().stream()
                        .map(Object::toString)
                        .collect(Collectors.toSet()));
    }

    public static SetOfSymbolsReaderProperties createDefault() {
        return createDefault(",");
    }

    public static SetOfSymbolsReaderProperties createDefault(String valueSeparator) {
        SetOfSymbolsReaderProperties props = new SetOfSymbolsReaderProperties();

        props.setSymbolSplittingPolicy(RegularSymbolSplittingPolicy.UNARY_NAME_AND_INDEX_POLICY);
        props.addAsciiSymbols(valueSeparator);
        props.addUnicodeSymbols();

        props.addAllowedParenthesesSymbol(
                ParenthesesType.BRACES,
                SetOfSymbolsParserOperators.OBRACE,
                SetOfSymbolsParserOperators.CBRACE);

        return props;
    }

    @Override
    public CustomizableLexingPropertiesProvidable clone() {
        SetOfSymbolsReaderProperties props = new SetOfSymbolsReaderProperties();
        props.addSeparationSymbols(separationSymbols);
        props.addSeparatingOperators(separatingOperators);
        props.addNonSeparatingOperators(nonSeparatingOperators);
        props.setSymbolSplittingPolicy(symbolSplittingPolicy);
        props.addParenthesesSymbols(parenthesesMap, allowedParenthesesTypes);
        return props;
    }
}
