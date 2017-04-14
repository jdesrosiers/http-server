package org.httpserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;

@RunWith(DataProviderRunner.class)
public class RequestHandlerTest {

    @Test
    public void itShouldHandleAGETRequestForIndex() {
        RequestHandler handler = new StubRequestHandler();

        Request request = new Request("GET", "/");
        Response response = handler.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("text/html; charset=utf-8")));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("64")));
        assertThat(response.getBody().length(), greaterThan(0));
    }

    @Test
    public void itShouldRespondWith404NotFoundIfTheResouceDoesntExist() {
        RequestHandler handler = new StubRequestHandler();

        Request request = new Request("GET", "/foobar");
        Response response = handler.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("127")));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("text/html; charset=utf-8")));
        assertThat(response.getBody(), containsString("404 Not Found"));
    }

    @Test
    public void itShouldHandleAHEADRequestForIndex() {
        RequestHandler handler = new StubRequestHandler();

        Request request = new Request("HEAD", "/");
        Response response = handler.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Length"), equalTo(Option.of("64")));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("text/html; charset=utf-8")));
        assertThat(response.getBody(), containsString("index"));
    }

}
