package org.flint.parse;

import javaslang.Tuple;
import javaslang.Tuple2;

import org.jparsec.pattern.Pattern;
import org.jparsec.pattern.Patterns;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;

import static org.flint.parse.Abnf.ALPHA;
import static org.flint.parse.Abnf.CRLF;
import static org.flint.parse.Abnf.DIGIT;
import static org.flint.parse.Abnf.HTAB;
import static org.flint.parse.Abnf.SP;
import static org.flint.parse.Abnf.VCHAR;
import static org.flint.parse.Uri.SEGMENT;
import static org.flint.parse.Uri.QUERY;

import org.flint.request.OriginForm;
import org.flint.request.Request;
import org.flint.request.RequestTarget;

public class Http {
    public static final Parser<String> TCHAR = Parsers.or(
            DIGIT,
            ALPHA,
            Scanners.among("!#$%'*+-.^_`|~")
        ).label("obs-text").source();

    public static final Parser<String> OBS_TEXT = Patterns.regex("[\\x80-\\xFF]")
        .toScanner("obs-text").source();

    public static final Parser<String> FIELD_VCHAR = Parsers.or(
            VCHAR,
            OBS_TEXT
        ).label("field-vchar").source();

    public static final Parser<String> OWS = Parsers.or(SP, HTAB).many().label("OWS").source();

    public static final Parser<String> TOKEN = TCHAR.many1().label("token").source();

    public static final Parser<Void> HTTP_VERSION = Scanners.string("HTTP/1.1", "HTTP-version");

    public static final Parser<String> ABSOLUTE_PATH = Parsers.sequence(
            Scanners.isChar('/'),
            SEGMENT
        ).many1()
        .label("absolute-path").source();

    public static final Parser<OriginForm> ORIGIN_FORM = Parsers.sequence(
            ABSOLUTE_PATH,
            Parsers.sequence(
                Scanners.isChar('?'),
                QUERY,
                (_1, q) -> q
            ).optional(),
            (path, q) -> new OriginForm(path, q)
        ).label("origin-form");

    public static final Parser<RequestTarget> REQUEST_TARGET = Parsers.or(ORIGIN_FORM)
        .label("request-target").cast();

    public static final Parser<String> METHOD = TOKEN.label("method");

    public static final Parser<Request> REQUEST_LINE = Parsers.sequence(
            METHOD, SP, REQUEST_TARGET, SP, HTTP_VERSION, CRLF,
            (m, _2, t, _3, _4, _5) -> new Request(m, t)
        ).label("request-line");

    public static final Parser<String> FIELD_NAME = TOKEN.label("field-name");

    public static final Parser<String> FIELD_CONTENT = Parsers.sequence(
            FIELD_VCHAR,
            Parsers.sequence(
                Parsers.or(SP, HTAB).many1(),
                FIELD_VCHAR
            ).optional()
        ).label("field-content").source();

    public static final Parser<String> OBS_FOLD = Parsers.sequence(
            CRLF,
            Parsers.or(SP, HTAB).many1()
        ).label("obs-fold").source();

    public static final Parser<String> FIELD_VALUE = Parsers.or(
            FIELD_CONTENT,
            OBS_FOLD
        ).many()
        .label("field-value").source();

    public static final Parser<Tuple2<String, String>> HEADER_FIELD = Parsers.sequence(
            FIELD_NAME,
            Scanners.isChar(':'),
            OWS,
            FIELD_VALUE,
            OWS,
            (name, _2, _3, value, _5) -> Tuple.of(name, value)
        ).label("header-field");
}
