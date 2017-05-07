package org.flint.parse;

import javaslang.collection.List;

import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;

import static org.flint.parse.Abnf.ALPHA;
import static org.flint.parse.Abnf.DIGIT;
import static org.flint.parse.Abnf.HEXDIG;

// RFC 3986
public class Uri {
    public static final Parser<String> UNRESERVED = Parsers.or(
            DIGIT,
            ALPHA,
            Scanners.among("-._~")
        ).label("unreserved").source();

    public static final Parser<String> SUB_DELIMS = Scanners.among("!$&'()*+,;=")
        .label("sub-delims").source();

    public static final Parser<String> PCT_ENCODED = Parsers.sequence(
            Scanners.isChar('%'),
            Parsers.sequence(HEXDIG, HEXDIG).source(),
            (_1, ascii) -> ascii
        ).label("pct-encoded");

    public static final Parser<String> PCHAR = Parsers.or(
            UNRESERVED,
            PCT_ENCODED,
            SUB_DELIMS,
            Scanners.among(":@")
        ).label("pchar").source();

    public static final Parser<String> SEGMENT = PCHAR.many()
        .label("segment").source();

    public static final Parser<String> QUERY = Parsers.or(
            PCHAR,
            Scanners.isChar('/'),
            Scanners.isChar('?')
        ).many()
        .label("query").source();

    public static final Parser<String> ENCODE = Parsers.or(
            UNRESERVED.source(),
            Scanners.ANY_CHAR.source().map(Uri::asciiToPercentEncoded)
        ).many()
        .map(chars -> List.ofAll(chars).fold("", String::concat));

    public static final Parser<String> DECODE = Parsers.or(
            PCT_ENCODED.map(Uri::hexToAscii),
            Scanners.ANY_CHAR.source()
        ).many()
        .map(chars -> List.ofAll(chars).fold("", String::concat));

    private static String hexToAscii(String hex) {
        return Character.valueOf((char) Integer.parseInt(hex, 16)).toString();
    }

    private static String asciiToPercentEncoded(String ascii) {
        return "%" + Integer.toHexString((int) ascii.charAt(0)).toUpperCase();
    }
}
