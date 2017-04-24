package org.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
            { "txt", "text/plain; charset=utf-8" },
            { "html", "text/html; charset=utf-8" },
            { "htm", "text/html; charset=utf-8" },
            { "jpeg", "image/jpeg" },
            { "jpg", "image/jpeg" },
            { "png", "image/png" },
            { "gif", "image/gif" }
        };
    }

    @Test
    @UseDataProvider("dataProviderExtensionToMediaType")
    public void itShouldMapExtensionToMediaType(String extension, String mediaType) {
        assertThat(MediaType.fromExtension(extension), equalTo(Option.of(mediaType)));
    }

}
