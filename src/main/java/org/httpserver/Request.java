package org.httpserver;

public class Request {
    private String method;
    private String requestTarget;

    public Request(String method, String requestTarget) {
        this.method = method;
        this.requestTarget = requestTarget;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }
}
