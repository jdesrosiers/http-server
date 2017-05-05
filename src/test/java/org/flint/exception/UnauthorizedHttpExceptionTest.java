package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

import javaslang.control.Option;

import org.flint.response.StatusCode;

public class UnauthorizedHttpExceptionTest {

    @Test
    public void itShouldBeAnHttpException() {
        UnauthorizedHttpException he = new UnauthorizedHttpException("Basic realm-\"foo\"");
        assertThat(he, instanceOf(HttpException.class));
    }

    @Test
    public void itShouldHaveAStatusCode() {
        UnauthorizedHttpException he = new UnauthorizedHttpException("Basic realm-\"foo\"");
        assertThat(he.getStatusCode(), equalTo(StatusCode.UNAUTHORIZED));
    }

    @Test
    public void itShouldSetAWwwAuthenticateHeader() {
        UnauthorizedHttpException he = new UnauthorizedHttpException("Basic realm=\"foo\"");
        assertThat(he.getHeader("WWW-Authenticate"), equalTo(Option.of("Basic realm=\"foo\"")));
    }
}
