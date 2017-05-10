package org.flint.routing;

import javaslang.CheckedFunction1;
import javaslang.CheckedFunction2;

import org.flint.middleware.AfterMiddleware;
import org.flint.middleware.BeforeMiddleware;
import org.flint.request.Request;
import org.flint.response.Response;

public class Route {
    private String method;
    private UriTemplate uriTemplate;
    private CheckedFunction1<Request, Response> controller;
    private BeforeMiddleware beforeMiddleware;
    private AfterMiddleware afterMiddleware;

    public Route(String method, UriTemplate uriTemplate, CheckedFunction1<Request, Response> controller) {
        this.method = method;
        this.uriTemplate = uriTemplate;
        this.controller = controller;
        this.beforeMiddleware = new BeforeMiddleware();
        this.afterMiddleware = new AfterMiddleware();
    }

    public String getMethod() {
        return method;
    }

    public UriTemplate getUriTemplate() {
        return uriTemplate;
    }

    public Response applyController(Request request) throws Throwable {
        request = beforeMiddleware.applyAll(request);
        Response response = controller.apply(request);
        return afterMiddleware.applyAll(request, response);
    }

    public Route before(CheckedFunction1<Request, Request> handler) {
        beforeMiddleware = beforeMiddleware.enqueue(handler);
        return this;
    }

    public Route after(CheckedFunction2<Request, Response, Response> handler) {
        afterMiddleware = afterMiddleware.enqueue(handler);
        return this;
    }
}
