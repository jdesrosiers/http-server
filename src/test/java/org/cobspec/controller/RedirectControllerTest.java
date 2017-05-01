package org.cobspec.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;

import org.flint.OriginForm;
import org.flint.Response;
import org.flint.Request;
import org.flint.StatusCode;

@RunWith(DataProviderRunner.class)
public class RedirectControllerTest {

    @Test
    public void itShouldRedirect() {
        RedirectController controller = new RedirectController();
        Request request = new Request("GET", new OriginForm("/foo"));
        Response response = controller.redirect(request, "/foo");

        assertThat(response.getStatusCode(), equalTo(StatusCode.FOUND));
        assertThat(response.getHeader("Location"), equalTo(Option.of("/foo")));
    }

    @Test
    public void itShouldTemporaryRedirect() {
        RedirectController controller = new RedirectController();
        Request request = new Request("GET", new OriginForm("/foo"));
        Response response = controller.temporaryRedirect(request, "/foo");

        assertThat(response.getStatusCode(), equalTo(StatusCode.TEMPORARY_REDIRECT));
        assertThat(response.getHeader("Location"), equalTo(Option.of("/foo")));
    }

    @Test
    public void itShouldPermanentRedirect() {
        RedirectController controller = new RedirectController();
        Request request = new Request("GET", new OriginForm("/foo"));
        Response response = controller.permanentRedirect(request, "/foo");

        assertThat(response.getStatusCode(), equalTo(StatusCode.PERMANENT_REDIRECT));
        assertThat(response.getHeader("Location"), equalTo(Option.of("/foo")));
    }

    @Test
    public void itShouldMovePermanently() {
        RedirectController controller = new RedirectController();
        Request request = new Request("GET", new OriginForm("/foo"));
        Response response = controller.movedPermanently(request, "/foo");

        assertThat(response.getStatusCode(), equalTo(StatusCode.MOVED_PERMANENTLY));
        assertThat(response.getHeader("Location"), equalTo(Option.of("/foo")));
    }

}
