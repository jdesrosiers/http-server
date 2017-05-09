package org.cobspec.controller;

import org.flint.cookie.Cookie;
import org.flint.formurlencoded.FormUrlencoded;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

public class CookieController {
    public Response writeCookie(Request request) {
        FormUrlencoded query = request.getQuery().getOrElse(FormUrlencoded::new);

        Response response = Response.create(StatusCode.OK);
        query.get("type")
            .peek((type) -> {
                Cookie cookie = getCookie(request);
                cookie.put("type", type);
                response.setHeader("Set-Cookie", cookie.toString());
            });

        response.setBody("Eat");

        return response;
    }

    public Response useCookie(Request request) {
        Response response = Response.create();
        Cookie cookie = getCookie(request);
        response.setBody("mmmm " + cookie.get("type").getOrElse("don't you wish you had a cookie?"));

        return response;
    }

    private Cookie getCookie(Request request) {
        return request.getHeader("Cookie").map(Cookie::new).getOrElse(Cookie::new);
    }
}
