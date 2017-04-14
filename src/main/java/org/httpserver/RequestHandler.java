package org.httpserver;

public abstract class RequestHandler {
    public Response handle(Request request) {
        if (request.getMethod() == "GET") {
            return handleGET(request);
        } else if (request.getMethod() == "HEAD") {
            return handleHEAD(request);
        } else if (request.getMethod() == "POST") {
            return handlePOST(request);
        } else if (request.getMethod() == "PUT") {
            return handlePUT(request);
        } else if (request.getMethod() == "DELETE") {
            return handleDELETE(request);
        } else if (request.getMethod() == "OPTIONS") {
            return handleOPTIONS(request);
        } else {
            return Response.create(StatusCode.METHOD_NOT_ALLOWED);
        }
    }

    protected Response handleGET(Request request) {
        return Response.create(StatusCode.METHOD_NOT_ALLOWED);
    }

    protected Response handleHEAD(Request request) {
        return Response.create(StatusCode.METHOD_NOT_ALLOWED);
    }

    protected Response handlePOST(Request request) {
        return Response.create(StatusCode.METHOD_NOT_ALLOWED);
    }

    protected Response handlePUT(Request request) {
        return Response.create(StatusCode.METHOD_NOT_ALLOWED);
    }

    protected Response handleDELETE(Request request) {
        return Response.create(StatusCode.METHOD_NOT_ALLOWED);
    }

    protected Response handleOPTIONS(Request request) {
        return Response.create(StatusCode.METHOD_NOT_ALLOWED);
    }
}
