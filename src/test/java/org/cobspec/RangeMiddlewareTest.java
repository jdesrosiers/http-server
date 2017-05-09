package org.cobspec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;

import org.flint.request.OriginForm;
import org.flint.request.Method;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

@RunWith(DataProviderRunner.class)
public class RangeMiddlewareTest {

    private Response response;

    @Before
    public void setup() {
        response = Response.create(StatusCode.OK);
        response.setHeader("Content-Type", "text/plain; charset=utf-8");
        response.setBody("abcdefghijklmnopqrstuvwxyz");
    }

    @Test
    public void itShouldDoNothingIfThereIsNoRangeHeader() {
        RangeMiddleware range = new RangeMiddleware();

        Request request = new Request(Method.GET, new OriginForm("foo"));
        Response result = range.handleRange(request, response);

        assertThat(result.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(result.getHeader("Content-Range"), equalTo(Option.none()));
        assertThat(result.getContentLength(), equalTo(26));
        assertThat(result.getHeader("Content-Type"), equalTo(Option.of("text/plain; charset=utf-8")));
        assertThat(result.getBodyAsString(), equalTo("abcdefghijklmnopqrstuvwxyz"));
    }

    @DataProvider
    public static Object[][] dataProviderMethods() {
        return new Object[][] {
            { Method.HEAD },
            { Method.POST },
            { Method.PUT },
            { Method.DELETE },
            { Method.OPTIONS }
        };
    }

    @Test
    @UseDataProvider("dataProviderMethods")
    public void itShouldIgnoreTheRangeHeaderIfTheMethodIsNotGET(String method) {
        RangeMiddleware range = new RangeMiddleware();

        Request request = new Request(method, new OriginForm("foo"));
        request.setHeader("Range", "bytes=0-4");
        Response result = range.handleRange(request, response);

        assertThat(result.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(result.getHeader("Content-Range"), equalTo(Option.none()));
        assertThat(result.getContentLength(), equalTo(26));
        assertThat(result.getHeader("Content-Type"), equalTo(Option.of("text/plain; charset=utf-8")));
        assertThat(result.getBodyAsString(), equalTo("abcdefghijklmnopqrstuvwxyz"));
    }

    @DataProvider
    public static Object[][] dataProviderStatusCodes() {
        return new Object[][] {
            { Response.create(StatusCode.NO_CONTENT), StatusCode.NO_CONTENT },
            { Response.create(StatusCode.NOT_MODIFIED), StatusCode.NOT_MODIFIED },
            { Response.create(StatusCode.NOT_FOUND), StatusCode.NOT_FOUND },
            { Response.create(StatusCode.INTERNAL_SERVER_ERROR), StatusCode.INTERNAL_SERVER_ERROR }
        };
    }

    @Test
    @UseDataProvider("dataProviderStatusCodes")
    public void itShouldIgnoreTheRangeHeaderIfTheStatusIsNotOK(Response response, int statusCode) {
        RangeMiddleware range = new RangeMiddleware();

        Request request = new Request(Method.GET, new OriginForm("foo"));
        request.setHeader("Range", "bytes=0-4");
        Response result = range.handleRange(request, response);

        assertThat(result.getStatusCode(), equalTo(statusCode));
        assertThat(result.getHeader("Content-Range"), equalTo(Option.none()));
        assertThat(result.getBodyAsString(), equalTo(""));
    }

    @Test
    public void itShouldIgnoreARangeHeaderThatItDoesntUnderstand() {
        RangeMiddleware range = new RangeMiddleware();

        Request request = new Request(Method.GET, new OriginForm("foo"));
        request.setHeader("Range", "foo=0-4");
        Response result = range.handleRange(request, response);

        assertThat(result.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(result.getHeader("Content-Range"), equalTo(Option.none()));
        assertThat(result.getContentLength(), equalTo(26));
        assertThat(result.getHeader("Content-Type"), equalTo(Option.of("text/plain; charset=utf-8")));
        assertThat(result.getBodyAsString(), equalTo("abcdefghijklmnopqrstuvwxyz"));
    }

    @Test
    public void itShouldIgnoreAnInvalidRange() {
        RangeMiddleware range = new RangeMiddleware();

        Request request = new Request(Method.GET, new OriginForm("foo"));
        request.setHeader("Range", "bytes=30-35");
        Response result = range.handleRange(request, response);

        assertThat(result.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(result.getHeader("Content-Range"), equalTo(Option.none()));
        assertThat(result.getContentLength(), equalTo(26));
        assertThat(result.getHeader("Content-Type"), equalTo(Option.of("text/plain; charset=utf-8")));
        assertThat(result.getBodyAsString(), equalTo("abcdefghijklmnopqrstuvwxyz"));
    }

    @Test
    public void itShouldReturnAPartialContentResponseIfEverythingIsSatisfiable() {
        RangeMiddleware range = new RangeMiddleware();

        Request request = new Request(Method.GET, new OriginForm("foo"));
        request.setHeader("Range", "bytes=0-4");
        Response result = range.handleRange(request, response);

        assertThat(result.getStatusCode(), equalTo(StatusCode.PARTIAL_CONTENT));
        assertThat(result.getHeader("Content-Range"), equalTo(Option.of("bytes 0-4/26")));
        assertThat(result.getContentLength(), equalTo(5));
        assertThat(result.getHeader("Content-Type"), equalTo(Option.of("text/plain; charset=utf-8")));
        assertThat(result.getBodyAsString(), equalTo("abcde"));
    }

}
