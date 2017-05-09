package org.flint.cookie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;

@RunWith(DataProviderRunner.class)
public class CookieTest {

    @Test
    public void itShouldStartOutEmpty() {
        Cookie cookie = new Cookie();
        assertThat(cookie.isEmpty(), equalTo(true));
    }

    @Test
    public void itShouldReturnNoneIfGettingAValueThatDoesntExist() {
        Cookie cookie = new Cookie();
        assertThat(cookie.get("foo"), equalTo(Option.none()));
    }

    @Test
    public void itShouldBeAbleToAddValues() {
        Cookie cookie = new Cookie();
        cookie.put("foo", "bar");
        assertThat(cookie.isEmpty(), equalTo(false));
        assertThat(cookie.get("foo"), equalTo(Option.of("bar")));
    }

    @Test
    public void itShouldContructACookieFromASetCookieHeader() {
        Cookie cookie = new Cookie("foo=bar");
        assertThat(cookie.get("foo"), equalTo(Option.of("bar")));
    }

    @DataProvider
    public static Object[][] dataProviderCookies() {
        return new Object[][] {
            { "foo=bar" },
            { "abc=123; foo=bar" }
        };
    }

    @Test
    @UseDataProvider("dataProviderCookies")
    public void itShouldBeSerializableToASetCookieHeader(String subject) {
        Cookie cookie = new Cookie(subject);
        assertThat(cookie.toString(), equalTo(subject));
    }

}
