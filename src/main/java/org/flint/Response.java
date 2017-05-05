package org.flint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Option;
import javaslang.control.Try;

import org.util.FileSystem;

public class Response {
    private int statusCode;
    private Map headers = HashMap.of();
    private InputStream body;

    public static Response create() {
        return Response.create(200, HashMap.of(), "");
    }

    public static Response create(int statusCode) {
        return Response.create(statusCode, HashMap.of(), "");
    }

    public static Response create(int statusCode, Map headers) {
        return Response.create(statusCode, headers, "");
    }

    public static Response create(int statusCode, Map headers, String body) {
        Response response = new Response();
        response.statusCode = statusCode;
        response.headers = headers;
        response.setBody(body);

        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map getHeaders() {
        return headers;
    }

    public Option<String> getHeader(String header) {
        return headers.get(header);
    }

    public String getBodyAsString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        return Try.of(() -> {
            FileSystem.copyStreams(body, out);
            return out.toString();
        }).getOrElse("");
    }

    public InputStream getBody() {
        return body;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeader(String header, String value) {
        this.headers = headers.put(header, value);
    }

    public int getContentLength() {
        return getHeader("Content-Length")
            .map(Integer::parseInt)
            .get();
    }

    public void setBody(String body) {
        setBody(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
    }

    public void removeBody() {
        this.body = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
    }

    public void setBody(InputStream body) {
        this.body = body;
        int length = Try.of(() -> body.available()).getOrElse(0);
        setHeader("Content-Length", Integer.toString(length));
    }
}
