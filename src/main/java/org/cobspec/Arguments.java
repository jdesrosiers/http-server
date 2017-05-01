package org.cobspec;

import org.jparsec.pattern.Patterns;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;

import javaslang.collection.HashMap;
import javaslang.Tuple;

public class Arguments {
    private static final Parser<Void> WS = Scanners.WHITESPACES.skipMany();
    private static final Parser<String> PORT = Scanners.INTEGER.many1().source();
    private static final Parser<String> DIR = Patterns.regex("[^\\s]+").toScanner("directory").source();

    public static final Parser<HashMap<String, String>> PARSER = Parsers.or(
        WS.next(Scanners.string("-p")).next(WS).next(PORT).map((p) -> Tuple.of("p", p)),
        WS.next(Scanners.string("-d")).next(WS).next(DIR).map((d) -> Tuple.of("d", d))
    ).many().map(HashMap::ofEntries);
}
