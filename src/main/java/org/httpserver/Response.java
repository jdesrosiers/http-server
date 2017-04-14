package org.httpserver;

import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Option;

import java.io.PrintWriter;
import java.io.OutputStream;

public class Response {
    private int statusCode;
    private Map headers = HashMap.of();
    private String body;

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
        return body;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeader(String header, String value) {
        this.headers = headers.put(header, value);
    }

    public void setBody(String body) {
        this.body = body;
        setHeader("Content-Length", Integer.toString(body.getBytes().length));
    }

    public void writeHttpMessage(OutputStream os) {
        PrintWriter out = new PrintWriter(os, true);

        out.println(String.format("HTTP/1.1 %s %s\r", statusCode, StatusCode.getMessage(statusCode).get()));
        headers.forEach((header, value) -> out.println(String.format("%s: %s\r", header, value)));
        out.println("\r");

        if (body.length() > 0) {
            out.println(String.format("%s\r", body));
        }

        out.close();
    }
}
