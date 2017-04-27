package org.core.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.collection.HashMap;
import javaslang.control.Option;

@RunWith(DataProviderRunner.class)
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
