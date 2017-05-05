package org.flint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.util.FileSystem;

@RunWith(DataProviderRunner.class)
public class UnixPatchTest {

    @Test
    public void isShouldApplyAPatch() throws InterruptedException, IOException {
        Path targetPath = Paths.get("src/test/resources/foo.txt");
        Files.copy(Paths.get("src/test/resources/patch-content.txt"), targetPath);

        StringBuilder patch = new StringBuilder();
        patch.append("1c1\n");
        patch.append("< default content\n");
        patch.append("\\ No newline at end of file\n");
        patch.append("---\n");
        patch.append("> foo content\n");
        patch.append("\\ No newline at end of file\n");

        UnixPatch patcher = UnixPatch.patch(targetPath, patch.toString());
        assertThat(patcher.getStatus(), equalTo(0));
        assertThat(FileSystem.fileToString(targetPath), equalTo("foo content"));
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("src/test/resources/foo.txt.tmp.rej"));
        Files.deleteIfExists(Paths.get("src/test/resources/foo.txt.tmp"));
        Files.deleteIfExists(Paths.get("src/test/resources/foo.txt"));
    }
}
