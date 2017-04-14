package org.httpserver;

public class StubRequestHandler extends RequestHandler {
    @Override
    protected Response handleGET(Request request) {
        if (request.getRequestTarget().equals("/")) {
            Response response = Response.create(StatusCode.OK);
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            response.setBody("<html><head><title>index</title></head><body>index</body></html>");

            return response;
        } else {
            Response response = Response.create(StatusCode.NOT_FOUND);
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            response.setBody(String.format("<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1>%s - was not found on this server</body></html>", request.getRequestTarget()));

            return response;
        }
    }

    @Override
    protected Response handleHEAD(Request request) {
        return handleGET(request);
    }
}
