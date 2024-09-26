package de.tudortmund.cs.iltis.utils;

import de.tudortmund.cs.iltis.utils.collections.ListSet;
import java.util.*;
import java.util.function.Function;

public class StringUtils {

    public static String getUnicodeSuperscript(int digit) {
        List<String> unicodes =
                Arrays.asList(
                        "\u2070", "\u00B9", "\u00B2", "\u00B3", "\u2074", "\u2075", "\u2076",
                        "\u2077", "\u2078", "\u2079");
        return unicodes.get(digit);
    }

    public static String getUnicodeSubscript(int digit) {
        List<String> unicodes =
                Arrays.asList(
                        "\u2080", "\u2081", "\u2082", "\u2083", "\u2084", "\u2085", "\u2086",
                        "\u2087", "\u2088", "\u2089");
        return unicodes.get(digit);
    }

    public static String escapeCSV(String toEscape) {
        return escapeCSV(toEscape, ",");
    }

    public static String escapeCSV(String toEscape, String delimiter) {
        String escaped = toEscape;
        if (escaped.contains(delimiter) || escaped.contains("\"")) {
            escaped = "\"" + escaped.replace("\"", "\"\"") + "\"";
        }
        return escaped;
    }

    public static String unescapeCSV(String toUnescape) {
        String unescaped = toUnescape;
        if (unescaped.startsWith("\"")) {
            unescaped = unescaped.substring(1, unescaped.length() - 1);
            unescaped = unescaped.replace("\"\"", "\"");
        }
        return unescaped;
    }

    public static String format(final String format, final Object... args) {
        if (!format.contains("%s")) {
            return format;
        }
        String[] split = format.split("%s", -1);
        ArrayList<String> resultParts = new ArrayList<>(Arrays.asList(split));
        for (int i = 0; i < args.length; i++) {
            if (split.length - 1 <= i) {
                break;
            }
            resultParts.add(2 * i + 1, String.valueOf(args[i]));
        }
        return String.join("", resultParts);
    }

    /**
     * Concatenates the strings obtained by applying {@link T#toString()} on the elements of the
     * specified list. ", " is used as separator and the empty string if list is empty.
     *
     * @param list the input values for toString
     * @return the resulting string
     */
    public static <T> String concatenate(Collection<T> list) {
        return concatenate(list, T::toString);
    }

    /**
     * Concatenates the strings obtained by applying the specified function on the elements of the
     * specified list. ", " is used as separator and the empty string if list is empty.
     *
     * @param list the input values for toString
     * @param toString the function to convert list values to stings
     * @return the resulting string
     */
    public static <T> String concatenate(Collection<T> list, Function<? super T, String> toString) {
        return concatenate(list, toString, "", ", ");
    }

    /**
     * Concatenates the strings obtained by applying the specified function on the elements of the
     * specified list, using the specified separator.
     *
     * @param list the input values for toString
     * @param toString the function to convert list values to stings
     * @param none the placeholder to use if list is empty
     * @param separator the separator to use between the elements
     * @return the resulting string
     */
    public static <T> String concatenate(
            Collection<T> list,
            Function<? super T, String> toString,
            String none,
            String separator) {
        return concatenate(list, toString, none, separator, separator, separator);
    }

    /**
     * Concatenates the strings obtained by applying {@link T#toString()} on the elements of the
     * specified list, using the specified separator.
     *
     * @param list the input values for toString
     * @param none the placeholder to use if list is empty
     * @param separator the separator to use between the elements
     * @return the resulting string
     */
    public static <T> String concatenate(Collection<T> list, String none, String separator) {
        return concatenate(list, none, separator, separator, separator);
    }

    /**
     * Concatenates the strings obtained by applying {@link T#toString()} on the elements of the
     * specified list, using the specified separator.
     *
     * @param list the input values for toString
     * @param none the placeholder to use if list is empty
     * @param separator the separator to use between the elements except before the last element
     * @param lastSeparator the separator to use before the last element if there are at least three
     *     elements
     * @param binarySeparator the separator to use between the elements if there are only two
     *     elements
     * @return the resulting string
     */
    public static <T> String concatenate(
            Collection<T> list,
            String none,
            String separator,
            String lastSeparator,
            String binarySeparator) {
        return concatenate(list, T::toString, none, separator, lastSeparator, binarySeparator);
    }

    /**
     * Concatenates the strings obtained by applying the specified function on the elements of the
     * specified list, using the specified separator.
     *
     * @param list the input values for toString
     * @param toString the function to convert list values to stings
     * @param none the placeholder to use if list is empty
     * @param separator the separator to use between the elements except before the last element
     * @param lastSeparator the separator to use before the last element if there are at least three
     *     elements
     * @param binarySeparator the separator to use between the elements if there are only two
     *     elements
     * @return the resulting string
     */
    public static <T> String concatenate(
            Collection<T> list,
            Function<? super T, String> toString,
            String none,
            String separator,
            String lastSeparator,
            String binarySeparator) {
        int size = list.size();

        if (size == 0) {
            return none;
        } else if (size == 2) {
            Iterator<T> it = list.iterator();
            return it.next() + binarySeparator + it.next();
        } else {
            StringBuilder result = new StringBuilder(size * 15);
            int i = 0;
            for (T item : list) {
                if (i == 0) result.append(toString.apply(item));
                else if (i == size - 1) result.append(lastSeparator).append(toString.apply(item));
                else result.append(separator).append(toString.apply(item));
                i++;
            }

            return result.toString();
        }
    }

    /** Capitalizes the first letter of the argument. */
    public static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /** Returns all line breaking and non line breaking unicode whitespace characters. */
    public static Collection<Character> getUnicodeWhitespaces() {
        ListSet<Character> allWhitespaces = new ListSet<>();
        allWhitespaces.addAll(getUnicodeNonLineBreakingWhitespaces());
        allWhitespaces.addAll(getUnicodeLineBreakingWhitespaces());
        return allWhitespaces;
    }

    public static Collection<Character> getUnicodeNonLineBreakingWhitespaces() {
        return new ListSet<>(
                // see category White_Space in
                // http://www.unicode.org/Public/UCD/latest/ucd/PropList.txt
                '\u0009',
                '\u000B',
                '\u000C',
                ' ', // Space is "%u2020"
                '\u00A0',
                '\u1680',
                '\u2000',
                '\u2001',
                '\u2002',
                '\u2003',
                '\u2004',
                '\u2005',
                '\u2006',
                '\u2007',
                '\u2008',
                '\u2009',
                '\u200A',
                '\u200B',
                '\u200C',
                '\u200D',
                '\u200E',
                '\u200F',
                '\u202F',
                '\u205F',
                '\u3000',
                // ﻿and a Zero Width No-Break Space (for historic reasons)
                '\uFEFF');
    }

    public static Collection<Character> getUnicodeLineBreakingWhitespaces() {
        return new ListSet<>(
                // see category White_Space in
                // http://www.unicode.org/Public/UCD/latest/ucd/PropList.txt
                '\n',
                '\r', // "%u000A" and "%u000D"
                '\u0085',
                '\u2028',
                '\u2029');
    }

    /** Checks whether the given string is empty or consists solely of whitespace characters */
    public static boolean isBlank(String input) {
        return trimBlanks(input).isEmpty();
    }

    /**
     * Splits the given string on all occurrences of whitespace characters
     *
     * @param str the string to split
     * @return the list of substrings separated by whitespaces and without the whitespaces
     */
    public static List<String> splitOnBlanks(String str) {
        if (isBlank(str)) {
            return new LinkedList<>();
        }
        String input = trimBlanks(str);
        // input is not empty and first character is non-blank
        int end = 0;
        while (end < input.length() && !getUnicodeWhitespaces().contains(input.charAt(end))) {
            ++end;
        }
        List<String> result = new LinkedList<>();
        result.add(input.substring(0, end));
        result.addAll(splitOnBlanks(input.substring(end)));
        return result;
    }

    /** Removes all leading and trailing whitespaces from the given string */
    public static String trimBlanks(String input) {
        int start, end;
        for (start = 0; start < input.length(); ++start) {
            Character c = input.charAt(start);
            if (!getUnicodeWhitespaces().contains(c)) {
                break;
            }
        }
        // start is at first non-blank index or at input.length if input is blank

        for (end = input.length(); end > start; --end) {
            Character c = input.charAt(end - 1);
            if (!getUnicodeWhitespaces().contains(c)) {
                break;
            }
        }
        return input.substring(start, end);
    }

    /** Removes all whitespaces from the given string */
    public static String removeBlanks(String input) {
        StringBuilder result = new StringBuilder();
        for (Character c : input.toCharArray()) {
            if (!getUnicodeWhitespaces().contains(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }
}
