package org.httpserver;

import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Option;

import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import javaslang.control.Try;

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

    public String getBody() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        return Try.of(() -> {
            copyStreams(body, out);
            return out.toString();
        }).getOrElse("");
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeader(String header, String value) {
        this.headers = headers.put(header, value);
    }

    public void setBody(String body) {
        setBody(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
    }

    public void setBody(InputStream body) {
        this.body = body;
        int length = Try.of(() -> body.available()).getOrElse(0);
        setHeader("Content-Length", Integer.toString(length));
    }

    public void writeHttpMessage(OutputStream os) throws IOException {
        PrintWriter out = new PrintWriter(os, true);
        out.println(String.format("HTTP/1.1 %s %s\r", statusCode, StatusCode.getMessage(statusCode).getOrElse("")));
        headers.forEach((header, value) -> out.println(String.format("%s: %s\r", header, value)));
        out.println("\r");

        if (body.available() > 0) {
            copyStreams(body, os);
            out.println("\r");
        }
    }

    private void copyStreams(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int size = in.read(buffer);
        while (size != -1) {
            out.write(buffer, 0, size);
            size = in.read(buffer);
        }
    }
}
