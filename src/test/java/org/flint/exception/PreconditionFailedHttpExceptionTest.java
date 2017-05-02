package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

import org.flint.StatusCode;

public class PreconditionFailedHttpExceptionTest {

    @Test
    public void itShouldBeAnHttpException() {
        PreconditionFailedHttpException he = new PreconditionFailedHttpException();
        assertThat(he, instanceOf(HttpException.class));
    }

    @Test
    public void itShouldHaveAStatusCode() {
        PreconditionFailedHttpException he = new PreconditionFailedHttpException();
        assertThat(he.getStatusCode(), equalTo(StatusCode.PRECONDITION_FAILED));
    }
}
