package org.flint.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.collection.HashMap;
import javaslang.control.Option;

import org.flint.StatusCode;

@RunWith(DataProviderRunner.class)
public class HttpExceptionTest {

    @Test
    public void itShouldBeAnUncheckedException() {
        HttpException he = new HttpException(StatusCode.NOT_FOUND);
        assertThat(he, instanceOf(RuntimeException.class));
    }

    @Test
    public void itShouldHaveAStatusCode() {
        HttpException he = new HttpException(StatusCode.NOT_FOUND);
        assertThat(he.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
    }

    @Test
    public void itShouldAcceptHeaders() {
        HttpException he = new HttpException(StatusCode.NOT_FOUND);
        he.setHeader("Content-Length", "3");
        assertThat(he.getHeader("Content-Length"), equalTo(Option.of("3")));
    }

    @Test
    public void itShouldGiveAllHeaders() {
        HttpException he = new HttpException(StatusCode.NOT_FOUND);
        he.setHeader("Content-Length", "3");
        assertThat(he.getHeaders(), equalTo(HashMap.of("Content-Length", "3")));
    }

}
