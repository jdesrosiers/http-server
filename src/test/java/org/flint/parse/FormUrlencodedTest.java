package org.flint.parse;

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
public class FormUrlencodedTest {

    @Test
    public void itShouldParseAnEmptyString() {
        HashMap<String, String> content = FormUrlencoded.PARSER.parse("");
        assertThat(content.isEmpty(), equalTo(true));
    }

    @Test
    public void itShouldParseASingleValue() {
        HashMap<String, String> content = FormUrlencoded.PARSER.parse("foo");
        assertThat(content.get("foo"), equalTo(Option.some(null)));
    }

    @Test
    public void itShouldParseAKeyValuePair() {
        HashMap<String, String> content = FormUrlencoded.PARSER.parse("foo=bar");
        assertThat(content.get("foo"), equalTo(Option.of("bar")));
    }

    @Test
    public void itShouldParseMultipleKeyValuePairs() {
        HashMap<String, String> content = FormUrlencoded.PARSER.parse("foo=bar&bar=foo");
        assertThat(content.get("foo"), equalTo(Option.of("bar")));
        assertThat(content.get("bar"), equalTo(Option.of("foo")));
    }

}
