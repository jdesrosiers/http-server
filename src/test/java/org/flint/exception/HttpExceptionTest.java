package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

import javaslang.collection.HashMap;
import javaslang.control.Option;

import org.flint.StatusCode;

public class HttpExceptionTest {

    @Test
    public void itShouldBeAnUncheckedException() {
        HttpException he = new HttpException(StatusCode.NOT_FOUND);
        assertThat(he, instanceOf(RuntimeException.class));
    }

    @Test
    public void itShouldHaveAStatusCode() {
        HttpException he = new HttpException(StatusCode.NOT_FOUND);
        assertThat(he.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
    }

    @Test
    public void itShouldAcceptHeaders() {
        HttpException he = new HttpException(StatusCode.NOT_FOUND);
        he.setHeader("Content-Length", "3");
        assertThat(he.getHeader("Content-Length"), equalTo(Option.of("3")));
    }

    @Test
    public void itShouldGiveAllHeaders() {
        HttpException he = new HttpException(StatusCode.NOT_FOUND);
        he.setHeader("Content-Length", "3");
        assertThat(he.getHeaders(), equalTo(HashMap.of("Content-Length", "3")));
    }

}
