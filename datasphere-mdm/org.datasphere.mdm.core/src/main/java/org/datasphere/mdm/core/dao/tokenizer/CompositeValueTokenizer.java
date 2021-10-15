/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 * 
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.dao.tokenizer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mikhail Mikhailov
 *
 * Copyright (c) 2003, PostgreSQL Global Development Group
 * See the LICENSE file in the project root for more information.
 *
 * Extended to support arrays as part of composite objects.
 *
 * This class is used to tokenize the text output of org.postgres. It's mainly used by the geometric
 * classes, but is useful in parsing any output from custom data types output from org.postgresql.
 *
 * @see org.postgresql.geometric.PGbox
 * @see org.postgresql.geometric.PGcircle
 * @see org.postgresql.geometric.PGlseg
 * @see org.postgresql.geometric.PGpath
 * @see org.postgresql.geometric.PGpoint
 * @see org.postgresql.geometric.PGpolygon
 */
public class CompositeValueTokenizer {
    /**
     * PG text escaping set.
     */
    private static final char[] TEXT_ESC_SET = { '\\', '"' };
    /**
     * Our tokens.
     */
    protected final List<String> tokens;
    /**
     * Constructor.
     */
    private CompositeValueTokenizer(String string, char delim) {
        super();
        tokens = new ArrayList<>();
        tokenize(string, delim);
    }
    /**
     * This resets this tokenizer with a new string and/or delimiter.
     *
     * @param string containing tokens
     * @param delim single character to split the tokens
     * @return number of tokens
     */
    private int tokenize(String string, char delim) {

        // nest holds how many levels we are in the current token.
        // if this is > 0 then we don't split a token when delim is matched.
        //
        // The Geometric datatypes use this, because often a type may have others
        // (usualls PGpoint) imbedded within a token.
        //
        // Peter 1998 Jan 6 - Added < and > to the nesting rules
        //
        // 08.08.2019 - { and } array marks added by UD
        int nest = 0;
        int p;
        int s;
        boolean skipChar = false;
        boolean nestedDoubleQuote = false;

        for (p = 0, s = 0; p < string.length(); p++) {
            char c = string.charAt(p);

            // increase nesting if an open character is found
            if (c == '(' || c == '{' || c == '[' || c == '<' || (!nestedDoubleQuote && !skipChar && c == '"')) {
                nest++;
                if (c == '"') {
                    nestedDoubleQuote = true;
                    skipChar = true;
                }
            }

            // decrease nesting if a close character is found
            if (c == ')' || c == '}' || c == ']' || c == '>' || (nestedDoubleQuote && !skipChar && c == '"')) {
                nest--;
                if (c == '"') {
                    nestedDoubleQuote = false;
                }
            }

            skipChar = c == '\\';

            if (nest == 0 && c == delim) {
                tokens.add(string.substring(s, p));
                s = p + 1; // +1 to skip the delimiter
            }
        }

        // Don't forget the last token ;-)
        if (s < string.length()) {
            tokens.add(string.substring(s));
        } else if (s > 0 && s == string.length() && string.charAt(s - 1) == delim) {
            tokens.add(StringUtils.EMPTY);
        }

        return tokens.size();
    }
    /**
     * @return the number of tokens available
     */
    public int getSize() {
        return tokens.size();
    }
    /**
     * @param n Token number ( 0 ... getSize()-1 )
     * @return The token value
     */
    public String getToken(int n) {
        return tokens.get(n);
    }
    /**
     * Strips a char 's' from tail ad head.
     * @param v the value
     * @param s the character to strip
     * @return stripped or unchanged
     */
    public static String strip(String v, char s) {

        if (StringUtils.isBlank(v)) {
            return v;
        }

        int start = 0;
        int end = v.length() - 1;

        while (start < end && v.charAt(start) == s) {
            start++;
        }

        while (end > start && v.charAt(end) == s) {
            end--;
        }

        if (start > 0 || end < (v.length() - 1)) {
            return v.substring(start, end + 1);
        }

        return v;
    }
    /**
     * Strips the string from both sides looking for disitinct characters at s/e.
     * @param v the value
     * @param s char to strip from head
     * @param e char to strip from tail
     * @return stripped or unchanged
     */
    public static String strip(String v, char s, char e) {

        if (StringUtils.isBlank(v)) {
            return v;
        }

        int start = 0;
        int end = v.length() - 1;

        while (start < end && v.charAt(start) == s) {
            start++;
        }

        while (end > start && v.charAt(end) == e) {
            end--;
        }

        if (start > 0 || end < (v.length() - 1)) {
            return v.substring(start, end + 1);
        }

        return v;
    }
    /**
     * Strips any of the characters in found in sl from both sides of the value.
     * @param v the value
     * @param sl the character set
     * @return stripped or unchanged
     */
    public static String strip(String v, char[] sl) {

        if (StringUtils.isBlank(v)) {
            return v;
        }

        int start = 0;
        int end = v.length() - 1;

        _s: while (start < v.length()) {
            for (int i = 0; i < sl.length; i++) {
                if (v.charAt(start) == sl[i]) {
                    start++;
                    continue _s;
                }
            }

            break;
        }

        _l: while (end > 0) {
            for (int i = 0; i < sl.length; i++) {
                if (v.charAt(end) == sl[i]) {
                    end--;
                    continue _l;
                }
            }

            break;
        }

        if (start > 0 || end < (v.length() - 1)) {
            return v.substring(start, end + 1);
        }

        return v;
    }
    /**
     * Strips bytea value.
     * @param v the value
     * @return stripped
     */
    public static String stripBytea(String v) {

        if (StringUtils.isBlank(v)) {
            return v;
        }

        int start = 0;
        int end = v.length() - 1;

        while (start < end && v.charAt(start) == '"' || (v.charAt(start) == '\\' && v.charAt(start + 1) != 'x')) {
            start++;
        }

        while (end > 0 && (v.charAt(end) == '"' || v.charAt(end) == '\\')) {
            end--;
        }

        if (start > 0 || end < (v.length() - 1)) {
            return v.substring(start, end + 1);
        }

        return v;
    }
    /**
     * Strips escaping garbage off text/ts values.
     * @param v the value
     * @return stripped
     */
    public static String stripText(String v) {
        return strip(v, TEXT_ESC_SET);
    }
    /**
     * Removes ( and ) from the beginning and end of a string.
     *
     * @param s String to remove from
     * @return String without the ( or )
     */
    public static String removePara(String s) {
        return strip(s, '(', ')');
    }
    /**
     * Removes [ and ] from the beginning and end of a string.
     *
     * @param s String to remove from
     * @return String without the [ or ]
     */
    public static String removeBox(String s) {
        return strip(s, '[', ']');
    }
    /**
     * Removes &lt; and &gt; from the beginning and end of a string.
     *
     * @param s String to remove from
     * @return String without the &lt; or &gt;
     */
    public static String removeAngle(String s) {
        return strip(s, '<', '>');
    }
    /**
     * Removes curly braces { and } from the beginning and end of a string.
     *
     * @param s String to remove from
     * @return String without the { or }
     */
    public static String removeCurlyBrace(String s) {
        return strip(s, '{', '}');
    }
    /**
     * Struct (composite object) factory method.
     * @param val the value to process
     * @return tokenizer
     */
    public static CompositeValueTokenizer structTokenizer(String val) {
        return new CompositeValueTokenizer(removePara(strip(val, '"')), ',');
    }
    /**
     * Struct (composite object) factory method.
     * @param val the value to process
     * @return tokenizer
     */
    public static CompositeValueTokenizer arrayTokenizer(String val) {
        return new CompositeValueTokenizer(removeCurlyBrace(strip(val, '"')), ',');
    }
}
