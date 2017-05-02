package org.flint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;

@RunWith(DataProviderRunner.class)
public class StatusCodeTest {

    @Test
    public void itShouldGetTheMessageForAStatusCode() {
        assertThat(StatusCode.getMessage(StatusCode.OK), equalTo(Option.of("OK")));
        assertThat(StatusCode.getMessage(StatusCode.NOT_FOUND), equalTo(Option.of("Not Found")));
    }

}
