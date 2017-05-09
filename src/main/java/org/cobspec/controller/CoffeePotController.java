package org.cobspec.controller;

import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

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
