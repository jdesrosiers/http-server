package org.httpserver;

import javaslang.collection.Map;
import javaslang.control.Option;
import javaslang.Tuple;

public class Request {
    private String method;
    private String requestTarget;
    private Map<String, String> headers;
    private String body;

    public Request(String method, String requestTarget, Map<String, String> headers, String body) {
        this.method = method;
        this.requestTarget = requestTarget;
        this.headers = headers.map((key, value) -> Tuple.of(key.toLowerCase(), value));
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public Option<String> getHeader(String name) {
        return headers.get(name.toLowerCase());
    }

    public String getBody() {
        return body;
    }

    public void setHeader(String name, String value) {
        headers = headers.put(name.toLowerCase(), value);
    }

    public void setBody(String body) {
        this.body = body;
    }
}
