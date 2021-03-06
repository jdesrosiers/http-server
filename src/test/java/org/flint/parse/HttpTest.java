package org.flint.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.collection.HashMap;
import javaslang.control.Option;
import javaslang.Tuple;
import javaslang.Tuple2;

import org.flint.request.Method;
import org.flint.request.Request;
import org.flint.request.RequestTarget;

@RunWith(DataProviderRunner.class)
public class HttpTest {

    @Test
    public void itShouldParseARequest() {
        Request request = Http.REQUEST_LINE.parse("GET /hello.txt HTTP/1.1\r\n");

        assertThat(request, instanceOf(Request.class));
        assertThat(request.getMethod(), equalTo(Method.GET));
        assertThat(request.getPath(), equalTo("/hello.txt"));
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
        assertThat(Http.ABSOLUTE_PATH.parse(subject), equalTo(subject));
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
            { "/hello.txt?foo=bar&abc=123", "/hello.txt", "foo=bar&abc=123" },
            { "/hello.txt?foo=abc%20123", "/hello.txt", "foo=abc%20123" }
        };
    }

    @Test
    @UseDataProvider("dataProviderRequestTarget")
    public void itShouldParseRequestTarget(String subject, String path, String query) {
        RequestTarget requestTarget = Http.REQUEST_TARGET.parse(subject);
        assertThat(requestTarget.getPath(), equalTo(path));
        assertThat(requestTarget.getQuery(), equalTo(Option.of(query)));
    }

    @DataProvider
    public static Object[][] dataProviderHeaders() {
        return new Object[][] {
            { "Content-Length: 12", Tuple.of("Content-Length", "12") },
            { "Content-Type: text/plain; charset=utf-8", Tuple.of("Content-Type", "text/plain; charset=utf-8") },
            { "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebkit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36", Tuple.of("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebkit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36") }
        };
    }

    @Test
    @UseDataProvider("dataProviderHeaders")
    public void itShouldParseHeaders(String header, Tuple2<String, String> expected) {
        assertThat(Http.HEADER_FIELD.parse(header), equalTo(expected));
    }

}
