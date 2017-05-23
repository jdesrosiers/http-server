package org.flint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;

import org.junit.Test;

import javaslang.control.Option;

import org.flint.request.OriginForm;
import org.flint.request.Method;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

public class ApplicationTest {

    @Test
    public void itShouldHandleASimpleGET() {
        Application app = new Application();

        app.get("/foo", (request) -> {
            Response response = Response.create();
            response.setBody("foo");

            return response;
        });

        Request request = new Request(Method.GET, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getContentLength(), equalTo(3));
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

        Request request = new Request(Method.GET, new OriginForm("/foo", "bar"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getContentLength(), equalTo(3));
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

        Request request = new Request(Method.HEAD, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getContentLength(), equalTo(3));
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

        Request request = new Request(Method.POST, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getContentLength(), equalTo(3));
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

        Request request = new Request(Method.PUT, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getContentLength(), equalTo(3));
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

        Request request = new Request(Method.DELETE, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getContentLength(), equalTo(3));
        assertThat(response.getBodyAsString(), equalTo("foo"));
    }

    @Test
    public void itShouldHandleASimpleOPTIONS() {
        Application app = new Application();

        app.options("/foo", (request) -> {
            Response response = Response.create();
            response.setHeader("Allow", Method.GET + "," + Method.POST);
            response.setBody("foo");

            return response;
        });

        Request request = new Request(Method.OPTIONS, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Allow"), equalTo(Option.of(Method.GET + "," + Method.POST)));
        assertThat(response.getContentLength(), equalTo(3));
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

        Request request = new Request(Method.PATCH, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NO_CONTENT));
        assertThat(response.getHeader("ETag"), equalTo(Option.of("foo")));
        assertThat(response.getContentLength(), equalTo(0));
        assertThat(response.getBodyAsString(), equalTo(""));
    }

    @Test
    public void itShould404WhenANonexistentResourceIsRequested() {
        Application app = new Application();

        Request request = new Request(Method.GET, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
        assertThat(response.getBodyAsString(), containsString("404 Not Found"));
    }

    @Test
    public void itShould404WhenANonexistentResourceIsHeadRequested() {
        Application app = new Application();

        Request request = new Request(Method.HEAD, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
        assertThat(response.getContentLength(), greaterThan(0));
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

        Request request = new Request(Method.GET, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.METHOD_NOT_ALLOWED));
        assertThat(response.getHeader("Allow"), equalTo(Option.of(Method.POST)));
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

        Request request = new Request(Method.DELETE, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.METHOD_NOT_ALLOWED));
        assertThat(response.getHeader("Allow"), equalTo(Option.of(Method.GET + "," + Method.HEAD + "," + Method.POST)));
        assertThat(response.getBodyAsString(), containsString("405 Method Not Allowed"));
    }

}
