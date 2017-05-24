package org.flint.routing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.collection.HashMap;
import javaslang.control.Option;

@RunWith(DataProviderRunner.class)
public class UriTemplateTest {

    @Test
    public void itShouldMatchOnAnExactMatch() {
        UriTemplate uriTemplate = new UriTemplate("/foo");
        assertThat(uriTemplate.getMatchFor("/foo"), equalTo(Option.of(HashMap.of())));
    }

    @Test
    public void itShouldFailIfItDoesntMatch() {
        UriTemplate uriTemplate = new UriTemplate("/foo");
        assertThat(uriTemplate.getMatchFor("/bar"), equalTo(Option.none()));
    }

    @DataProvider
    public static Object[][] dataProviderWildcardTemplates() {
        return new Object[][] {
            { "*", "/bar" },
            { "/foo/*", "/foo/bar" },
            { "*.json", "/bar.json" },
            { "/foo/*.json", "/foo/bar.json" }
        };
    }

    @Test
    @UseDataProvider("dataProviderWildcardTemplates")
    public void itShouldMatchWildcardExperessions(String template, String subject) {
        UriTemplate uriTemplate = new UriTemplate(template);
        assertThat(uriTemplate.getMatchFor(subject), equalTo(Option.of(HashMap.of())));
    }

}
