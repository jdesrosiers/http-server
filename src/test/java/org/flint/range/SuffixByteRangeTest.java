package org.flint.range;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import javaslang.control.Option;

import org.flint.Response;
import org.flint.StatusCode;

public class SuffixByteRangeTest {

    private Response response;

    @Before
    public void setup() {
        response = Response.create(StatusCode.OK);
        response.setHeader("Content-Type", "text/plain; charset=utf-8");
        response.setBody("abcdefghijklmnopqrstuvwxyz");
    }

    @Test
    public void itShouldHandleARangeInSuffixForm() throws IOException {
        SuffixByteRange range = new SuffixByteRange(3);

        Response result = range.makePartial(response);

        assertThat(result.getStatusCode(), equalTo(StatusCode.PARTIAL_CONTENT));
        assertThat(result.getHeader("Content-Range"), equalTo(Option.of("bytes 23-25/26")));
        assertThat(result.getContentLength(), equalTo(3));
        assertThat(result.getHeader("Content-Type"), equalTo(Option.of("text/plain; charset=utf-8")));
        assertThat(result.getBodyAsString(), equalTo("xyz"));
    }

    @Test
    public void itShouldHandleASuffixRangePastTheEndOfTheDocument() throws IOException {
        SuffixByteRange range = new SuffixByteRange(30);

        Response result = range.makePartial(response);

        assertThat(result.getStatusCode(), equalTo(StatusCode.PARTIAL_CONTENT));
        assertThat(result.getHeader("Content-Range"), equalTo(Option.of("bytes 0-25/26")));
        assertThat(result.getContentLength(), equalTo(26));
        assertThat(result.getHeader("Content-Type"), equalTo(Option.of("text/plain; charset=utf-8")));
        assertThat(result.getBodyAsString(), equalTo("abcdefghijklmnopqrstuvwxyz"));
    }

}
