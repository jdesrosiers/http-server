package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

import javaslang.control.Option;

import org.flint.request.Method;
import org.flint.response.StatusCode;

public class MethodNotAllowedHttpExceptionTest {

    @Test
    public void itShouldBeAnHttpException() {
        MethodNotAllowedHttpException he = new MethodNotAllowedHttpException(Method.GET);
        assertThat(he, instanceOf(HttpException.class));
    }

    @Test
    public void itShouldHaveAStatusCode() {
        MethodNotAllowedHttpException he = new MethodNotAllowedHttpException(Method.POST);
        assertThat(he.getStatusCode(), equalTo(StatusCode.METHOD_NOT_ALLOWED));
    }

    @Test
    public void itShouldSetAllow() {
        String allowed = Method.GET + "," + Method.POST;
        MethodNotAllowedHttpException he = new MethodNotAllowedHttpException(allowed);
        assertThat(he.getHeader("Allow"), equalTo(Option.of(allowed)));
    }
}

