package org.cobspec.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

import javaslang.control.Option;
import javaslang.collection.HashMap;

import org.flint.OriginForm;
import org.flint.Response;
import org.flint.Request;
import org.flint.StatusCode;

public class CookieControllerTest {

    @Test
    public void itShouldSetACookie() {
        CookieController controller = new CookieController();
        Request request = new Request("GET", new OriginForm("/cookie", "type=chocolate"));
        Response response = controller.writeCookie(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Set-Cookie"), equalTo(Option.of("type=chocolate")));
        assertThat(response.getBodyAsString(), containsString("Eat"));
    }

    @Test
    public void itShouldNotSetACookieIfThereIsNoTypeQueryParam() {
        CookieController controller = new CookieController();
        Request request = new Request("GET", new OriginForm("/cookie"));
        Response response = controller.writeCookie(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Set-Cookie"), equalTo(Option.none()));
        assertThat(response.getBodyAsString(), containsString("Eat"));
    }

    @Test
    public void itShouldUseACookie() {
        CookieController controller = new CookieController();
        Request request = new Request("GET", new OriginForm("/eat_cookie"));
        request.setHeader("Cookie", "type=chocolate");
        Response response = controller.useCookie(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getBodyAsString(), containsString("mmmm chocolate"));
    }

    @Test
    public void itShouldUseDefaultIfThereIsNoTypeCookie() {
        CookieController controller = new CookieController();
        Request request = new Request("GET", new OriginForm("/eat_cookie"));
        request.setHeader("Cookie", "foo=bar");
        Response response = controller.useCookie(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getBodyAsString(), equalTo("mmmm don't you wish you had a cookie?"));
    }

    @Test
    public void itShouldUseDefaultIfThereIsNoCookieAtAll() {
        CookieController controller = new CookieController();
        Request request = new Request("GET", new OriginForm("/eat_cookie"));
        Response response = controller.useCookie(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getBodyAsString(), equalTo("mmmm don't you wish you had a cookie?"));
    }

}
