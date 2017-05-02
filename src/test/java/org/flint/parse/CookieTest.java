package org.flint.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import javaslang.collection.HashMap;
import javaslang.control.Option;

public class CookieTest {

    @Test
    public void itShouldParseASinglePair() {
        HashMap<String, String> cookies = Cookie.PARSER.parse("foo=bar");
        assertThat(cookies.get("foo"), equalTo(Option.of("bar")));
    }

    @Test
    public void itShouldParseMultiplePairs() {
        HashMap<String, String> cookies = Cookie.PARSER.parse("foo=bar; abc=123");
        assertThat(cookies.get("foo"), equalTo(Option.of("bar")));
        assertThat(cookies.get("abc"), equalTo(Option.of("123")));
    }

}
