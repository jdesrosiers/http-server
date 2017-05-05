package org.flint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import org.junit.Test;

import javaslang.control.Option;

import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

public class ServerTest {

    @Test
    public void isShouldParseARequestWithoutABody() throws IOException {
        StringBuilder message = new StringBuilder();
        message.append("GET /hello.txt HTTP/1.1\r\n");
        message.append("Accept: text/plain; charset=utf-8\r\n");
        message.append("\r\n");
        ByteArrayInputStream in = new ByteArrayInputStream(message.toString().getBytes());
        InputStreamReader reader = new InputStreamReader(in);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Server server = new Server(request -> {
            assertThat(request.getMethod(), equalTo("GET"));
            assertThat(request.getPath(), equalTo("/hello.txt"));
            assertThat(request.getHeader("Accept"), equalTo(Option.of("text/plain; charset=utf-8")));

            return Response.create();
        });
        server.handleHttpMessage(reader, os);
    }

    @Test
    public void isShouldParseARequestWithABody() throws IOException {
        StringBuilder message = new StringBuilder();
        message.append("POST /form HTTP/1.1\r\n");
        message.append("Content-Length: 20\r\n");
        message.append("Content-Type: application/json\r\n");
        message.append("\r\n");
        message.append("{ \"hello\": \"world\" }\r\n");
        ByteArrayInputStream in = new ByteArrayInputStream(message.toString().getBytes());
        InputStreamReader reader = new InputStreamReader(in);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Server server = new Server(request -> {
            assertThat(request.getMethod(), equalTo("POST"));
            assertThat(request.getPath(), equalTo("/form"));
            assertThat(request.getHeader("Content-Length"), equalTo(Option.of("20")));
            assertThat(request.getHeader("Content-Type"), equalTo(Option.of("application/json")));
            assertThat(request.getBody(), equalTo("{ \"hello\": \"world\" }"));

            return Response.create();
        });
        server.handleHttpMessage(reader, os);
    }

    @Test
    public void itShouldGenerateAnHttpMessageRepresentationFor200OK() throws IOException {
        String requestMessage = "GET /hello.txt HTTP/1.1\r\n\r\n";
        ByteArrayInputStream in = new ByteArrayInputStream(requestMessage.getBytes());
        InputStreamReader reader = new InputStreamReader(in);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        StringBuilder expected = new StringBuilder();
        expected.append("HTTP/1.1 200 OK\r\n");
        expected.append("Content-Type: text/plain; charset=utf-8\r\n");
        expected.append("Content-Length: 3\r\n");
        expected.append("\r\n");
        expected.append("foo\r\n");

        Server server = new Server(request -> {
            Response response = Response.create();
            response.setHeader("Content-Type", "text/plain; charset=utf-8");
            response.setBody("foo");
            return response;
        });
        server.handleHttpMessage(reader, os);

        assertThat(os.toString(), equalTo(expected.toString()));
    }

    @Test
    public void itShouldGenerateAnHttpMessageRepresentationFor404NotFound() throws IOException {
        String requestMessage = "GET /hello.txt HTTP/1.1\r\n\r\n";
        ByteArrayInputStream in = new ByteArrayInputStream(requestMessage.getBytes());
        InputStreamReader reader = new InputStreamReader(in);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        StringBuilder expected = new StringBuilder();
        expected.append("HTTP/1.1 404 Not Found\r\n");
        expected.append("Content-Length: 0\r\n");
        expected.append("\r\n");

        Server server = new Server(request -> {
            return Response.create(StatusCode.NOT_FOUND);
        });
        server.handleHttpMessage(reader, os);

        assertThat(os.toString(), equalTo(expected.toString()));
    }

    @Test
    public void itShouldRespondWithABadRequestIfItCantParseTheRequest() throws IOException {
        String requestMessage = "/hello.txt HTTP/1.1\r\n\r\n";
        ByteArrayInputStream in = new ByteArrayInputStream(requestMessage.toString().getBytes());
        InputStreamReader reader = new InputStreamReader(in);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Server server = new Server(request -> Response.create());
        server.handleHttpMessage(reader, os);

        assertThat(os.toString(), containsString("HTTP/1.1 400 Bad Request"));
    }

    @Test
    public void itShouldRespondWithAnInternalErrorIfAnUncaughtExceptionGetsThrough() throws IOException {
        String requestMessage = "GET /hello.txt HTTP/1.1\r\n\r\n";
        ByteArrayInputStream in = new ByteArrayInputStream(requestMessage.getBytes());
        InputStreamReader reader = new InputStreamReader(in);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Server server = new Server(request -> {
            throw new RuntimeException();
        });
        server.handleHttpMessage(reader, os);

        assertThat(os.toString(), containsString("HTTP/1.1 500 Internal Server Error"));
    }

}
