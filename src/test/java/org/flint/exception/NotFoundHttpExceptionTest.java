package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

import org.flint.StatusCode;

public class NotFoundHttpExceptionTest {

    @Test
    public void itShouldBeAnHttpException() {
        NotFoundHttpException he = new NotFoundHttpException();
        assertThat(he, instanceOf(HttpException.class));
    }

    @Test
    public void itShouldHaveAStatusCode() {
        NotFoundHttpException he = new NotFoundHttpException();
        assertThat(he.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
    }
}
