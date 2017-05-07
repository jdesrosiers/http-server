package org.cobspec.controller;

import org.flint.Request;
import org.flint.Response;
import org.flint.StatusCode;
import org.flint.FormUrlencoded;

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
