package org.flint.parse;

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
            HEXDIG,
            HEXDIG
        ).label("pct-encoded").source();

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
}
