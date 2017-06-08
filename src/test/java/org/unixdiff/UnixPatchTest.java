package org.unixdiff;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

public class UnixPatchTest {

    @Test
    public void isShouldApplyAPatch() throws InterruptedException, IOException {
        InputStream subject = new ByteArrayInputStream("default content".getBytes());

        StringBuilder patch = new StringBuilder();
        patch.append("1c1\n");
        patch.append("< default content\n");
        patch.append("\\ No newline at end of file\n");
        patch.append("---\n");
        patch.append("> foo content\n");
        patch.append("\\ No newline at end of file\n");

        UnixPatch patcher = UnixPatch.patch(subject, patch.toString());

        assertThat(patcher.getStatus(), equalTo(0));
        assertThat(patcher.getPatchedContent(), equalTo("foo content"));
    }
}
