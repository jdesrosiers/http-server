package org.flint.routing;

import javaslang.collection.Queue;

import org.flint.exception.NotFoundHttpException;
import org.flint.exception.MethodNotAllowedHttpException;
import org.flint.request.Method;
import org.flint.request.Request;
import org.flint.response.Response;

public class RouteMatcher {
    private Queue<Route> routes = Queue.empty();

    public void addRoute(Route route) {
        routes = routes.enqueue(route);
    }

    public Response applyController(Request request) throws Throwable {
        return getMatchFor(request).applyController(request);
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
        String requestMethod = request.getMethod().equals(Method.HEAD) ? Method.GET : request.getMethod();

        return routeMethod.equals(requestMethod);
    }
}
