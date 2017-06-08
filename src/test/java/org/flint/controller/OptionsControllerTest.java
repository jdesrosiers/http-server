package org.flint.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;

import javaslang.control.Option;

import org.flint.Application;
import org.flint.request.OriginForm;
import org.flint.request.Request;
import org.flint.request.Method;
import org.flint.response.Response;
import org.flint.response.StatusCode;

public class OptionsControllerTest {

    @Test
    public void itShouldSetTheAllowHeaderBasedOnExistingRoutes() {
        Application app = new Application();

        app.get("/foo", request -> Response.create());
        app.post("/foo", request -> Response.create());

        OptionsController optionsController = new OptionsController(app.getRouteMatcher());
        app.options("*", optionsController::options);

        Request request = new Request(Method.OPTIONS, new OriginForm("/foo"));
        Response response = app.requestHandler(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NO_CONTENT));
        assertThat(response.getHeader("Allow"), equalTo(Option.of("GET,HEAD,POST,OPTIONS")));
    }

}
