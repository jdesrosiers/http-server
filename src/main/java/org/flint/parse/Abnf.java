package org.flint.parse;

import org.jparsec.pattern.Patterns;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;

// RFC 5234
public class Abnf {
    public static final Parser<String> ALPHA = Patterns.regex("[a-zA-Z]").toScanner("ALPHA").source();
    public static final Parser<String> BIT = Scanners.among("01").label("BIT").source();
    public static final Parser<String> CHAR = Patterns.regex("[\\x01-\\x7F]").toScanner("CHAR").source();
    public static final Parser<String> CR = Scanners.isChar('\r').label("CR").source();
    public static final Parser<String> LF = Scanners.isChar('\n').label("LF").source();
    public static final Parser<String> CRLF = Parsers.sequence(CR, LF).label("CRLF").source();
    public static final Parser<String> CTL = Patterns.regex("[\\x00-\\x1F\\x7F]").toScanner("CTL").source();
    public static final Parser<String> DIGIT = Patterns.regex("[0-9]").toScanner("DIGIT").source();
    public static final Parser<String> DQUOTE = Scanners.isChar('"').label("DQUOTE").source();
    public static final Parser<String> HEXDIG = Parsers.or(
            DIGIT,
            Patterns.regex("[a-fA-F]").toScanner("A-F")
        ).label("HEXDIG").source();
    public static final Parser<String> HTAB = Scanners.isChar('\t').label("HTAB").source();
    public static final Parser<String> SP = Scanners.isChar(' ', "SP").source();
    public static final Parser<String> WSP = Parsers.or(
            SP,
            HTAB
        ).label("WSP").source();
    public static final Parser<String> LWSP = Parsers.or(
            WSP,
            Parsers.sequence(CRLF, WSP)
        ).many()
        .label("LWSP").source();
    public static final Parser<String> VCHAR = Patterns.regex("[\\x21-\\x7E]").toScanner("VCHAR").source();
}
