package org.flint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.collection.HashMap;
import javaslang.control.Option;
import javaslang.Tuple;

@RunWith(DataProviderRunner.class)
public class RequestTest {

    @DataProvider
    public static Object[][] dataProviderMethods() {
        return new Object[][] {
            { "GET" },
            { "POST" }
        };
    }

    @Test
    @UseDataProvider("dataProviderMethods")
    public void itShouldAllowGettingTheMethod(String method) {
        Request request = new Request(method, new OriginForm("/hello.txt"));
        request.setBody("foo");
        assertThat(request.getMethod(), equalTo(method));
    }

    @DataProvider
    public static Object[][] dataProviderRequestTargets() {
        return new Object[][] {
            { new OriginForm("/") },
            { new OriginForm("/hello.txt") },
            { new OriginForm("/hello.txt", "foo=bar") }
        };
    }

    @Test
    @UseDataProvider("dataProviderRequestTargets")
    public void itShouldAllowGettingTheRequestTarget(RequestTarget requestTarget) {
        Request request = new Request("GET", requestTarget);
        request.setBody("foo");
        assertThat(request.getRequestTarget(), equalTo(requestTarget));
    }

    @DataProvider
    public static Object[][] dataProviderHeaders() {
        return new Object[][] {
            { HashMap.empty(), Option.none() },
            { HashMap.ofEntries(Tuple.of("Content-Type", "image/png")), Option.none() },
            { HashMap.ofEntries(Tuple.of("Content-Length", "3")), Option.of("3") },
            { HashMap.ofEntries(Tuple.of("Content-Length", "3"), Tuple.of("Content-Type", "image/png")), Option.of("3") },
            { HashMap.ofEntries(Tuple.of("content-length", "3")), Option.of("3") },
        };
    }

    @Test
    @UseDataProvider("dataProviderHeaders")
    public void itShouldAllowGettingTheMethod(HashMap<String, String>  headers, Option expected) {
        Request request = new Request("GET", new OriginForm("/hello.txt"));
        headers.forEach(request::setHeader);
        request.setBody("foo");
        assertThat(request.getHeader("Content-Length"), equalTo(expected));
    }

    @Test
    public void itShouldAllowSettingHeaders() {
        Request request = new Request("GET", new OriginForm("/hello.txt"));
        request.setHeader("Content-Length", "3");
        request.setBody("foo");
        assertThat(request.getHeader("Content-Length"), equalTo(Option.of("3")));
    }

    @Test
    public void matchingHeadersShouldBeCaseInsensitive() {
        Request request = new Request("GET", new OriginForm("/hello.txt"));
        request.setHeader("Content-Length", "3");
        request.setBody("foo");
        assertThat(request.getHeader("content-length"), equalTo(Option.of("3")));
    }

    @Test
    public void itShouldAllowGettingTheBody() {
        Request request = new Request("GET", new OriginForm("/hello.txt"));
        request.setBody("foo");
        assertThat(request.getBody(), equalTo("foo"));
    }

    @Test
    public void itShouldAllowSettingTheBody() {
        Request request = new Request("GET", new OriginForm("/hello.txt"));
        request.setBody("bar");
        assertThat(request.getBody(), equalTo("bar"));
    }

}
