package org.httpserver.controller;

import org.core.Response;
import org.core.Request;
import org.core.StatusCode;

public class RedirectController {
    public Response redirect(Request request, String location) {
        Response response = Response.create(StatusCode.FOUND);
        response.setHeader("Location", location);

        return response;
    }

    public Response temporaryRedirect(Request request, String location) {
        Response response = Response.create(StatusCode.TEMPORARY_REDIRECT);
        response.setHeader("Location", location);

        return response;
    }

    public Response permanentRedirect(Request request, String location) {
        Response response = Response.create(StatusCode.PERMANENT_REDIRECT);
        response.setHeader("Location", location);

        return response;
    }

    public Response movedPermanently(Request request, String location) {
        Response response = Response.create(StatusCode.MOVED_PERMANENTLY);
        response.setHeader("Location", location);

        return response;
    }
}
