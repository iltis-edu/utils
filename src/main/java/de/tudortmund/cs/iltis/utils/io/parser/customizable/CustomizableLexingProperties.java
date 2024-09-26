package de.tudortmund.cs.iltis.utils.io.parser.customizable;

import de.tudortmund.cs.iltis.utils.collections.ListSet;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsableSymbol;
import de.tudortmund.cs.iltis.utils.io.parser.parentheses.ParenthesesCheckProperties;
import de.tudortmund.cs.iltis.utils.io.parser.symbol.BaseSymbolSplittingPolicy;
import de.tudortmund.cs.iltis.utils.io.parser.symbol.SymbolSplittingPolicy;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * Properties for a generic parser. A property-object must be always safe to use and can never
 * contain any {@code null}-values!
 */
public abstract class CustomizableLexingProperties extends ParenthesesCheckProperties
        implements CustomizableLexingPropertiesProvidable {

    // -----------------------
    // SEPARATING OPERATORS
    // -----------------------

    /**
     * map of all operators separating the input sequence; that means: in the input these operators
     * are recognized, no matter where they appear; never null and no element is null; is given by
     * get...() sorted according to symbol (string) length, descending
     */
    protected Map<String, ParsableSymbol> separatingOperators = new TreeMap<>();

    /**
     * @see CustomizableLexingProperties#separatingOperators
     */
    public Map<String, ParsableSymbol> getSeparatingOperators() {
        Map<String, ParsableSymbol> map = new TreeMap<>(lengthComparator);
        map.putAll(separatingOperators);
        return map;
    }

    /**
     * @see CustomizableLexingProperties#separatingOperators
     */
    public void clearSeparatingOperators() {
        separatingOperators.clear();
    }

    /**
     * @see CustomizableLexingProperties#separatingOperators
     */
    public void addSeparatingOperator(String symbol, ParsableSymbol operator) {
        if (symbol == null || operator == null)
            throw new IllegalArgumentException("no argument may be null");
        if (nonSeparatingOperators.containsKey(symbol))
            throw new IllegalArgumentException(
                    "symbol " + symbol + " is already defined as not separating operator");
        if (separatingOperators.containsKey(symbol)) {
            if (!separatingOperators.get(symbol).equals(operator))
                throw new IllegalArgumentException(
                        "symbol " + symbol + " is already defined with different operator");
        } else separatingOperators.put(symbol, operator);
    }

    /**
     * @see CustomizableLexingProperties#separatingOperators
     */
    public void addSeparatingOperators(Map<String, ParsableSymbol> map) {
        if (map == null) throw new IllegalArgumentException("no argument may be null");
        for (Entry<String, ParsableSymbol> entry : map.entrySet())
            addSeparatingOperator(entry.getKey(), entry.getValue());
    }

    @Override
    public Map<String, ParsableSymbol> getSeparatingSymbols() {
        return getSeparatingOperators();
    }

    // -----------------------
    // SEPARATION SYMBOLS
    // -----------------------

    protected Set<String> separationSymbols = new ListSet<>();

    /**
     * @see CustomizableLexingProperties#separationSymbols
     */
    @Override
    public Set<String> getSeparationSymbols() {
        return new ListSet<>(separationSymbols);
    }

    /**
     * @see CustomizableLexingProperties#separationSymbols
     */
    public void clearSeparationSymbols() {
        separationSymbols.clear();
    }

    /**
     * @see CustomizableLexingProperties#separationSymbols
     */
    public void addSeparationSymbol(String symbol) {
        if (symbol == null) throw new IllegalArgumentException("no argument may be null");
        if (nonSeparatingOperators.containsKey(symbol) || separatingOperators.containsKey(symbol))
            throw new IllegalArgumentException(
                    "symbol " + symbol + " is already defined as an operator");
        separationSymbols.add(symbol);
    }

    /**
     * @see CustomizableLexingProperties#separationSymbols
     */
    public void addSeparationSymbols(Set<String> symbols) {
        if (symbols == null) throw new IllegalArgumentException("no argument may be null");
        for (String symbol : symbols) addSeparationSymbol(symbol);
    }

    /**
     * @see CustomizableLexingProperties#separationSymbols
     */
    public void addSeparationSymbols(String... symbols) {
        if (symbols == null) throw new IllegalArgumentException("no argument may be null");
        for (String symbol : symbols) addSeparationSymbol(symbol);
    }

    // -----------------------
    // NON SEPARATING OPERATORS
    // -----------------------

    /**
     * map of all operators not separating the input sequence; that means: in the input these
     * operators have to be separated by whitespace or by separating operators to be recognized;
     * never null and no element is null; is given by get...() sorted according to symbol (string)
     * length, descending
     */
    protected Map<String, ParsableSymbol> nonSeparatingOperators = new TreeMap<>();

    /**
     * @see CustomizableLexingProperties#nonSeparatingOperators
     */
    public Map<String, ParsableSymbol> getNonSeparatingOperators() {
        Map<String, ParsableSymbol> map = new TreeMap<>(lengthComparator);
        map.putAll(nonSeparatingOperators);
        return map;
    }

    /**
     * @see CustomizableLexingProperties#nonSeparatingOperators
     */
    public void clearNonSeparatingOperators() {
        nonSeparatingOperators.clear();
    }

    /**
     * @see CustomizableLexingProperties#nonSeparatingOperators
     */
    public void addNonSeparatingOperator(String symbol, ParsableSymbol operator) {
        if (symbol == null || operator == null)
            throw new IllegalArgumentException("no argument may be null");
        if (separatingOperators.containsKey(symbol))
            throw new IllegalArgumentException(
                    "symbol " + symbol + " is already defined as separating operator");
        if (nonSeparatingOperators.containsKey(symbol)) {
            if (!nonSeparatingOperators.get(symbol).equals(operator))
                throw new IllegalArgumentException(
                        "symbol " + symbol + " is already defined with different operator");
        } else nonSeparatingOperators.put(symbol, operator);
    }

    /**
     * @see CustomizableLexingProperties#nonSeparatingOperators
     */
    public void addNonSeparatingOperators(Map<String, ParsableSymbol> map) {
        if (map == null) throw new IllegalArgumentException("no argument may be null");
        for (Entry<String, ParsableSymbol> entry : map.entrySet())
            addNonSeparatingOperator(entry.getKey(), entry.getValue());
    }

    @Override
    public Map<String, ParsableSymbol> getNonSeparatingSymbols() {
        return getNonSeparatingOperators();
    }

    // -----------------------
    // SYMBOL SPLITTING POLICY
    // -----------------------

    /**
     * Specifies how valid symbol strings look like and how a string will be converted to (possible
     * multiple) indexed symbols. Never null, default is the {@link BaseSymbolSplittingPolicy}.
     */
    protected SymbolSplittingPolicy symbolSplittingPolicy = new BaseSymbolSplittingPolicy();

    /**
     * @see #symbolSplittingPolicy
     */
    @Override
    public SymbolSplittingPolicy getSymbolSplittingPolicy() {
        return symbolSplittingPolicy;
    }

    /**
     * @see #symbolSplittingPolicy
     */
    public void setSymbolSplittingPolicy(SymbolSplittingPolicy symbolSplittingPolicy) {
        Objects.requireNonNull(symbolSplittingPolicy);
        this.symbolSplittingPolicy = symbolSplittingPolicy;
    }

    // -----------------------
    // HELPER
    // -----------------------

    @Override
    public String toString() {
        return "CustomizableLexingProperties [separatingOperators="
                + separatingOperators
                + ", nonSeparatingOperators="
                + nonSeparatingOperators
                + ", separationSymbols="
                + separationSymbols
                + ", symbolSplittingPolicy"
                + symbolSplittingPolicy
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + nonSeparatingOperators.hashCode();
        result = prime * result + separatingOperators.hashCode();
        result = prime * result + separationSymbols.hashCode();
        result = prime * result + symbolSplittingPolicy.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        CustomizableLexingProperties other = (CustomizableLexingProperties) obj;
        return nonSeparatingOperators.equals(other.nonSeparatingOperators)
                && separatingOperators.equals(other.separatingOperators)
                && separationSymbols.equals(other.separationSymbols)
                && symbolSplittingPolicy.equals(other.symbolSplittingPolicy);
    }

    @Override
    public abstract CustomizableLexingPropertiesProvidable clone();
}
