package org.flint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import javaslang.control.Option;

public class OriginFormTest {

    @Test
    public void itShouldHaveAPathPart() {
        OriginForm originForm = new OriginForm("/");
        assertThat(originForm.getPath(), equalTo("/"));
    }

    @Test
    public void itShouldHaveAQueryPart() {
        OriginForm originForm = new OriginForm("/", "foo=bar");
        assertThat(originForm.getQuery(), equalTo(Option.of("foo=bar")));
    }

}
