package org.flint;

import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Option;
import javaslang.Tuple;

public class Request {
    private String method;
    private RequestTarget requestTarget;
    private Map<String, String> headers;
    private String body;

    public Request(String method, RequestTarget requestTarget) {
        this.method = method;
        this.requestTarget = requestTarget;
        this.headers = HashMap.empty();
        this.body = "";
    }

    public String getMethod() {
        return method;
    }

    public RequestTarget getRequestTarget() {
        return requestTarget;
    }

    public Option<FormUrlencoded> getQuery() {
        return requestTarget.getQuery().map(FormUrlencoded::new);
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
