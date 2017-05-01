package org.httpserver.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;
import javaslang.collection.HashMap;

import org.core.OriginForm;
import org.core.Response;
import org.core.Request;
import org.core.StatusCode;

@RunWith(DataProviderRunner.class)
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
