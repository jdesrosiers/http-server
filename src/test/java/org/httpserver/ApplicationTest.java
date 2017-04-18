package org.httpserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;
import javaslang.collection.HashMap;

@RunWith(DataProviderRunner.class)
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

        Request request = new Request("GET", "/foo", HashMap.empty(), "");
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBody(), equalTo("foo"));
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

        Request request = new Request("HEAD", "/foo", HashMap.empty(), "");
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBody(), equalTo(""));
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

        Request request = new Request("POST", "/foo", HashMap.empty(), "");
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBody(), equalTo("foo"));
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

        Request request = new Request("PUT", "/foo", HashMap.empty(), "");
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBody(), equalTo("foo"));
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

        Request request = new Request("DELETE", "/foo", HashMap.empty(), "");
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBody(), equalTo("foo"));
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

        Request request = new Request("OPTIONS", "/foo", HashMap.empty(), "");
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Allow"), equalTo(Option.of("GET,POST")));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
        assertThat(response.getBody(), equalTo("foo"));
    }

    @Test
    public void itShould404WhenANonexistentResourceIsRequested() {
        Application app = new Application();

        Server server = app.getServer();

        Request request = new Request("GET", "/foo", HashMap.empty(), "");
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
        assertThat(response.getBody(), containsString("404 Not Found"));
    }

    @Test
    public void itShould404WhenANonexistentResourceIsHeadRequested() {
        Application app = new Application();

        Server server = app.getServer();

        Request request = new Request("HEAD", "/foo", HashMap.empty(), "");
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
        assertThat(Integer.parseInt(response.getHeader("Content-Length").get()), greaterThan(0));
        assertThat(response.getBody(), equalTo(""));
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

        Request request = new Request("GET", "/foo", HashMap.empty(), "");
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.METHOD_NOT_ALLOWED));
        assertThat(response.getHeader("Allow"), equalTo(Option.of("POST")));
        assertThat(response.getBody(), containsString("405 Method Not Allowed"));
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

        Request request = new Request("DELETE", "/foo", HashMap.empty(), "");
        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.METHOD_NOT_ALLOWED));
        assertThat(response.getHeader("Allow"), equalTo(Option.of("GET,POST")));
        assertThat(response.getBody(), containsString("405 Method Not Allowed"));
    }

}
