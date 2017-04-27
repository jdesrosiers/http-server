package org.httpserver.controller;

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
import java.nio.file.Paths;

import javaslang.Tuple;
import javaslang.collection.HashMap;
import javaslang.control.Option;

import org.core.OriginForm;
import org.core.Response;
import org.core.Request;
import org.core.StatusCode;
import org.util.FileSystem;

@RunWith(DataProviderRunner.class)
public class FileSystemControllerTest {

    @Test
    public void itShouldGetAFileFromTheFileSystem() throws IOException {
        FileSystemController controller = new FileSystemController(Paths.get("public"));
        Request request = new Request("GET", new OriginForm("/file1"), HashMap.empty(), "");
        Response response = controller.get(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("application/octet-stream")));
        assertThat(response.getBodyAsString(), equalTo("file1 contents"));
    }

    @Test
    public void itShould404WhenGettingANonexistentFileFromTheFileSystem() throws IOException {
        FileSystemController controller = new FileSystemController(Paths.get("public"));
        Request request = new Request("GET", new OriginForm("/foobar"), HashMap.empty(), "");
        Response response = controller.get(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
    }

    @Test
    public void itShouldGetADirectoryListingFromTheFileSystem() throws IOException {
        FileSystemController controller = new FileSystemController(Paths.get("public"));
        Request request = new Request("GET", new OriginForm("/"), HashMap.empty(), "");
        Response response = controller.index(request);
        String body = response.getBodyAsString();

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("text/html; charset=utf-8")));
        assertThat(body, containsString("Index - /"));
        assertThat(body, containsString("<a href=\"/file1\">file1</a>"));
    }

    @Test
    public void itShouldDeleteAFileFromTheFileSystem() throws IOException {
        Files.copy(Paths.get("public/file1"), Paths.get("public/foo"));

        FileSystemController controller = new FileSystemController(Paths.get("public"));
        Request request = new Request("DELETE", new OriginForm("/foo"), HashMap.empty(), "");
        Response response = controller.delete(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
    }

    @Test
    public void itShouldPutANewFileToTheFileSystem() throws IOException {
        FileSystemController controller = new FileSystemController(Paths.get("public"));
        Request request = new Request("PUT", new OriginForm("/foo"), HashMap.empty(), "foo contents");
        Response response = controller.write(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.CREATED));
        assertThat(FileSystem.fileToString(Paths.get("public/foo")), equalTo("foo contents"));
    }

    @Test
    public void itShouldPutAnExistingFileToTheFileSystem() throws IOException {
        Files.copy(Paths.get("public/file1"), Paths.get("public/foo"));

        FileSystemController controller = new FileSystemController(Paths.get("public"));
        Request request = new Request("PUT", new OriginForm("/foo"), HashMap.empty(), "foo contents");
        Response response = controller.write(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(FileSystem.fileToString(Paths.get("public/foo")), equalTo("foo contents"));
    }

    @Test
    public void itShouldOnlyAcceptPatchRequestsInUnixDiffFormat() throws IOException, InterruptedException {
        Files.copy(Paths.get("public/patch-content.txt"), Paths.get("public/foo"));

        FileSystemController controller = new FileSystemController(Paths.get("public"));
        HashMap<String, String> requestHeaders = HashMap.ofEntries(
            Tuple.of("Content-Type", "text/plain; charset=utf-8")
        );
        Request request = new Request("PATCH", new OriginForm("/foo"), requestHeaders, "foo contents");
        Response response = controller.patch(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.UNSUPPORTED_MEDIA_TYPE));
        assertThat(response.getHeader("Accept-Patch"), equalTo(Option.of("application/unix-patch")));
    }

    @Test
    public void itShould404OnAPatchRequestToANonexistentResource() throws IOException, InterruptedException {
        FileSystemController controller = new FileSystemController(Paths.get("public"));
        HashMap<String, String> requestHeaders = HashMap.ofEntries(
            Tuple.of("Content-Type", "application/unix-patch")
        );

        StringBuilder patch = new StringBuilder();
        patch.append("1c1\n");
        patch.append("< default content\n");
        patch.append("\\ No newline at end of file\n");
        patch.append("---\n");
        patch.append("> foo content\n");
        patch.append("\\ No newline at end of file\n");

        Request request = new Request("PATCH", new OriginForm("/foo"), requestHeaders, patch.toString());
        Response response = controller.patch(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
    }

    @Test
    public void itShould412WhenTryingToPatchAndEtagsDontMatch() throws IOException, InterruptedException {
        Files.copy(Paths.get("public/patch-content.txt"), Paths.get("public/foo"));

        FileSystemController controller = new FileSystemController(Paths.get("public"));
        HashMap<String, String> requestHeaders = HashMap.ofEntries(
            Tuple.of("Content-Type", "application/unix-patch"),
            Tuple.of("If-Match", "xxx")
        );

        StringBuilder patch = new StringBuilder();
        patch.append("1c1\n");
        patch.append("< default content\n");
        patch.append("\\ No newline at end of file\n");
        patch.append("---\n");
        patch.append("> foo content\n");
        patch.append("\\ No newline at end of file\n");

        Request request = new Request("PATCH", new OriginForm("/foo"), requestHeaders, patch.toString());
        Response response = controller.patch(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.PRECONDITION_FAILED));
    }

    @Test
    public void itShouldPatchADocument() throws IOException, InterruptedException {
        Files.copy(Paths.get("public/patch-content.txt"), Paths.get("public/foo"));

        FileSystemController controller = new FileSystemController(Paths.get("public"));
        HashMap<String, String> requestHeaders = HashMap.ofEntries(
            Tuple.of("Content-Type", "application/unix-patch"),
            Tuple.of("If-Match", "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec")
        );

        StringBuilder patch = new StringBuilder();
        patch.append("1c1\n");
        patch.append("< default content\n");
        patch.append("\\ No newline at end of file\n");
        patch.append("---\n");
        patch.append("> foo content\n");
        patch.append("\\ No newline at end of file\n");

        Request request = new Request("PATCH", new OriginForm("/foo"), requestHeaders, patch.toString());
        Response response = controller.patch(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NO_CONTENT));
        assertThat(FileSystem.fileToString(Paths.get("public/foo")), equalTo("foo content"));
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("public/foo"));
    }

}
