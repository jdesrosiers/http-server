package org.flint;

import java.io.IOException;

import javaslang.collection.Queue;
import javaslang.CheckedFunction1;

import org.flint.exception.HttpException;

public class Application {
    private RouteMatcher routeMatcher = new RouteMatcher();

    public Route match(String method, String uriTemplate, CheckedFunction1<Request, Response> controller) {
        Route route = new Route(method, new UriTemplate(uriTemplate), controller);
        routeMatcher.addRoute(route);

        return route;
    }

    public Route get(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("GET", uriTemplate, controller);
    }

    public Route post(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("POST", uriTemplate, controller);
    }

    public Route put(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("PUT", uriTemplate, controller);
    }

    public Route delete(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("DELETE", uriTemplate, controller);
    }

    public Route options(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("OPTIONS", uriTemplate, controller);
    }

    public Route patch(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("PATCH", uriTemplate, controller);
    }

    public void run(int port) throws IOException {
        Server server = new Server(this::requestHandler);

        server.run(port);
    }

    public Response requestHandler(Request request) {
        Response response;

        try {
            Logger.logRequest(request);
            response = routeMatcher.getMatchFor(request).getController().apply(request);
        } catch (HttpException he) {
            response = defaultResponse(he.getStatusCode());
            he.getHeaders().forEach(response::setHeader);
        } catch (Throwable e) {
            response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
        }

        if (request.getMethod().equals("HEAD")) {
            response.removeBody();
        }

        return response;
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
