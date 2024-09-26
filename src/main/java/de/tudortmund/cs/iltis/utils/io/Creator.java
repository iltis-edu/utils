package de.tudortmund.cs.iltis.utils.io;

import de.tudortmund.cs.iltis.utils.function.TriFunction;
import de.tudortmund.cs.iltis.utils.term.Term;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * A class offering the possibility to register functions for creating objects of certain types.
 * When calling the {@code create}-methods the appropriate function for the given type is determined
 * and executed with the given arguments.
 *
 * @param <TypeE> Type to use to distinguish different functions
 * @param <TermT> Return type of the {@code create}-methods
 * @param <NameT> For some functions, an additional name of this type is used (see {@code NameT} in
 *     {@link Term})
 */
public abstract class Creator<TypeE, TermT extends Creatable, NameT> {
    private Map<TypeE, Supplier<TermT>> constantCreators;
    private Map<TypeE, UnaryOperator<TermT>> unaryFunctionCreators;
    private Map<TypeE, BinaryOperator<TermT>> binaryFunctionCreators;
    private Map<TypeE, Function<List<TermT>, TermT>> vararyFunctionCreators;
    private Map<TypeE, Function<NameT, TermT>> constantWithNameCreators;
    private Map<TypeE, BiFunction<NameT, TermT, TermT>> unaryFunctionWithNameCreators;
    private Map<TypeE, TriFunction<NameT, TermT, TermT, TermT>> binaryFunctionWithNameCreators;
    private Map<TypeE, BiFunction<NameT, List<TermT>, TermT>> vararyFunctionWithNameCreators;

    public Creator() {
        constantCreators = new HashMap<>();
        unaryFunctionCreators = new HashMap<>();
        binaryFunctionCreators = new HashMap<>();
        vararyFunctionCreators = new HashMap<>();
        constantWithNameCreators = new HashMap<>();
        unaryFunctionWithNameCreators = new HashMap<>();
        binaryFunctionWithNameCreators = new HashMap<>();
        vararyFunctionWithNameCreators = new HashMap<>();
    }

    public void registerConstant(TypeE type, Supplier<TermT> creator) {
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(creator, "Creator must not be null");
        constantCreators.put(type, creator);
    }

    public void registerUnaryFunction(TypeE type, UnaryOperator<TermT> creator) {
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(creator, "Creator must not be null");
        unaryFunctionCreators.put(type, creator);
    }

    public void registerBinaryFunction(TypeE type, BinaryOperator<TermT> creator) {
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(creator, "Creator must not be null");
        binaryFunctionCreators.put(type, creator);
    }

    public void registerVararyFunction(TypeE type, Function<List<TermT>, TermT> creator) {
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(creator, "Creator must not be null");
        vararyFunctionCreators.put(type, creator);
    }

    public void registerConstantWithName(TypeE type, Function<NameT, TermT> creator) {
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(creator, "Creator must not be null");
        constantWithNameCreators.put(type, creator);
    }

    public void registerUnaryFunctionWithName(TypeE type, BiFunction<NameT, TermT, TermT> creator) {
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(creator, "Creator must not be null");
        unaryFunctionWithNameCreators.put(type, creator);
    }

    public void registerBinaryFunctionWithName(
            TypeE type, TriFunction<NameT, TermT, TermT, TermT> creator) {
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(creator, "Creator must not be null");
        binaryFunctionWithNameCreators.put(type, creator);
    }

    public void registerVararyFunctionWithName(
            TypeE type, BiFunction<NameT, List<TermT>, TermT> creator) {
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(creator, "Creator must not be null");
        vararyFunctionWithNameCreators.put(type, creator);
    }

    public TermT create(TypeE type) {
        if (!constantCreators.containsKey(type))
            throw new IllegalArgumentException(
                    "No creator for type '" + type + "' with the current arguments registered");
        return constantCreators.get(type).get();
    }

    public TermT create(TypeE type, TermT term) {
        if (!unaryFunctionCreators.containsKey(type))
            throw new IllegalArgumentException(
                    "No creator for type '" + type + "' with the current arguments registered");
        return unaryFunctionCreators.get(type).apply(term);
    }

    public TermT create(TypeE type, TermT term1, TermT term2) {
        if (!binaryFunctionCreators.containsKey(type))
            throw new IllegalArgumentException(
                    "No creator for type '" + type + "' with the current arguments registered");
        return binaryFunctionCreators.get(type).apply(term1, term2);
    }

    public TermT create(TypeE type, List<TermT> terms) {
        if (!vararyFunctionCreators.containsKey(type))
            throw new IllegalArgumentException(
                    "No creator for type '" + type + "' with the current arguments registered");
        return vararyFunctionCreators.get(type).apply(terms);
    }

    public TermT create(TypeE type, NameT name) {
        if (!constantWithNameCreators.containsKey(type))
            throw new IllegalArgumentException(
                    "No creator for type '" + type + "' with the current arguments registered");
        return constantWithNameCreators.get(type).apply(name);
    }

    public TermT create(TypeE type, NameT name, TermT term) {
        if (!unaryFunctionWithNameCreators.containsKey(type))
            throw new IllegalArgumentException(
                    "No creator for type '" + type + "' with the current arguments registered");
        return unaryFunctionWithNameCreators.get(type).apply(name, term);
    }

    public TermT create(TypeE type, NameT name, TermT term1, TermT term2) {
        if (!binaryFunctionWithNameCreators.containsKey(type))
            throw new IllegalArgumentException(
                    "No creator for type '" + type + "' with the current arguments registered");
        return binaryFunctionWithNameCreators.get(type).apply(name, term1, term2);
    }

    public TermT create(TypeE type, NameT name, List<TermT> terms) {
        if (!vararyFunctionWithNameCreators.containsKey(type))
            throw new IllegalArgumentException(
                    "No creator for type '" + type + "' with the current arguments registered");
        return vararyFunctionWithNameCreators.get(type).apply(name, terms);
    }
}
