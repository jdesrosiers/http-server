package org.core.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import javaslang.collection.HashMap;
import javaslang.Tuple;
import javaslang.Tuple2;

import org.jparsec.pattern.Pattern;
import org.jparsec.pattern.Patterns;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;

import static org.core.parse.Abnf.*;
import static org.core.parse.Uri.*;

import org.core.OriginForm;
import org.core.Request;
import org.core.RequestTarget;

public class Http {
    private static final Parser<Void> forwardSlash = Patterns.string("/").toScanner("/");
    private static final Parser<Void> questionMark = Patterns.string("?").toScanner("?");
    private static final Parser<Void> colon = Patterns.string(":").toScanner(":");
    private static final Parser<Void> sp = SP.toScanner("SP");
    private static final Parser<Void> crlf = CRLF.toScanner("CRLF");

    public static final Pattern tchar = Patterns.or(DIGIT, ALPHA, Patterns.regex("[!#$%'*+\\-.^_`|~]"));
    public static final Pattern obsText = Patterns.regex("[\\x80-\\xFF]");
    public static final Pattern fieldVchar = Patterns.or(VCHAR, obsText);

    public static final Parser<Void> OWS = Patterns.or(SP, HTAB).many().toScanner("OWS");
    public static final Parser<String> token = tchar.many1().toScanner("token").source();
    public static final Parser<Void> HTTPVersion = Scanners.string("HTTP/1.1", "HTTP-version");
    public static final Parser<String> absolutePath = Parsers.sequence(forwardSlash, segment).many1().source();
    public static final Parser<OriginForm> originForm = Parsers.sequence(
            absolutePath,
            Parsers.sequence(
                questionMark,
                query,
                (_1, q) -> q
            ).optional(),
            (path, q) -> new OriginForm(path, q)
        );
    public static final Parser<RequestTarget> requestTarget = Parsers.or(originForm);
    public static final Parser<String> method = token;
    public static final Parser<Request> requestLine = Parsers.sequence(
            method, sp, requestTarget, sp, HTTPVersion, crlf,
            (m, _2, t, _3, _4, _5) -> new Request(m, t)
        );
    public static final Parser<String> fieldName = token;
    public static final Parser<String> fieldContent = Patterns.sequence(
            fieldVchar,
            Patterns.sequence(Patterns.or(SP, HTAB).many1(), fieldVchar).optional()
        ).toScanner("field-content").source();
    public static final Parser<Void> obsFold = Patterns.sequence(CRLF, Patterns.or(SP, HTAB).many1()).toScanner("obs-fold");
    public static final Parser<String> fieldValue = Parsers.or(fieldContent, obsFold).many().source();
    public static final Parser<Tuple2<String, String>> headerField = Parsers.sequence(fieldName, colon, OWS, fieldValue, OWS, (name, _colon, _ows, value, _ows2) -> Tuple.of(name, value));

    public static Request request(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);

        Request request = requestLine.parse(in.readLine() + "\r\n");

        String line = in.readLine();
        while (line.length() > 0) {
            Tuple2<String, String> header = headerField.parse(line);
            request.setHeader(header._1, header._2);
            line = in.readLine();
        }

        int contentLength = Integer.parseInt(request.getHeader("Content-Length").getOrElse("0"));
        if (contentLength > 0) {
            char[] body = new char[contentLength];
            in.read(body, 0, contentLength);
            request.setBody(new String(body));
        }

        return request;
    }
}
