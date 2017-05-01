package org.flint.exception;

import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Option;

import org.flint.Request;
import org.flint.StatusCode;

public class HttpException extends RuntimeException {
    private int statusCode;
    private HashMap<String, String> headers;

    public HttpException(int statusCode) {
        super(String.format("%s %s", statusCode, StatusCode.getMessage(statusCode).getOrElse("")));

        this.statusCode = statusCode;
        this.headers = HashMap.empty();
    }

    public HttpException(int statusCode, String message) {
        super(message);

        this.statusCode = statusCode;
        this.headers = HashMap.empty();
    }

    public HttpException(int statusCode, String message, Throwable cause) {
        super(message, cause);

        this.statusCode = statusCode;
        this.headers = HashMap.empty();
    }

    public HttpException(int statusCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

        this.statusCode = statusCode;
        this.headers = HashMap.empty();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setHeader(String key, String value) {
        headers = headers.put(key, value);
    }

    public Option<String> getHeader(String key) {
        return headers.get(key);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
