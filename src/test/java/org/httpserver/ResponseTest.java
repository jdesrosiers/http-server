package org.httpserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.Tuple;
import javaslang.control.Option;

import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;

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

        assertThat(response.getBody(), equalTo(""));
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

        assertThat(response.getBody(), equalTo(body));
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

        assertThat(response.getBody(), equalTo("foo"));
    }

    @Test
    public void settingTheBodyShouldSetTheAppropriateContentLength() {
        Response response = Response.create();
        response.setBody("foo");

        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("3")));
    }

    @Test
    public void itShouldGenerateAnHttpMessageRepresentationFor200OK() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Response response = Response.create();
        response.setHeader("Content-Type", "text/plain; charset=utf-8");
        response.setBody("foo");

        StringBuilder expected = new StringBuilder();
        expected.append("HTTP/1.1 200 OK\r\n");
        expected.append("Content-Type: text/plain; charset=utf-8\r\n");
        expected.append("Content-Length: 3\r\n");
        expected.append("\r\n");
        expected.append("foo\r\n");

        response.writeHttpMessage(os);

        assertThat(os.toString(), equalTo(expected.toString()));
    }

    @Test
    public void itShouldGenerateAnHttpMessageRepresentationFor404NotFound() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Response response = Response.create(StatusCode.NOT_FOUND);

        StringBuilder expected = new StringBuilder();
        expected.append("HTTP/1.1 404 Not Found\r\n");
        expected.append("Content-Length: 0\r\n");
        expected.append("\r\n");

        response.writeHttpMessage(os);

        assertThat(os.toString(), equalTo(expected.toString()));
    }

    @Test
    public void itShouldSetTheContentLengthHeaderToTheCorrectValue() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Response response = Response.create();
        response.setBody("foo");

        StringBuilder expected = new StringBuilder();
        expected.append("HTTP/1.1 200 OK\r\n");
        expected.append("Content-Length: 3\r\n");
        expected.append("\r\n");
        expected.append("foo\r\n");

        response.writeHttpMessage(os);

        assertThat(os.toString(), equalTo(expected.toString()));
    }

}
