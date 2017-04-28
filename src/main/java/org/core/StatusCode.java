package org.core;

import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Option;
import javaslang.Tuple;

public class StatusCode {
    public static final int CONTINUE = 100;
    public static final int SWITCHING_PROTOCOLS = 101;

    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NON_AUTHORITATIVE_INFORMATION = 203;
    public static final int NO_CONTENT = 204;
    public static final int RESET_CONTENT = 205;
    public static final int PARTIAL_CONTENT = 206;

    public static final int MULTIPLE_CHOICES = 300;
    public static final int MOVED_PERMANENTLY = 301;
    public static final int FOUND = 302;
    public static final int SEE_OTHER = 303;
    public static final int NOT_MODIFIED = 304;
    public static final int USE_PROXY = 305;
    public static final int TEMPORARY_REDIRECT = 307;

    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int PAYMENT_REQUIRED = 402;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int NOT_ACCEPTABLE = 406;
    public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int CONFLICT = 409;
    public static final int GONE = 410;
    public static final int LENGTH_REQUIRED = 411;
    public static final int PRECONDITION_FAILED = 412;
    public static final int PAYLOAD_TOO_LARGE = 413;
    public static final int URI_TOO_LONG = 414;
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;
    public static final int RANGE_NOT_SATISFIABLE = 416;
    public static final int EXPECTATION_FAILED = 417;
    public static final int IM_A_TEAPOT = 418;
    public static final int UNPROCESSABLE_ENTITY = 422;
    public static final int UPGRADE_REQUIRED = 426;

    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;
    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

    private static final Map messages = HashMap.ofEntries(
        // 1XX
        Tuple.of(CONTINUE, "Continue"),
        Tuple.of(SWITCHING_PROTOCOLS, "Switching Protocols"),

        // 2XX
        Tuple.of(OK, "OK"),
        Tuple.of(CREATED, "Created"),
        Tuple.of(ACCEPTED, "Accepted"),
        Tuple.of(NON_AUTHORITATIVE_INFORMATION, "Non-Authoritative Information"),
        Tuple.of(NO_CONTENT, "No Content"),
        Tuple.of(RESET_CONTENT, "Reset Content"),
        Tuple.of(PARTIAL_CONTENT, "Partial Content"),

        // 3XX
        Tuple.of(MULTIPLE_CHOICES, "Multiple Choices"),
        Tuple.of(MOVED_PERMANENTLY, "Moved Permanently"),
        Tuple.of(FOUND, "Found"),
        Tuple.of(SEE_OTHER, "See Other"),
        Tuple.of(NOT_MODIFIED, "Not Modified"),
        Tuple.of(USE_PROXY, "Use Proxy"),
        Tuple.of(TEMPORARY_REDIRECT, "Temporary Redirect"),

        // 4XX
        Tuple.of(BAD_REQUEST, "Bad Request"),
        Tuple.of(UNAUTHORIZED, "Unauthorized"),
        Tuple.of(PAYMENT_REQUIRED, "Payment Required"),
        Tuple.of(FORBIDDEN, "Forbidden"),
        Tuple.of(NOT_FOUND, "Not Found"),
        Tuple.of(METHOD_NOT_ALLOWED, "Method Not Allowed"),
        Tuple.of(NOT_ACCEPTABLE, "Not Acceptable"),
        Tuple.of(PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication Required"),
        Tuple.of(REQUEST_TIMEOUT, "Request Timeout"),
        Tuple.of(CONFLICT, "Conflict"),
        Tuple.of(GONE, "Gone"),
        Tuple.of(LENGTH_REQUIRED, "Length Required"),
        Tuple.of(PRECONDITION_FAILED, "Precondition Failed"),
        Tuple.of(PAYLOAD_TOO_LARGE, "Payload Too Large"),
        Tuple.of(URI_TOO_LONG, "URI Too Long"),
        Tuple.of(UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type"),
        Tuple.of(RANGE_NOT_SATISFIABLE, "Range Not Satisfiable"),
        Tuple.of(EXPECTATION_FAILED, "Expectation Failed"),
        Tuple.of(IM_A_TEAPOT, "I'm a teapot"),
        Tuple.of(UNPROCESSABLE_ENTITY, "Unprocessable Entity"),
        Tuple.of(UPGRADE_REQUIRED, "Upgrade Required"),

        // 5XX
        Tuple.of(INTERNAL_SERVER_ERROR, "Internal Server Error"),
        Tuple.of(NOT_IMPLEMENTED, "Not Implemented"),
        Tuple.of(BAD_GATEWAY, "Bad Gateway"),
        Tuple.of(SERVICE_UNAVAILABLE, "Service Unavailable"),
        Tuple.of(GATEWAY_TIMEOUT, "Gateway Timeout"),
        Tuple.of(HTTP_VERSION_NOT_SUPPORTED, "HTTP Version Not Supported")
    );

    public static final Option<String> getMessage(int statusCode) {
        return messages.get(statusCode);
    }
}
