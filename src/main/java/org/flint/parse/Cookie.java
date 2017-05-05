package org.flint.parse;

import javaslang.collection.List;
import javaslang.collection.HashMap;
import javaslang.Tuple;
import javaslang.Tuple2;

import org.jparsec.pattern.Patterns;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;

import static org.flint.parse.Abnf.DQUOTE;
import static org.flint.parse.Abnf.SP;
import static org.flint.parse.Http.TOKEN;

// RFC 6265 - https://tools.ietf.org/html/rfc6265
public class Cookie {
    private static final Parser<String> COOKIE_NAME = TOKEN.label("cookie-name");
    private static final Parser<String> COOKIE_OCTET = Patterns.regex("[\\x21\\x23-\\x2B\\x2D-\\x3A\\x3C-\\x5B\\x5D-\\x7E]")
        .toScanner("cookie-octet").source();

    private static final Parser<String> COOKIE_VALUE = Parsers.or(
            COOKIE_OCTET.many().source(),
            Parsers.sequence(
                DQUOTE,
                COOKIE_OCTET.many().source(),
                DQUOTE,
                (_1, value, _2) -> value
            )
        ).label("cookie-value");

    private static final Parser<Tuple2<String, String>> COOKIE_PAIR = Parsers.sequence(
            COOKIE_NAME,
            Scanners.string("="),
            COOKIE_VALUE,
            (cookieName, _2, cookieValue) -> Tuple.of(cookieName, cookieValue)
        ).label("cookie-pair");

    private static final Parser<List<Tuple2<String, String>>> COOKIE_STRING = Parsers.sequence(
            COOKIE_PAIR,
            Parsers.sequence(
                Scanners.string(";"),
                SP,
                COOKIE_PAIR,
                (_1, _2, cookiePair) -> cookiePair
            ).many().map(List::ofAll),
            (head, tail) -> tail.prepend(head)
        ).label("cookie-string");

    public static final Parser<HashMap<String, String>> PARSER = COOKIE_STRING.map(HashMap::ofEntries);
}
