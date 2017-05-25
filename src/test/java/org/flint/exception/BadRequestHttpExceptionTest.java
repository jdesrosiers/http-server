package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

import org.flint.response.StatusCode;

public class BadRequestHttpExceptionTest {

    @Test
    public void itShouldBeAnHttpException() {
        BadRequestHttpException he = new BadRequestHttpException();
        assertThat(he, instanceOf(HttpException.class));
    }

    @Test
    public void itShouldHaveAStatusCode() {
        BadRequestHttpException he = new BadRequestHttpException();
        assertThat(he.getStatusCode(), equalTo(StatusCode.BAD_REQUEST));
    }
}
