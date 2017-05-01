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
import javaslang.Tuple;

@RunWith(DataProviderRunner.class)
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
