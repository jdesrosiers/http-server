package org.cobspec.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import javaslang.control.Option;

import org.flint.datastore.DataStoreException;
import org.flint.request.Method;
import org.flint.request.OriginForm;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

public class FileSystemControllerTest {

    @Test
    public void itShouldServeADirectoryListingForDirectories() throws DataStoreException {
        FileSystemController controller = new FileSystemController(Paths.get("src/test/resources"));
        Request request = new Request(Method.GET, new OriginForm("/"));
        Response response = controller.get(request);

        String body = response.getBodyAsString();
        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("text/html")));
        assertThat(body, containsString("Index - /"));
        assertThat(body, containsString("<a href=\"/file1.txt\">file1.txt</a>"));
    }

    @Test
    public void itShouldPatchWithUnixPatch() throws DataStoreException, IOException {
        Path rootPath = Files.createTempDirectory(null);
        Path patchContentPath = Paths.get(rootPath + "/patch-content.txt");
        Files.copy(Paths.get("src/test/resources/patch-content.txt"), patchContentPath);
        FileSystemController controller = new FileSystemController(rootPath);

        StringBuilder patch = new StringBuilder();
        patch.append("1c1\n");
        patch.append("< default content\n");
        patch.append("\\ No newline at end of file\n");
        patch.append("---\n");
        patch.append("> foo content\n");
        patch.append("\\ No newline at end of file\n");

        Request request = new Request(Method.PATCH, new OriginForm("/patch-content.txt"));
        request.setHeader("Content-Type", "application/unix-diff");
        request.setHeader("If-Match", "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec");
        request.setBody(patch.toString());
        Response response = controller.patch(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NO_CONTENT));
        assertThat(new String(Files.readAllBytes(patchContentPath)), equalTo("foo content"));
    }

}
