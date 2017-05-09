package org.flint;

import java.io.IOException;

import javaslang.CheckedFunction1;
import javaslang.CheckedFunction2;

import org.flint.exception.HttpException;
import org.flint.request.Method;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;
import org.flint.routing.Route;
import org.flint.routing.RouteMatcher;
import org.flint.routing.UriTemplate;

public class Application {
    private RouteMatcher routeMatcher;
    private BeforeMiddleware beforeMiddleware;
    private AfterMiddleware afterMiddleware;

    public Application() {
        this.routeMatcher = new RouteMatcher();
        this.beforeMiddleware = new BeforeMiddleware();
        this.afterMiddleware = new AfterMiddleware();
    }

    public Route match(String method, String uriTemplate, CheckedFunction1<Request, Response> controller) {
        Route route = new Route(method, new UriTemplate(uriTemplate), controller);
        routeMatcher.addRoute(route);

        return route;
    }

    public Route get(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match(Method.GET, uriTemplate, controller);
    }

    public Route post(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match(Method.POST, uriTemplate, controller);
    }

    public Route put(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match(Method.PUT, uriTemplate, controller);
    }

    public Route delete(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match(Method.DELETE, uriTemplate, controller);
    }

    public Route options(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match(Method.OPTIONS, uriTemplate, controller);
    }

    public Route patch(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match(Method.PATCH, uriTemplate, controller);
    }

    public Application before(CheckedFunction1<Request, Request> handler) {
        beforeMiddleware = beforeMiddleware.enqueue(handler);
        return this;
    }

    public Application after(CheckedFunction2<Request, Response, Response> handler) {
        afterMiddleware = afterMiddleware.enqueue(handler);
        return this;
    }

    public void run(int port) throws IOException {
        Server server = new Server(this::requestHandler);

        server.run(port);
    }

    public Response requestHandler(Request request) {
        Response response;

        try {
            request = beforeMiddleware.applyMiddleware(request);
            response = routeMatcher.applyController(request);
            response = afterMiddleware.applyMiddleware(request, response);
        } catch (HttpException he) {
            response = defaultResponse(he.getStatusCode());
            he.getHeaders().forEach(response::setHeader);
        } catch (Throwable e) {
            response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
        }

        if (request.getMethod().equals(Method.HEAD)) {
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
