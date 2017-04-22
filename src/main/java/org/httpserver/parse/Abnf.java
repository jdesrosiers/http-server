package org.httpserver.parse;

import org.jparsec.pattern.Pattern;
import org.jparsec.pattern.Patterns;

// RFC 5234
public class Abnf {
    public static final Pattern ALPHA = Patterns.regex("[a-zA-Z]");
    public static final Pattern BIT = Patterns.regex("[01]");
    public static final Pattern CHAR = Patterns.regex("[\\x01-\\x7F]");
    public static final Pattern CR = Patterns.string("\r");
    public static final Pattern LF = Patterns.string("\n");
    public static final Pattern CRLF = Patterns.sequence(CR, LF);
    public static final Pattern CTL = Patterns.regex("[\\x00-\\x1F\\x7F]");
    public static final Pattern DIGIT = Patterns.regex("[0-9]");
    public static final Pattern DQUOTE = Patterns.string("\"");
    public static final Pattern HEXDIG = Patterns.or(DIGIT, Patterns.regex("[a-fA-F]"));
    public static final Pattern HTAB = Patterns.string("\t");
    public static final Pattern SP = Patterns.string(" ");
    public static final Pattern WSP = Patterns.or(SP, HTAB);
    public static final Pattern LWSP = Patterns.or(WSP, Patterns.sequence(CRLF, WSP)).many();
    public static final Pattern VCHAR = Patterns.regex("[\\x21-\\x7E]");
}
