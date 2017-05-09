package org.cobspec.controller;

import org.flint.formurlencoded.FormUrlencoded;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

public class ParameterDecodeController {
    public Response run(Request request) {
        String body = request.getQuery()
            .getOrElse(new FormUrlencoded())
            .toList()
            .map(entry -> entry._1 + " = " + entry._2)
            .intersperse("\n")
            .fold("", String::concat);

        Response response = Response.create(StatusCode.OK);
        response.setHeader("Content-Type", "text/plain; charset=utf-8");
        response.setBody(body);

        return response;
    }
}
