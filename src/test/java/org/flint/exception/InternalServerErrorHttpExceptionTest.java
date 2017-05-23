package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

import org.flint.response.StatusCode;

public class InternalServerErrorHttpExceptionTest {

    @Test
    public void itShouldBeAnHttpException() {
        InternalServerErrorHttpException he = new InternalServerErrorHttpException();
        assertThat(he, instanceOf(HttpException.class));
    }

    @Test
    public void itShouldHaveAStatusCode() {
        InternalServerErrorHttpException he = new InternalServerErrorHttpException();
        assertThat(he.getStatusCode(), equalTo(StatusCode.INTERNAL_SERVER_ERROR));
    }
}
