package org.flint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;

import org.junit.Test;

import javaslang.collection.HashMap;
import javaslang.control.Option;

public class ApplicationTest {

    @Test
    public void itShouldHandleASimpleGET() {
        Application app = new Application();

        app.get("/foo", (request) -> {
            Response response = Response.create();
            response.setBody("foo");

            return response;
        });

        Server server = app.getServer();

        Request request = new Request("GET", new OriginForm("/foo"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBodyAsString(), equalTo("foo"));
    }

    @Test
    public void itShouldMatchRoutesWithoutQueryParams() {
        Application app = new Application();

        app.get("/foo", (request) -> {
            Response response = Response.create();
            response.setBody("foo");

            return response;
        });

        Server server = app.getServer();

        Request request = new Request("GET", new OriginForm("/foo", "bar"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBodyAsString(), equalTo("foo"));
    }

    @Test
    public void itShouldHandleASimpleHEAD() {
        Application app = new Application();

        app.get("/foo", (request) -> {
            Response response = Response.create();
            response.setBody("foo");

            return response;
        });

        Server server = app.getServer();

        Request request = new Request("HEAD", new OriginForm("/foo"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBodyAsString(), equalTo(""));
    }

    @Test
    public void itShouldHandleASimplePOST() {
        Application app = new Application();

        app.post("/foo", (request) -> {
            Response response = Response.create();
            response.setBody("foo");

            return response;
        });

        Server server = app.getServer();

        Request request = new Request("POST", new OriginForm("/foo"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBodyAsString(), equalTo("foo"));
    }

    @Test
    public void itShouldHandleASimplePUT() {
        Application app = new Application();

        app.put("/foo", (request) -> {
            Response response = Response.create();
            response.setBody("foo");

            return response;
        });

        Server server = app.getServer();

        Request request = new Request("PUT", new OriginForm("/foo"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBodyAsString(), equalTo("foo"));
    }

    @Test
    public void itShouldHandleASimpleDELETE() {
        Application app = new Application();

        app.delete("/foo", (request) -> {
            Response response = Response.create();
            response.setBody("foo");

            return response;
        });

        Server server = app.getServer();

        Request request = new Request("DELETE", new OriginForm("/foo"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBodyAsString(), equalTo("foo"));
    }

    @Test
    public void itShouldHandleASimpleOPTIONS() {
        Application app = new Application();

        app.options("/foo", (request) -> {
            Response response = Response.create();
            response.setHeader("Allow", "GET,POST");
            response.setBody("foo");

            return response;
        });

        Server server = app.getServer();

        Request request = new Request("OPTIONS", new OriginForm("/foo"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Allow"), equalTo(Option.of("GET,POST")));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBodyAsString(), equalTo("foo"));
    }

    @Test
    public void itShouldHandleASimplePATCH() {
        Application app = new Application();

        app.patch("/foo", (request) -> {
            Response response = Response.create(StatusCode.NO_CONTENT);
            response.setHeader("ETag", "foo");

            return response;
        });

        Server server = app.getServer();

        Request request = new Request("PATCH", new OriginForm("/foo"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NO_CONTENT));
        assertThat(response.getHeader("ETag"), equalTo(Option.of("foo")));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("0")));
        assertThat(response.getBodyAsString(), equalTo(""));
    }

    @Test
    public void itShould404WhenANonexistentResourceIsRequested() {
        Application app = new Application();

        Server server = app.getServer();

        Request request = new Request("GET", new OriginForm("/foo"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
        assertThat(response.getBodyAsString(), containsString("404 Not Found"));
    }

    @Test
    public void itShould404WhenANonexistentResourceIsHeadRequested() {
        Application app = new Application();

        Server server = app.getServer();

        Request request = new Request("HEAD", new OriginForm("/foo"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
        assertThat(Integer.parseInt(response.getHeader("Content-Length").get()), greaterThan(0));
        assertThat(response.getBodyAsString(), equalTo(""));
    }

    @Test
    public void itShould405WhenResourceExistsButMethodIsNotAllowed() {
        Application app = new Application();

        app.post("/foo", (request) -> {
            Response response = Response.create();
            response.setBody("foo");

            return response;
        });

        Server server = app.getServer();

        Request request = new Request("GET", new OriginForm("/foo"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.METHOD_NOT_ALLOWED));
        assertThat(response.getHeader("Allow"), equalTo(Option.of("POST")));
        assertThat(response.getBodyAsString(), containsString("405 Method Not Allowed"));
    }

    @Test
    public void itShouldHaveAnAllowHeaderWithAllAllowedMethodsWhenResponseIs405() {
        Application app = new Application();

        app.get("/foo", (request) -> {
            Response response = Response.create();
            response.setBody("foo");

            return response;
        });

        app.post("/foo", (request) -> {
            return Response.create();
        });

        Server server = app.getServer();

        Request request = new Request("DELETE", new OriginForm("/foo"));
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.METHOD_NOT_ALLOWED));
        assertThat(response.getHeader("Allow"), equalTo(Option.of("GET,POST")));
        assertThat(response.getBodyAsString(), containsString("405 Method Not Allowed"));
    }

}