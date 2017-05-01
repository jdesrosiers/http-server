package org.flint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.collection.Map;
import javaslang.collection.HashMap;
import javaslang.control.Option;
import javaslang.Tuple;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

@RunWith(DataProviderRunner.class)
public class ResponseTest {

    @Test
    public void itShouldDefaultTo200OK() {
        Response response = Response.create();

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
    }

    @Test
    public void itShouldDefaultToNoBody() {
        Response response = Response.create();

        assertThat(response.getBodyAsString(), equalTo(""));
    }

    @Test
    public void itShouldTakeAStatusCodeInTheConstructor() {
        Response response = Response.create(StatusCode.NOT_FOUND);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
    }

    @Test
    public void itShouldTakeHeadersInTheConstructor() {
        Map headers = HashMap.ofEntries(Tuple.of("Content-Type", "text/html; charset=utf-8"));
        Response response = Response.create(StatusCode.NOT_FOUND, headers);

        Map expected = headers.put("Content-Length", "0");
        assertThat(response.getHeaders(), equalTo(expected));
    }

    @Test
    public void itShouldTakeTheBodyInTheConstructor() {
        Map headers = HashMap.of(Tuple.of("Content-Length", "3"));
        String body = "foo";
        Response response = Response.create(StatusCode.NOT_FOUND, headers, body);

        assertThat(response.getBodyAsString(), equalTo(body));
    }

    @Test
    public void itShouldRetrieveASingleHeader() {
        Response response = Response.create(StatusCode.OK, HashMap.of(Tuple.of("Content-Type", "text/html; charset=utf-8")));

        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("text/html; charset=utf-8")));
    }

    @Test
    public void itShouldHaveAStatusCodeSetter() {
        Response response = Response.create();
        response.setStatusCode(StatusCode.NOT_FOUND);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
    }

    @Test
    public void itShouldHaveASingleHeaderSetter() {
        Response response = Response.create();
        response.setHeader("Content-Type", "text/html; charset=utf-8");

        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("text/html; charset=utf-8")));
    }

    @Test
    public void itShouldHaveABodySetter() {
        Response response = Response.create();
        response.setBody("foo");

        assertThat(response.getBodyAsString(), equalTo("foo"));
    }

    @Test
    public void settingTheBodyShouldSetTheAppropriateContentLength() {
        Response response = Response.create();
        response.setBody("foo");

        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
    }

}
