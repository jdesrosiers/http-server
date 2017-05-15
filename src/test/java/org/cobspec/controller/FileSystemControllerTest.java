package org.cobspec.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.fail;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javaslang.Tuple;
import javaslang.collection.HashMap;
import javaslang.control.Option;

import org.flint.exception.NotFoundHttpException;
import org.flint.exception.PreconditionFailedHttpException;
import org.flint.exception.UnsupportedMediaTypeHttpException;
import org.flint.request.Method;
import org.flint.request.Request;
import org.flint.request.OriginForm;
import org.flint.response.Response;
import org.flint.response.StatusCode;
import org.util.FileSystem;

@RunWith(DataProviderRunner.class)
public class FileSystemControllerTest {

    @DataProvider
    public static Object[] dataProviderDirectories() {
        return new Object[][] {
            { "src/test/resources", "/foo", "src/test/resources/foo" },
            { ".", "/foo", "foo" },
            { ".", "/", "." }
        };
    }

    @Test
    @UseDataProvider("dataProviderDirectories")
    public void itShouldCalculateTheTargetPath(String directory, String uri, String expected) {
        FileSystemController controller = new FileSystemController(Paths.get(directory));
        Request request = new Request(Method.GET, new OriginForm(uri));

        assertThat(controller.getTargetPath(request).toString(), equalTo(expected));
    }

    @Test
    public void itShouldGetAFileFromTheFileSystem() throws IOException {
        FileSystemController controller = new FileSystemController(Paths.get("src/test/resources"));
        Request request = new Request(Method.GET, new OriginForm("/file1"));
        Response response = controller.get(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("text/plain")));
        assertThat(response.getBodyAsString(), equalTo("file1 contents"));
    }

    @Test(expected=NotFoundHttpException.class)
    public void itShould404WhenGettingANonexistentFileFromTheFileSystem() throws IOException {
        FileSystemController controller = new FileSystemController(Paths.get("src/test/resources"));
        Request request = new Request(Method.GET, new OriginForm("/foobar"));
        Response response = controller.get(request);
    }

    @Test
    public void itShouldGetADirectoryListingFromTheFileSystem() throws IOException {
        FileSystemController controller = new FileSystemController(Paths.get("src/test/resources"));
        Request request = new Request(Method.GET, new OriginForm("/"));
        Response response = controller.get(request);
        String body = response.getBodyAsString();

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("text/html; charset=utf-8")));
        assertThat(body, containsString("Index - /"));
        assertThat(body, containsString("<a href=\"/file1\">file1</a>"));
    }

    @Test
    public void itShouldDeleteAFileFromTheFileSystem() throws IOException {
        Files.copy(Paths.get("src/test/resources/file1"), Paths.get("src/test/resources/foo"));

        FileSystemController controller = new FileSystemController(Paths.get("src/test/resources"));
        Request request = new Request(Method.DELETE, new OriginForm("/foo"));
        Response response = controller.delete(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
    }

    @Test
    public void itShouldPutANewFileToTheFileSystem() throws IOException {
        FileSystemController controller = new FileSystemController(Paths.get("src/test/resources"));
        Request request = new Request(Method.PUT, new OriginForm("/foo"));
        request.setBody("foo contents");
        Response response = controller.write(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.CREATED));
        assertThat(FileSystem.fileToString(Paths.get("src/test/resources/foo")), equalTo("foo contents"));
    }

    @Test
    public void itShouldPutAnExistingFileToTheFileSystem() throws IOException {
        Files.copy(Paths.get("src/test/resources/file1"), Paths.get("src/test/resources/foo"));

        FileSystemController controller = new FileSystemController(Paths.get("src/test/resources"));
        Request request = new Request(Method.PUT, new OriginForm("/foo"));
        request.setBody("foo contents");
        Response response = controller.write(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(FileSystem.fileToString(Paths.get("src/test/resources/foo")), equalTo("foo contents"));
    }

    @Test
    public void itShouldOnlyAcceptPatchRequestsInUnixDiffFormat() throws IOException, InterruptedException {
        Files.copy(Paths.get("src/test/resources/patch-content.txt"), Paths.get("src/test/resources/foo"));

        FileSystemController controller = new FileSystemController(Paths.get("src/test/resources"));
        Request request = new Request(Method.PATCH, new OriginForm("/foo"));
        request.setBody("foo contents");
        request.setHeader("Content-Type", "text/plain; charset=utf-8");

        try {
            Response response = controller.patch(request);
            fail();
        } catch (UnsupportedMediaTypeHttpException he) {
            assertThat(he.getHeader("Accept-Patch"), equalTo(Option.of("application/unix-diff")));
        }
    }

    @Test(expected=NotFoundHttpException.class)
    public void itShould404OnAPatchRequestToANonexistentResource() throws IOException, InterruptedException {
        FileSystemController controller = new FileSystemController(Paths.get("src/test/resources"));

        StringBuilder patch = new StringBuilder();
        patch.append("1c1\n");
        patch.append("< default content\n");
        patch.append("\\ No newline at end of file\n");
        patch.append("---\n");
        patch.append("> foo content\n");
        patch.append("\\ No newline at end of file\n");

        Request request = new Request(Method.PATCH, new OriginForm("/foo"));
        request.setHeader("Content-Type", "application/unix-diff");
        request.setBody(patch.toString());
        Response response = controller.patch(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
    }

    @Test(expected=PreconditionFailedHttpException.class)
    public void itShould412WhenTryingToPatchAndEtagsDontMatch() throws IOException, InterruptedException {
        Files.copy(Paths.get("src/test/resources/patch-content.txt"), Paths.get("src/test/resources/foo"));

        FileSystemController controller = new FileSystemController(Paths.get("src/test/resources"));

        StringBuilder patch = new StringBuilder();
        patch.append("1c1\n");
        patch.append("< default content\n");
        patch.append("\\ No newline at end of file\n");
        patch.append("---\n");
        patch.append("> foo content\n");
        patch.append("\\ No newline at end of file\n");

        Request request = new Request(Method.PATCH, new OriginForm("/foo"));
        request.setHeader("Content-Type", "application/unix-diff");
        request.setHeader("If-Match", "xxx");
        request.setBody(patch.toString());
        Response response = controller.patch(request);
    }

    @Test
    public void itShouldPatchADocument() throws IOException, InterruptedException {
        Files.copy(Paths.get("src/test/resources/patch-content.txt"), Paths.get("src/test/resources/foo"));

        FileSystemController controller = new FileSystemController(Paths.get("src/test/resources"));

        StringBuilder patch = new StringBuilder();
        patch.append("1c1\n");
        patch.append("< default content\n");
        patch.append("\\ No newline at end of file\n");
        patch.append("---\n");
        patch.append("> foo content\n");
        patch.append("\\ No newline at end of file\n");

        Request request = new Request(Method.PATCH, new OriginForm("/foo"));
        request.setHeader("Content-Type", "application/unix-diff");
        request.setHeader("If-Match", "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec");
        request.setBody(patch.toString());
        Response response = controller.patch(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NO_CONTENT));
        assertThat(FileSystem.fileToString(Paths.get("src/test/resources/foo")), equalTo("foo content"));
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("src/test/resources/foo"));
    }

}
