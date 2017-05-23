package org.cobspec.controller;

import javaslang.collection.List;

import org.flint.exception.InternalServerErrorHttpException;
import org.flint.exception.MethodNotAllowedHttpException;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;
import org.flint.routing.RouteMatcher;

public class OptionsController {
    private RouteMatcher routeMatcher;

    public OptionsController(RouteMatcher routeMatcher) {
        this.routeMatcher = routeMatcher;
    }

    public Response options(Request request) {
        Request dummyRequest = new Request("NOTAMETHOD", request.getRequestTarget());

        try {
            routeMatcher.getMatchFor(dummyRequest);
        } catch (MethodNotAllowedHttpException he) {
            Response response = Response.create(StatusCode.NO_CONTENT);
            he.getHeader("Allow").peek(allow -> response.setHeader("Allow", allow));

            return response;
        }

        // Should never get here
        throw new InternalServerErrorHttpException();
    }
}
