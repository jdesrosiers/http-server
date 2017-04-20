package org.httpserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class FileSystemTest {

    @DataProvider
    public static Object[][] dataProviderExtensions() {
        return new Object[][] {
            { "a/b/c", "" },
            { "foo.txt", "txt" },
            { "a/b/c.jpg", "jpg" },
            { "a/b.txt/c", "" }
        };
    }

    @Test
    @UseDataProvider("dataProviderExtensions")
    public void itShouldGetTheExtensionFromAPath(String subject, String extension) {
        assertThat(FileSystem.getExtension(subject), equalTo(extension));
    }
}
