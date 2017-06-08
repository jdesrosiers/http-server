package org.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FileSystemTest {

    @Test
    public void itShouldCopyStreams() throws IOException {
        String subject = "Hello World\nFoo\nBar";
        ByteArrayInputStream in = new ByteArrayInputStream(subject.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        FileSystem.copyStreams(in, out);

        assertThat(out.toString(), equalTo(subject));
    }

}
