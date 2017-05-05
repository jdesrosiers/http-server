package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

import org.flint.StatusCode;

public class UnsupportedMediaTypeHttpExceptionTest {

    @Test
    public void itShouldBeAnHttpException() {
        UnsupportedMediaTypeHttpException he = new UnsupportedMediaTypeHttpException();
        assertThat(he, instanceOf(HttpException.class));
    }

    @Test
    public void itShouldHaveAStatusCode() {
        UnsupportedMediaTypeHttpException he = new UnsupportedMediaTypeHttpException();
        assertThat(he.getStatusCode(), equalTo(StatusCode.UNSUPPORTED_MEDIA_TYPE));
    }
}
