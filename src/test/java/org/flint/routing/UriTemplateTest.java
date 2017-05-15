package org.flint.routing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import javaslang.collection.HashMap;
import javaslang.control.Option;

public class UriTemplateTest {

    @Test
    public void testSimpleMatch() {
        UriTemplate uriTemplate = new UriTemplate("/foo");
        assertThat(uriTemplate.getMatchFor("/foo"), equalTo(Option.of(HashMap.of())));
    }

    @Test
    public void testSimpleFail() {
        UriTemplate uriTemplate = new UriTemplate("/foo");
        assertThat(uriTemplate.getMatchFor("/bar"), equalTo(Option.none()));
    }

    @Test
    public void testWildcardMatch() {
        UriTemplate uriTemplate = new UriTemplate("*");
        assertThat(uriTemplate.getMatchFor("/bar"), equalTo(Option.of(HashMap.of())));
    }

}
