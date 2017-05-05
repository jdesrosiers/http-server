package org.flint.parse;

import javaslang.collection.List;
import javaslang.collection.HashMap;
import javaslang.Tuple;
import javaslang.Tuple2;

import org.jparsec.pattern.Patterns;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;

import static org.flint.parse.Abnf.HEXDIG;

// https://tools.ietf.org/html/draft-hoehrmann-urlencoded-01
public class FormUrlencoded {
    private static final Parser<String> SEPERATOR = Scanners.among(";&", "seperator").source();

    private static final Parser<String> ESCAPE = Parsers.sequence(Scanners.isChar('%'), HEXDIG, HEXDIG)
        .label("escape").source();

    private static final Parser<String> NAMECHAR = Scanners.notAmong(";&+%=", "namechar").source();

    private static final Parser<String> VALUECHAR = Scanners.notAmong(";&+%", "valuechar").source();

    private static final Parser<String> NAME = Parsers.or(
            NAMECHAR,
            ESCAPE,
            Scanners.isChar('%'),
            Scanners.isChar('+')
        ).many1()
        .label("name").source();

    private static final Parser<String> VALUE = Parsers.or(
            VALUECHAR,
            ESCAPE,
            Scanners.isChar('%'),
            Scanners.isChar('+')
        ).many()
        .label("value").source();

    private static final Parser<Tuple2<String, String>> PAIR = Parsers.sequence(
            NAME,
            Parsers.sequence(
                Scanners.string("="),
                VALUE,
                (_1, value) -> value
            ).optional(),
            Tuple::of
        ).label("pair");

    private static final Parser<List<Tuple2<String, String>>> PAIRS = Parsers.sequence(
            PAIR,
            Parsers.sequence(
                SEPERATOR,
                PAIR,
                (_1, pair) -> pair
            ).many().map(List::ofAll),
            (head, tail) -> tail.prepend(head)
        ).label("pairs");

    private static final Parser<List<Tuple2<String, String>>> EMPTY_SET = Scanners.string("")
        .label("empty-set").retn(List.empty());

    public static final Parser<HashMap<String, String>> PARSER = Parsers.or(
            PAIRS,
            EMPTY_SET
        ).map(HashMap::ofEntries);
}
