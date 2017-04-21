package org.httpserver;

import javaslang.collection.Map;

public class Request {
    private String method;
    private String requestTarget;
    private Map<String, String> headers;
    private String body;

    public Request(String method, String requestTarget, Map<String, String> headers, String body) {
        this.method = method;
        this.requestTarget = requestTarget;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return String.format("%s %s HTTP/1.1\r\n", method, requestTarget);
    }
}
