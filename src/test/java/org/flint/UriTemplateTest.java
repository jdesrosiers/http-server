package org.flint;

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

}
