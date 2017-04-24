package org.core.parse;

import javaslang.collection.List;
import javaslang.collection.HashMap;
import javaslang.Tuple;
import javaslang.Tuple2;

import org.jparsec.pattern.Patterns;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;

import static org.core.parse.Abnf.*;

public class FormUrlencoded {
    public static final Parser<String> PLUS = Scanners.string("+").source();
    public static final Parser<String> PERCENT = Scanners.string("%").source();
    public static final Parser<String> SEPERATOR = Scanners.among(";&", "seperator").source();
    public static final Parser<String> ESCAPE = Patterns.sequence(Patterns.string("%"), HEXDIG, HEXDIG).toScanner("escape").source();
    public static final Parser<String> NAMECHAR = Scanners.notAmong(";&+%=", "namechar").source();
    public static final Parser<String> VALUECHAR = Scanners.notAmong(";&+%", "valuechar").source();
    public static final Parser<String> NAME = Parsers.or(NAMECHAR, ESCAPE, PERCENT, PLUS).many1().source();
    public static final Parser<String> VALUE = Parsers.or(VALUECHAR, ESCAPE, PERCENT, PLUS).many().source();
    public static final Parser<Tuple2<String, String>> PAIR = Parsers.sequence(
            NAME,
            Parsers.sequence(
                Scanners.string("="),
                VALUE,
                (_1, value) -> value
            ).optional(),
            Tuple::of
        );
    public static final Parser<List<Tuple2<String, String>>> PAIRS = Parsers.sequence(
            PAIR,
            Parsers.sequence(
                SEPERATOR,
                PAIR,
                (_1, pair) -> pair
            ).many().map(List::ofAll),
            (head, tail) -> tail.prepend(head)
        );
    public static final Parser<List<Tuple2<String, String>>> EMPTY_SET = Scanners.string("").retn(List.empty());
    public static final Parser<HashMap<String, String>> PARSER = Parsers.or(PAIRS, EMPTY_SET).map(HashMap::ofEntries);
}
