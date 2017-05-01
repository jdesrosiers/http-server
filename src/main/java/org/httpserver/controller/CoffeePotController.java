package org.httpserver.controller;

import org.flint.Response;
import org.flint.Request;
import org.flint.StatusCode;

public class CoffeePotController {
    public Response coffee(Request request) {
        Response response = Response.create(StatusCode.IM_A_TEAPOT);
        response.setBody("I'm a teapot");

        return response;
    }

    public Response tea(Request request) {
        return Response.create(StatusCode.OK);
    }
}
