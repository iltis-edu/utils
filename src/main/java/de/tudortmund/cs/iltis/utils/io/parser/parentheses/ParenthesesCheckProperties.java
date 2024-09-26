package de.tudortmund.cs.iltis.utils.io.parser.parentheses;

import de.tudortmund.cs.iltis.utils.collections.ListSet;
import de.tudortmund.cs.iltis.utils.collections.Pair;
import de.tudortmund.cs.iltis.utils.collections.SerializablePair;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsableSymbol;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class ParenthesesCheckProperties implements ParenthesesCheckPropertiesProvidable {

    /**
     * map assigning each supported parentheses type an opening and a closing parentheses symbol;
     * never null and no element is null
     */
    protected Map<ParenthesesType, SerializablePair<ParsableSymbol, ParsableSymbol>>
            parenthesesMap = new TreeMap<>();

    /**
     * set containing all parentheses types allowed to appear in the input; for types which are
     * supported by {@link #parenthesesMap} but not appear in this list, a parentheses checker may
     * raise a fault; never null and no element is null;
     */
    protected Set<ParenthesesType> allowedParenthesesTypes = new ListSet<>();

    /**
     * @see #parenthesesMap
     * @see #allowedParenthesesTypes
     */
    @Override
    public Map<ParenthesesType, SerializablePair<ParsableSymbol, ParsableSymbol>>
            getParenthesesSymbols() {
        return new TreeMap<>(parenthesesMap);
    }

    /**
     * @see #parenthesesMap
     * @see #allowedParenthesesTypes
     */
    public void clearParenthesesSymbols() {
        parenthesesMap.clear();
        allowedParenthesesTypes.clear();
    }

    /**
     * @see #parenthesesMap
     * @see #allowedParenthesesTypes
     */
    public void addAllowedParenthesesSymbol(
            ParenthesesType type, ParsableSymbol opening, ParsableSymbol closing) {
        addParenthesesSymbol(type, opening, closing);
        allowedParenthesesTypes.add(type);
    }

    /**
     * @see #parenthesesMap
     * @see #allowedParenthesesTypes
     */
    public void addDisallowedParenthesesSymbol(
            ParenthesesType type, ParsableSymbol opening, ParsableSymbol closing) {
        addParenthesesSymbol(type, opening, closing);
        allowedParenthesesTypes.remove(type);
    }

    private void addParenthesesSymbol(
            ParenthesesType type, ParsableSymbol opening, ParsableSymbol closing) {
        if (type == null || opening == null || closing == null)
            throw new IllegalArgumentException("no argument may be null");
        if (parenthesesMap.containsKey(type)) {
            if (!parenthesesMap.get(type).first().equals(opening)
                    || !parenthesesMap.get(type).second().equals(closing))
                throw new IllegalArgumentException(
                        "parentheses type " + type + " is already defined with different symbols");
        } else parenthesesMap.put(type, new SerializablePair<>(opening, closing));
    }

    /**
     * @see #parenthesesMap
     * @see #allowedParenthesesTypes
     */
    public void addAllowedParenthesesSymbols(
            Map<ParenthesesType, Pair<ParsableSymbol, ParsableSymbol>> map) {
        if (map == null) throw new IllegalArgumentException("no argument may be null");
        for (Entry<ParenthesesType, Pair<ParsableSymbol, ParsableSymbol>> entry : map.entrySet())
            addAllowedParenthesesSymbol(
                    entry.getKey(), entry.getValue().first(), entry.getValue().second());
    }

    /**
     * @see #parenthesesMap
     * @see #allowedParenthesesTypes
     */
    public void addDisallowedParenthesesSymbols(
            Map<ParenthesesType, Pair<ParsableSymbol, ParsableSymbol>> map) {
        if (map == null) throw new IllegalArgumentException("no argument may be null");
        for (Entry<ParenthesesType, Pair<ParsableSymbol, ParsableSymbol>> entry : map.entrySet())
            addDisallowedParenthesesSymbol(
                    entry.getKey(), entry.getValue().first(), entry.getValue().second());
    }

    /**
     * @see #parenthesesMap
     * @see #allowedParenthesesTypes
     */
    public void addParenthesesSymbols(
            Map<ParenthesesType, SerializablePair<ParsableSymbol, ParsableSymbol>> map,
            Set<ParenthesesType> allowedParenthesesTypes) {
        if (map == null) throw new IllegalArgumentException("no argument may be null");
        for (Entry<ParenthesesType, SerializablePair<ParsableSymbol, ParsableSymbol>> entry :
                map.entrySet())
            if (allowedParenthesesTypes.contains(entry.getKey()))
                addAllowedParenthesesSymbol(
                        entry.getKey(), entry.getValue().first(), entry.getValue().second());
            else
                addDisallowedParenthesesSymbol(
                        entry.getKey(), entry.getValue().first(), entry.getValue().second());
    }

    /**
     * @see #allowedParenthesesTypes
     */
    public void allowParenthesesType(ParenthesesType type) {
        if (type == null) throw new IllegalArgumentException("no argument may be null");
        if (!parenthesesMap.containsKey(type))
            throw new IllegalArgumentException(
                    "parentheses type " + type + " has no registered parentheses symbols");
        allowedParenthesesTypes.add(type);
    }

    /**
     * @see #allowedParenthesesTypes
     */
    public void disallowParenthesesType(ParenthesesType type) {
        if (type == null) throw new IllegalArgumentException("no argument may be null");
        allowedParenthesesTypes.remove(type);
    }

    /**
     * @see #allowedParenthesesTypes
     */
    public void disallowAllParenthesesTypes() {
        allowedParenthesesTypes.clear();
    }

    @Override
    public String toString() {
        return "ParenthesesCheckProperties [parenthesesMap="
                + parenthesesMap
                + ", allowedParenthesesTypes="
                + allowedParenthesesTypes
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + allowedParenthesesTypes.hashCode();
        result = prime * result + parenthesesMap.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ParenthesesCheckProperties other = (ParenthesesCheckProperties) obj;
        return allowedParenthesesTypes.equals(other.allowedParenthesesTypes)
                && parenthesesMap.equals(other.parenthesesMap);
    }

    @Override
    public ParenthesesCheckPropertiesProvidable clone() {
        ParenthesesCheckProperties clone = new ParenthesesCheckProperties();
        clone.addParenthesesSymbols(parenthesesMap, allowedParenthesesTypes);
        return clone;
    }
}
