package org.httpserver.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.jparsec.Scanners;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.pattern.Pattern;
import org.jparsec.pattern.Patterns;

import javaslang.collection.HashMap;
import javaslang.Tuple;
import javaslang.Tuple2;

import org.httpserver.Request;

@RunWith(DataProviderRunner.class)
public class HttpParserTest {

    @Test
    public void itShouldParseARequest() {
        Request request = Http.requestLine.parse("GET /hello.txt HTTP/1.1\r\n");

        assertThat(request, instanceOf(Request.class));
        assertThat(request.getMethod(), equalTo("GET"));
        assertThat(request.getRequestTarget(), equalTo("/hello.txt"));
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
            { "/" },
            { "/hello" },
            { "/hello.txt" },
            { "/hello.txt?" },
            { "/hello.txt?foo" },
            { "/hello.txt?foo=bar" }
        };
    }

    @Test
    @UseDataProvider("dataProviderRequestTarget")
    public void itShouldParseRequestTarget(String subject) {
        assertThat(Http.requestTarget.parse(subject), equalTo(subject));
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

}
