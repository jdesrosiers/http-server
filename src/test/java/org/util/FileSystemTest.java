package org.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    @Test
    public void itShouldCopyStreams() throws IOException {
        String subject = "Hello World\nFoo\nBar";
        ByteArrayInputStream in = new ByteArrayInputStream(subject.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        FileSystem.copyStreams(in, out);

        assertThat(out.toString(), equalTo(subject));
    }

}
