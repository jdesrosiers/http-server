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

    @Override
    public String toString() {
        return String.format("%s %s HTTP/1.1\r\n", method, requestTarget);
    }
}
