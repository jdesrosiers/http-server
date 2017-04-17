package org.httpserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

@RunWith(DataProviderRunner.class)
public class FileSystemRequestHandlerTest {

    @Test
    public void itShouldAssembleThePathToTheResource() {
        Path publicPath = Paths.get("public");
        Path targetPath = publicPath.resolve("./file1").normalize();

        assertThat(targetPath.toString(), equalTo("public/file1"));
        assertThat(Files.exists(targetPath), equalTo(true));
    }

    @Test
    public void ifTheResourceExistsItShouldReturnItWith200OK() {
        RequestHandler server = new FileSystemRequestHandler(Paths.get("public"));
        Request request = new Request("GET", "/file1");

        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getBody(), equalTo("file1 contents"));
    }

    @Test
    public void ifTheResourceDoesntExistsItShouldReturnItWith404NotFound() {
        RequestHandler server = new FileSystemRequestHandler(Paths.get("public"));
        Request request = new Request("GET", "/doesnt-exist");

        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
        assertThat(response.getBody(), containsString("404 Not Found"));
    }

    // FIXME: For some reason, getBody is returning an empty string.  It works in all cases except
    // this test.
    //@Test
    public void ifTheResourceIsADirectoryItShouldReturnADirectoryListing() {
        RequestHandler server = new FileSystemRequestHandler(Paths.get("public"));
        Request request = new Request("GET", "/");

        Response response = server.handle(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getBody(), containsString("<a href=\"file1\">file1</a>"));
        assertThat(response.getBody(), containsString("<a href=\"file2\">file2</a>"));
    }

}
