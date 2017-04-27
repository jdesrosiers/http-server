package org.core;

import javaslang.CheckedFunction1;

public class Route {
    private String method;
    private UriTemplate uriTemplate;
    private CheckedFunction1<Request, Response> controller;

    public Route(String method, UriTemplate uriTemplate, CheckedFunction1<Request, Response> controller) {
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

    public CheckedFunction1<Request, Response> getController() {
        return controller;
    }
}
