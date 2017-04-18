package org.httpserver;

import javaslang.Function1;

public class Route {
    private String method;
    private UriTemplate uriTemplate;
    private Function1<Request, Response> controller;

    public Route(String method, UriTemplate uriTemplate, Function1<Request, Response> controller) {
        this.method = method;
        this.uriTemplate = uriTemplate;
        this.controller = controller;
    }

    public String getMethod() {
        return method;
    }

    public UriTemplate getUriTemplate() {
        return uriTemplate;
    }

    public Function1<Request, Response> getController() {
        return controller;
    }
}
