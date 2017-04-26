package org.httpserver.controller;

import static javaslang.API.*;
import static javaslang.Patterns.*;

import org.core.Cookie;
import org.core.FormUrlencoded;
import org.core.Request;
import org.core.Response;

public class CookieController {
    public Response writeCookie(Request request) {
        FormUrlencoded query = getQuery(request);

        Response response = Match(query.get("type")).of(
            Case(Some($()), (type) -> {
                Response r= Response.create();
                Cookie cookie = getCookie(request);
                cookie.put("type", type);
                r.setHeader("Set-Cookie", cookie.toString());
                return r;
            }),
            Case(None(), () -> Response.create())
        );

        response.setBody("Eat");

        return response;
    }

    public Response useCookie(Request request) {
        Response response = Response.create();
        Cookie cookie = getCookie(request);
        response.setBody("mmmm " + cookie.get("type").getOrElse("don't you wish you had a cookie?"));

        return response;
    }

    private FormUrlencoded getQuery(Request request) {
        String query = request.getRequestTarget().getQuery();

        if (query != null) {
            return new FormUrlencoded(query);
        } else {
            return new FormUrlencoded();
        }
    }

    private Cookie getCookie(Request request) {
        return Match(request.getHeader("Cookie")).of(
            Case(Some($()), (content) -> new Cookie(content)),
            Case(None(), () -> new Cookie())
        );
    }
}
