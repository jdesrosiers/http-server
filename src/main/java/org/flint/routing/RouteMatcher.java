package org.flint.routing;

import javaslang.collection.Queue;

import org.flint.exception.NotFoundHttpException;
import org.flint.exception.MethodNotAllowedHttpException;
import org.flint.request.Request;

public class RouteMatcher {
    private Queue<Route> routes = Queue.empty();

    public void addRoute(Route route) {
        routes = routes.enqueue(route);
    }

    public Route getMatchFor(Request request) {
        Queue<Route> resourceMatches = routes.filter((route) -> isUriMatch(route, request));
        if (resourceMatches.isEmpty()) {
            throw new NotFoundHttpException();
        }

        return resourceMatches
            .filter((route) -> isMethodMatch(route, request))
            .getOrElseThrow(() -> {
                String allow = buildAllowHeader(resourceMatches);
                return new MethodNotAllowedHttpException(allow);
            });
    }

    private String buildAllowHeader(Queue<Route> matches) {
        return matches
            .map(Route::getMethod)
            .reduce((a, b) -> a + "," + b);
    }

    private boolean isUriMatch(Route route, Request request) {
        return route.getUriTemplate().getMatchFor(request.getPath()).isDefined();
    }

    private boolean isMethodMatch(Route route, Request request) {
        String routeMethod = route.getMethod();
        String requestMethod = request.getMethod().equals("HEAD") ? "GET" : request.getMethod();

        return routeMethod.equals(requestMethod);
    }
}
