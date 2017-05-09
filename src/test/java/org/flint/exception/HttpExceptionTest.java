package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

import javaslang.collection.HashMap;
import javaslang.control.Option;

import org.flint.response.StatusCode;

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
        he.setHeader("Content-Type", "image/png");
        assertThat(he.getHeader("Content-Type"), equalTo(Option.of("image/png")));
    }

    @Test
    public void itShouldGiveAllHeaders() {
        HttpException he = new HttpException(StatusCode.NOT_FOUND);
        he.setHeader("Content-Type", "image/png");
        assertThat(he.getHeaders(), equalTo(HashMap.of("Content-Type", "image/png")));
    }

}
