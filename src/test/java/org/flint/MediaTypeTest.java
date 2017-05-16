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
public class MediaTypeTest {

    @DataProvider
    public static Object[][] dataProviderExtensionToMediaType() {
        return new Object [][] {
            { "txt", "text/plain" },
            { "html", "text/html" },
            { "htm", "text/html" },
            { "jpeg", "image/jpeg" },
            { "jpg", "image/jpeg" },
            { "png", "image/png" },
            { "gif", "image/gif" }
        };
    }

    @Test
    @UseDataProvider("dataProviderExtensionToMediaType")
    public void itShouldMapExtensionToMediaType(String extension, String mediaType) {
        assertThat(MediaType.get(extension), equalTo(Option.of(mediaType)));
    }

}
