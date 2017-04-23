package org.httpserver.parse;

import org.jparsec.pattern.Pattern;
import org.jparsec.pattern.Patterns;
import org.jparsec.Parser;

import static org.httpserver.parse.Abnf.*;

// RFC 3986
public class Uri {
    private static final Pattern forwardSlash = Patterns.string("/");
    private static final Pattern questionMark = Patterns.string("?");

    public static final Pattern unreserved = Patterns.or(DIGIT, ALPHA, Patterns.regex("[\\-._~]"));
    public static final Pattern subDelims = Patterns.regex("[!$&'()*+,;=]");
    public static final Pattern pctEncoded = Patterns.sequence(Patterns.string("%"), HEXDIG, HEXDIG);
    public static final Pattern pchar = Patterns.or(unreserved, pctEncoded, subDelims, Patterns.regex("[:@]"));
    public static final Parser<String> segment = pchar.many().toScanner("segment").source();
    public static final Parser<String> query = Patterns.or(pchar, forwardSlash, questionMark).many().toScanner("query").source();
}
