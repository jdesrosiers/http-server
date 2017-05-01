package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.flint.StatusCode;

@RunWith(DataProviderRunner.class)
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
