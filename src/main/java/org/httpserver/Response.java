package org.httpserver;

import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Option;

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

    public String toHttpMessage() {
        StringBuilder message = new StringBuilder();
        message.append(String.format("HTTP/1.1 %s %s\r\n", statusCode, StatusCode.getMessage(statusCode).get()));

        headers.forEach((header, value) -> message.append(String.format("%s: %s\r\n", header, value)));

        message.append("\r\n");

        if (body.length() > 0) {
            message.append(String.format("%s\r\n", body));
        }

        return message.toString();
    }
}
