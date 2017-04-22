package org.httpserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        Request request = new Request(method, "/hello.txt", HashMap.empty(), "foo");
        assertThat(request.getMethod(), equalTo(method));
    }

    @DataProvider
    public static Object[][] dataProviderRequestTargets() {
        return new Object[][] {
            { "/" },
            { "/hello.txt" }
        };
    }

    @Test
    @UseDataProvider("dataProviderRequestTargets")
    public void itShouldAllowGettingTheRequestTarget(String requestTarget) {
        Request request = new Request("GET", requestTarget, HashMap.empty(), "foo");
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
        Request request = new Request("GET", "/hello.txt", headers, "foo");
        assertThat(request.getHeader("Content-Length"), equalTo(expected));
    }

    @Test
    public void itShouldAllowSettingHeaders() {
        Request request = new Request("GET", "/hello.txt", HashMap.empty(), "foo");
        request.setHeader("Content-Length", "3");
        assertThat(request.getHeader("Content-Length"), equalTo(Option.of("3")));
    }

    @Test
    public void matchingHeadersShouldBeCaseInsensitive() {
        Request request = new Request("GET", "/hello.txt", HashMap.empty(), "foo");
        request.setHeader("Content-Length", "3");
        assertThat(request.getHeader("content-length"), equalTo(Option.of("3")));
    }

    @Test
    public void isShouldAllowGettingTheBody() {
        Request request = new Request("GET", "/hello.txt", HashMap.empty(), "foo");
        assertThat(request.getBody(), equalTo("foo"));
    }

    @Test
    public void isShouldAllowSettingTheBody() {
        Request request = new Request("GET", "/hello.txt", HashMap.empty(), "foo");
        request.setBody("bar");
        assertThat(request.getBody(), equalTo("bar"));
    }

}
