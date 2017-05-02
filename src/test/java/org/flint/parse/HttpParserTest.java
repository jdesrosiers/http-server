package org.flint.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import javaslang.collection.HashMap;
import javaslang.control.Option;
import javaslang.Tuple;
import javaslang.Tuple2;

import org.flint.Request;
import org.flint.RequestTarget;

@RunWith(DataProviderRunner.class)
public class HttpParserTest {

    @Test
    public void itShouldParseARequest() {
        Request request = Http.requestLine.parse("GET /hello.txt HTTP/1.1\r\n");

        assertThat(request, instanceOf(Request.class));
        assertThat(request.getMethod(), equalTo("GET"));
        assertThat(request.getRequestTarget().getPath(), equalTo("/hello.txt"));
    }

    @DataProvider
    public static Object[][] dataProviderAbsolutePath() {
        return new Object[][] {
            { "/" },
            { "/hello" },
            { "/hello.txt" },
            { "/foo/hello.txt" }
        };
    }

    @Test
    @UseDataProvider("dataProviderAbsolutePath")
    public void itShouldParseAbsolutePath(String subject) {
        assertThat(Http.absolutePath.parse(subject), equalTo(subject));
    }

    @DataProvider
    public static Object[][] dataProviderRequestTarget() {
        return new Object[][] {
            { "/", "/", null },
            { "/hello", "/hello", null },
            { "/hello.txt", "/hello.txt", null },
            { "/hello.txt?", "/hello.txt", "" },
            { "/hello.txt?foo", "/hello.txt", "foo" },
            { "/hello.txt?foo=bar", "/hello.txt", "foo=bar" },
            { "/hello.txt?foo=bar&abc=123", "/hello.txt", "foo=bar&abc=123" }
        };
    }

    @Test
    @UseDataProvider("dataProviderRequestTarget")
    public void itShouldParseRequestTarget(String subject, String path, String query) {
        RequestTarget requestTarget = Http.requestTarget.parse(subject);
        assertThat(requestTarget.getPath(), equalTo(path));
        assertThat(requestTarget.getQuery(), equalTo(Option.of(query)));
    }

    @DataProvider
    public static Object[][] dataProviderHeaders() {
        return new Object[][] {
            { "Content-Length: 12", Tuple.of("Content-Length", "12") },
            { "Content-Type: text/plain; charset=utf-8", Tuple.of("Content-Type", "text/plain; charset=utf-8") }
        };
    }

    @Test
    @UseDataProvider("dataProviderHeaders")
    public void itShouldParseHeaders(String header, Tuple2<String, String> expected) {
        assertThat(Http.headerField.parse(header), equalTo(expected));
    }

    @Test
    public void isShouldParseARequestWithoutABody() throws IOException {
        StringBuilder message = new StringBuilder();
        message.append("GET /hello.txt HTTP/1.1\r\n");
        message.append("Accept: text/plain; charset=utf-8\r\n");
        message.append("\r\n");
        ByteArrayInputStream in = new ByteArrayInputStream(message.toString().getBytes());
        InputStreamReader reader = new InputStreamReader(in);

        Request request = Http.request(reader);

        assertThat(request.getMethod(), equalTo("GET"));
        assertThat(request.getRequestTarget().getPath(), equalTo("/hello.txt"));
        assertThat(request.getHeader("Accept"), equalTo(Option.of("text/plain; charset=utf-8")));
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

        Request request = Http.request(reader);

        assertThat(request.getMethod(), equalTo("POST"));
        assertThat(request.getRequestTarget().getPath(), equalTo("/form"));
        assertThat(request.getHeader("Content-Length"), equalTo(Option.of("20")));
        assertThat(request.getHeader("Content-Type"), equalTo(Option.of("application/json")));
        assertThat(request.getBody(), equalTo("{ \"hello\": \"world\" }"));
    }

}
