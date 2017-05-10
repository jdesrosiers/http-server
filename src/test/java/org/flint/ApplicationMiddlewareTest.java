package org.flint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import javaslang.control.Option;

import org.flint.request.OriginForm;
import org.flint.request.Method;
import org.flint.request.Request;
import org.flint.response.Response;

public class ApplicationMiddlewareTest {

    @Test
    public void itShouldCallApplicationBeforeMiddleware() {
        Application app = new Application()
            .before(request -> {
                request.setHeader("Foo", "bar");
                return request;
            })
            .before(request -> {
                request.setHeader("Bar", "foo");
                return request;
            });

        app.get("/foo", (request) -> Response.create());

        Request request = new Request(Method.GET, new OriginForm("/foo"));
        app.requestHandler(request);

        assertThat(request.getHeader("Foo"), equalTo(Option.of("bar")));
        assertThat(request.getHeader("Bar"), equalTo(Option.of("foo")));
    }

    @Test
    public void itShouldCallApplicationAfterMiddleware() {
        Application app = new Application()
            .after((request, response) -> {
                response.setHeader("Foo", "bar");
                return response;
            })
            .after((request, response) -> {
                response.setHeader("Bar", "foo");
                return response;
            });

        app.get("/foo", (request) -> Response.create());

        Request request = new Request(Method.GET, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getHeader("Bar"), equalTo(Option.of("foo")));
    }

    @Test
    public void itShouldCallRouteBeforeMiddleware() {
        Application app = new Application();

        app.get("/foo", (request) -> Response.create())
            .before(request -> {
                request.setHeader("Foo", "bar");
                return request;
            })
            .before(request -> {
                request.setHeader("Bar", "foo");
                return request;
            });

        Request request = new Request(Method.GET, new OriginForm("/foo"));
        app.requestHandler(request);

        assertThat(request.getHeader("Foo"), equalTo(Option.of("bar")));
        assertThat(request.getHeader("Bar"), equalTo(Option.of("foo")));
    }

    @Test
    public void itShouldNotCallRouteBeforeMiddlewareOnOtherRoutes() {
        Application app = new Application();

        app.get("/foo", (request) -> Response.create())
            .before(request -> {
                request.setHeader("Foo", "bar");
                return request;
            });
        app.get("/bar", (request) -> Response.create());

        Request request = new Request(Method.GET, new OriginForm("/bar"));
        app.requestHandler(request);

        assertThat(request.getHeader("Foo"), equalTo(Option.none()));
    }

    @Test
    public void itShouldCallRouteAfterMiddleware() {
        Application app = new Application();

        app.get("/foo", (request) -> Response.create())
            .after((request, response) -> {
                response.setHeader("Foo", "bar");
                return response;
            })
            .after((request, response) -> {
                response.setHeader("Bar", "foo");
                return response;
            });

        Request request = new Request(Method.GET, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getHeader("Foo"), equalTo(Option.of("bar")));
        assertThat(response.getHeader("Bar"), equalTo(Option.of("foo")));
    }

    @Test
    public void itShouldNotCallRouteAfterMiddlewareOnOtherRoutes() {
        Application app = new Application();

        app.get("/foo", (request) -> Response.create())
            .after((request, response) -> {
                response.setHeader("Foo", "bar");
                return response;
            });
        app.get("/bar", (request) -> Response.create());

        Request request = new Request(Method.GET, new OriginForm("/bar"));
        Response response = app.requestHandler(request);

        assertThat(response.getHeader("Foo"), equalTo(Option.none()));
    }

}
