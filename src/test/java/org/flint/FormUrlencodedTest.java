package org.flint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;

@RunWith(DataProviderRunner.class)
public class FormUrlencodedTest {

    @Test
    public void itShouldStartOutEmpty() {
        FormUrlencoded values = new FormUrlencoded();
        assertThat(values.isEmpty(), equalTo(true));
    }

    @Test
    public void itShouldReturnNoneIfGettingAValueThatDoesntExist() {
        FormUrlencoded values = new FormUrlencoded();
        assertThat(values.get("foo"), equalTo(Option.none()));
    }

    @Test
    public void itShouldBeAbleToAddValues() {
        FormUrlencoded values = new FormUrlencoded();
        values.put("foo", "bar");
        assertThat(values.isEmpty(), equalTo(false));
        assertThat(values.get("foo"), equalTo(Option.of("bar")));
    }

    @Test
    public void itShouldConstructValuesFromAQueryString() {
        FormUrlencoded values = new FormUrlencoded("foo=bar");
        assertThat(values.get("foo"), equalTo(Option.of("bar")));
    }

    @Test
    public void itShouldDecodePecentEncodedValuesWhenConstructingValuesFromAQueryString() {
        FormUrlencoded values = new FormUrlencoded("foo%20bar=abc%20123");
        assertThat(values.get("foo bar"), equalTo(Option.of("abc 123")));
    }

    @DataProvider
    public static Object[][] dataProviderQueryStrings() {
        return new Object[][] {
            { "" },
            { "foo=bar" },
            { "foo" },
            { "abc=123&foo=bar" },
            { "abc%20123=foo%20bar" }
        };
    }

    @Test
    @UseDataProvider("dataProviderQueryStrings")
    public void itShouldBeSerializableToAString(String subject) {
        FormUrlencoded values = new FormUrlencoded(subject);
        assertThat(values.toQueryString(), equalTo(subject));
    }

}

