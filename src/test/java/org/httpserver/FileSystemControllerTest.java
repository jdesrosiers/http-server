package org.httpserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.collection.HashMap;
import javaslang.control.Option;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

@RunWith(DataProviderRunner.class)
public class FileSystemControllerTest {

    @Test
    public void itShouldGetAFileFromTheFileSystem() {
        FileSystemController controller = new FileSystemController(Paths.get("public"));
        Request request = new Request("GET", "/file1", HashMap.empty(), "");
        Response response = controller.get(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("application/octet-stream")));
        assertThat(response.getBodyAsString(), equalTo("file1 contents"));
    }

    @Test
    public void itShouldGetADirectoryListingFromTheFileSystem() {
        FileSystemController controller = new FileSystemController(Paths.get("public"));
        Request request = new Request("GET", "/", HashMap.empty(), "");
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
        Request request = new Request("DELETE", "/foo", HashMap.empty(), "");
        Response response = controller.delete(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
    }

    @Test
    public void itShouldPutANewFileToTheFileSystem() throws IOException {
        FileSystemController controller = new FileSystemController(Paths.get("public"));
        Request request = new Request("PUT", "/foo", HashMap.empty(), "foo contents");
        Response response = controller.write(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.CREATED));
        assertThat(new String(Files.readAllBytes(Paths.get("public/foo"))), equalTo("foo contents"));
    }

    @Test
    public void itShouldPutAnExistingFileToTheFileSystem() throws IOException {
        Files.copy(Paths.get("public/file1"), Paths.get("public/foo"));

        FileSystemController controller = new FileSystemController(Paths.get("public"));
        Request request = new Request("PUT", "/foo", HashMap.empty(), "foo contents");
        Response response = controller.write(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(new String(Files.readAllBytes(Paths.get("public/foo"))), equalTo("foo contents"));
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("public/foo"));
    }

}
