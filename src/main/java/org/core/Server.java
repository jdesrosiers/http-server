package org.core;

import javaslang.collection.List;
import static javaslang.API.*;
import static javaslang.Patterns.*;

public class Server {
    List<Route> routes;

    public Server(List<Route> routes) {
        this.routes = routes;
    }

    public Response handle(Request request) {
        Response response;
        List<Route> resourceMatches = routes.filter((route) -> isUriMatch(route, request));

        if (resourceMatches.isEmpty()) {
            response = defaultResponse(StatusCode.NOT_FOUND);
        } else {
            List<Route> methodMatches = resourceMatches.filter((route) -> isMethodMatch(route, request));

            if (methodMatches.isEmpty()) {
                response = defaultResponse(StatusCode.METHOD_NOT_ALLOWED);
                response.setHeader("Allow", buildAllowHeader(resourceMatches));
            } else {
                response = methodMatches.head().getController().apply(request);
            }
        }

        if (request.getMethod().equals("HEAD")) {
            response.removeBody();
        }

        return response;
    }

    private String buildAllowHeader(List<Route> matches) {
        return matches
            .map(Route::getMethod)
            .reduce((a, b) -> a + "," + b);
    }

    private boolean isUriMatch(Route route, Request request) {
        return Match(route.getUriTemplate().match(request.getRequestTarget().getPath())).of(
            Case(Some($()), true),
            Case(None(), false)
        );
    }

    private boolean isMethodMatch(Route route, Request request) {
        String routeMethod = route.getMethod();
        String requestMethod = request.getMethod().equals("HEAD") ? "GET" : request.getMethod();

        return routeMethod.equals(requestMethod);
    }

    private Response defaultResponse(int statusCode) {
        Response response = Response.create(statusCode);
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody(defaultBody(response));

        return response;
    }

    private String defaultBody(Response response) {
        int status = response.getStatusCode();
        String message = String.format("%s %s", status, StatusCode.getMessage(status).get());
        String template = "<html><head><title>%s</title></head><body><h1>%s</h1></body></html>";
        return String.format(template, message, message);
    }
}
