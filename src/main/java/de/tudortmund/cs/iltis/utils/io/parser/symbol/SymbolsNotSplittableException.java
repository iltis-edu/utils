package de.tudortmund.cs.iltis.utils.io.parser.symbol;

public class SymbolsNotSplittableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SymbolsNotSplittableException() {
        super();
    }

    public SymbolsNotSplittableException(String msg) {
        super(msg);
    }

    public SymbolsNotSplittableException(String input, String pattern) {
        super(
                "Input '"
                        + input
                        + "' cannot be splitted to single symbols with symbol pattern '"
                        + pattern
                        + "'");
    }
}
