package org.flint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import javaslang.control.Option;

public class StatusCodeTest {

    @Test
    public void itShouldGetTheMessageForAStatusCode() {
        assertThat(StatusCode.getMessage(StatusCode.OK), equalTo(Option.of("OK")));
        assertThat(StatusCode.getMessage(StatusCode.NOT_FOUND), equalTo(Option.of("Not Found")));
    }

}
