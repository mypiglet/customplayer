package piglet.mp3player.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class CPStringUtils {
    public static String toString(int number) {
        return String.valueOf(number);
    }

    public static String toString(long number) {
        return String.valueOf(number);
    }

    /**
     * Check whether the given String is empty.
     * <p>
     * This method accepts any Object as an argument, comparing it to
     * {@code null} and the empty String. As a consequence, this method will
     * never return {@code true} for a non-null non-String object.
     * <p>
     * The Object signature is useful for general attribute handling code that
     * commonly deals with Strings but generally has to iterate over Objects
     * since attributes may e.g. be primitive value objects as well.
     * 
     * @param str
     *            the candidate String
     * @since 3.2.1
     */
    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    /**
     * Test if the given String starts with the specified prefix, ignoring
     * upper/lower case.
     * 
     * @param str
     *            the String to check
     * @param prefix
     *            the prefix to look for
     * @see java.lang.String#startsWith
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        if (str.startsWith(prefix)) {
            return true;
        }
        if (str.length() < prefix.length()) {
            return false;
        }
        String lcStr = str.substring(0, prefix.length()).toLowerCase();
        String lcPrefix = prefix.toLowerCase();
        return lcStr.equals(lcPrefix);
    }

    /**
     * Test if the given String ends with the specified suffix, ignoring
     * upper/lower case.
     * 
     * @param str
     *            the String to check
     * @param suffix
     *            the suffix to look for
     * @see java.lang.String#endsWith
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if (str == null || suffix == null) {
            return false;
        }
        if (str.endsWith(suffix)) {
            return true;
        }
        if (str.length() < suffix.length()) {
            return false;
        }

        String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
        String lcSuffix = suffix.toLowerCase();
        return lcStr.equals(lcSuffix);
    }

    /**
     * Test whether the given string matches the given substring at the given
     * index.
     * 
     * @param str
     *            the original string (or StringBuilder)
     * @param index
     *            the index in the original string to start matching against
     * @param substring
     *            the substring to match at the given index
     */
    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        for (int j = 0; j < substring.length(); j++) {
            int i = index + j;
            if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Count the occurrences of the substring in string s.
     * 
     * @param str
     *            string to search in. Return 0 if this is null.
     * @param sub
     *            string to search for. Return 0 if this is null.
     */
    public static int countOccurrencesOf(String str, String sub) {
        if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
            return 0;
        }
        int count = 0;
        int pos = 0;
        int idx;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    // ---------------------------------------------------------------------
    // Convenience methods for working with formatted Strings
    // ---------------------------------------------------------------------

    /**
     * Quote the given String with single quotes.
     * 
     * @param str
     *            the input String (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"), or {@code null} if the
     *         input was {@code null}
     */
    public static String quote(String str) {
        return str != null ? "'" + str + "'" : null;
    }

    /**
     * Turn the given Object into a String with single quotes if it is a String;
     * keeping the Object as-is else.
     * 
     * @param obj
     *            the input Object (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"), or the input object as-is
     *         if not a String
     */
    public static Object quoteIfString(Object obj) {
        return obj instanceof String ? quote((String) obj) : obj;
    }

    /**
     * Unqualify a string qualified by a '.' dot character. For example,
     * "this.name.is.qualified", returns "qualified".
     * 
     * @param qualifiedName
     *            the qualified name
     */
    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }

    /**
     * Unqualify a string qualified by a separator character. For example,
     * "this:name:is:qualified" returns "qualified" if using a ':' separator.
     * 
     * @param qualifiedName
     *            the qualified name
     * @param separator
     *            the separator
     */
    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    /**
     * Capitalize a {@code String}, changing the first letter to upper case as
     * per {@link Character#toUpperCase(char)}. No other letters are changed.
     * 
     * @param str
     *            the String to capitalize, may be {@code null}
     * @return the capitalized String, {@code null} if null
     */
    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    /**
     * Uncapitalize a {@code String}, changing the first letter to lower case as
     * per {@link Character#toLowerCase(char)}. No other letters are changed.
     * 
     * @param str
     *            the String to uncapitalize, may be {@code null}
     * @return the uncapitalized String, {@code null} if null
     */
    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        if (capitalize) {
            sb.append(Character.toUpperCase(str.charAt(0)));
        } else {
            sb.append(Character.toLowerCase(str.charAt(0)));
        }
        sb.append(str.substring(1));
        return sb.toString();
    }

    private static void validateLocalePart(String localePart) {
        for (int i = 0; i < localePart.length(); i++) {
            char ch = localePart.charAt(i);
            if (ch != '_' && ch != ' ' && !Character.isLetterOrDigit(ch)) {
                throw new IllegalArgumentException("Locale part \"" + localePart
                        + "\" contains invalid characters");
            }
        }
    }

    /**
     * Parse the given {@code timeZoneString} value into a {@link TimeZone}.
     * 
     * @param timeZoneString
     *            the time zone String, following
     *            {@link TimeZone#getTimeZone(String)} but throwing
     *            {@link IllegalArgumentException} in case of an invalid time
     *            zone specification
     * @return a corresponding {@link TimeZone} instance
     * @throws IllegalArgumentException
     *             in case of an invalid time zone specification
     */
    public static TimeZone parseTimeZoneString(String timeZoneString) {
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT")) {
            // We don't want that GMT fallback...
            throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString
                    + "'");
        }
        return timeZone;
    }

    // ---------------------------------------------------------------------
    // Convenience methods for working with String arrays
    // ---------------------------------------------------------------------

    /**
     * Copy the given Collection into a String array. The Collection must
     * contain String elements only.
     * 
     * @param collection
     *            the Collection to copy
     * @return the String array ({@code null} if the passed-in Collection was
     *         {@code null})
     */
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    /**
     * Copy the given Enumeration into a String array. The Enumeration must
     * contain String elements only.
     * 
     * @param enumeration
     *            the Enumeration to copy
     * @return the String array ({@code null} if the passed-in Enumeration was
     *         {@code null})
     */
    public static String[] toStringArray(Enumeration<String> enumeration) {
        if (enumeration == null) {
            return null;
        }
        List<String> list = Collections.list(enumeration);
        return list.toArray(new String[list.size()]);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * Trims tokens and omits empty tokens.
     * <p>
     * The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using {@code delimitedListToStringArray}
     * 
     * @param str
     *            the String to tokenize
     * @param delimiters
     *            the delimiter characters, assembled as String (each of those
     *            characters is individually considered as delimiter).
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * <p>
     * The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using {@code delimitedListToStringArray}
     * 
     * @param str
     *            the String to tokenize
     * @param delimiters
     *            the delimiter characters, assembled as String (each of those
     *            characters is individually considered as delimiter)
     * @param trimTokens
     *            trim the tokens via String's {@code trim}
     * @param ignoreEmptyTokens
     *            omit empty tokens from the result array (only applies to
     *            tokens that are empty after trimming; StringTokenizer will not
     *            consider subsequent delimiters as token in the first place).
     * @return an array of the tokens ({@code null} if the input String was
     *         {@code null})
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
            boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

}
