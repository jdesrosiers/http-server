package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

import javaslang.control.Option;

import org.flint.StatusCode;

public class MethodNotAllowedHttpExceptionTest {

    @Test
    public void itShouldBeAnHttpException() {
        MethodNotAllowedHttpException he = new MethodNotAllowedHttpException("GET");
        assertThat(he, instanceOf(HttpException.class));
    }

    @Test
    public void itShouldHaveAStatusCode() {
        MethodNotAllowedHttpException he = new MethodNotAllowedHttpException("POST");
        assertThat(he.getStatusCode(), equalTo(StatusCode.METHOD_NOT_ALLOWED));
    }

    @Test
    public void itShouldSetAllow() {
        MethodNotAllowedHttpException he = new MethodNotAllowedHttpException("GET,POST");
        assertThat(he.getHeader("Allow"), equalTo(Option.of("GET,POST")));
    }
}

