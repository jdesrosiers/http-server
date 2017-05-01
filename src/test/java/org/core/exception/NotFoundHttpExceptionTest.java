package org.core.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.core.StatusCode;

@RunWith(DataProviderRunner.class)
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
