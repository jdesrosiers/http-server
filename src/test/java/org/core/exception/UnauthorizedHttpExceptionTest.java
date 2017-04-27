package org.core.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;

import org.core.StatusCode;

@RunWith(DataProviderRunner.class)
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
        UnauthorizedHttpException he = new UnauthorizedHttpException("Basic realm-\"foo\"");
        assertThat(he.getHeader("WWW-Authenticate"), equalTo(Option.of("Basic realm-\"foo\"")));
    }
}
