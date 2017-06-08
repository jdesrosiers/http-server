package org.flint.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import static org.junit.Assert.fail;

import java.io.IOException;

import javaslang.collection.HashMap;
import javaslang.control.Option;
import javaslang.Tuple;

import org.flint.datastore.DataStore;
import org.flint.datastore.DataStoreException;
import org.flint.datastore.MapDataStore;
import org.flint.exception.NotFoundHttpException;
import org.flint.exception.PreconditionFailedHttpException;
import org.flint.exception.UnsupportedMediaTypeHttpException;
import org.flint.request.Method;
import org.flint.request.Request;
import org.flint.request.OriginForm;
import org.flint.response.Response;
import org.flint.response.StatusCode;
import org.util.FileSystem;

public class ControllerTest {

    @Test
    public void itShouldGetAFileFromTheDataStore() throws DataStoreException {
        DataStore dataStore = new MapDataStore(HashMap.ofEntries(Tuple.of("/file1.txt", "file1 contents")));
        Controller controller = new Controller(dataStore);
        Request request = new Request(Method.GET, new OriginForm("/file1.txt"));
        Response response = controller.get(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(response.getHeader("Content-Type"), equalTo(Option.of("text/plain")));
        assertThat(response.getBodyAsString(), equalTo("file1 contents"));
    }

    @Test(expected=NotFoundHttpException.class)
    public void itShould404WhenGettingANonexistentFileFromTheDataStore() throws DataStoreException {
        DataStore dataStore = new MapDataStore(HashMap.empty());
        Controller controller = new Controller(dataStore);
        Request request = new Request(Method.GET, new OriginForm("/foobar"));
        Response response = controller.get(request);
    }

    @Test
    public void itShouldDeleteAFileFromTheDataStore() throws DataStoreException {
        DataStore dataStore = new MapDataStore(HashMap.ofEntries(Tuple.of("/foo", "foo")));
        Controller controller = new Controller(dataStore);
        Request request = new Request(Method.DELETE, new OriginForm("/foo"));
        Response response = controller.delete(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(dataStore.contains("/foo"), equalTo(false));
    }

    @Test
    public void itShouldPutANewFileToTheDataStore() throws DataStoreException, IOException {
        DataStore dataStore = new MapDataStore(HashMap.empty());
        Controller controller = new Controller(dataStore);
        Request request = new Request(Method.PUT, new OriginForm("/foo"));
        request.setBody("foo contents");
        Response response = controller.put(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.CREATED));
        assertThat(FileSystem.inputStreamToString(dataStore.fetch("/foo")), equalTo("foo contents"));
    }

    @Test
    public void itShouldPutAnExistingFileToTheDataStore() throws DataStoreException, IOException {
        DataStore dataStore = new MapDataStore(HashMap.ofEntries(Tuple.of("/foo", "default content")));
        Controller controller = new Controller(dataStore);
        Request request = new Request(Method.PUT, new OriginForm("/foo"));
        request.setBody("foo contents");
        Response response = controller.put(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.OK));
        assertThat(FileSystem.inputStreamToString(dataStore.fetch("/foo")), equalTo("foo contents"));
    }

    @Test
    public void itShouldNotAcceptPatchFormatsItDoesUnderstand() throws DataStoreException {
        DataStore dataStore = new MapDataStore(HashMap.ofEntries(Tuple.of("/foo", "default content")));
        Controller controller = new Controller(dataStore);
        controller.addPatcher("application/unix-diff", (is, diff) -> new PatchResult(true, ""));
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
    public void itShould404OnAPatchRequestToANonexistentResource() throws DataStoreException {
        DataStore dataStore = new MapDataStore(HashMap.empty());
        Controller controller = new Controller(dataStore);

        Request request = new Request(Method.PATCH, new OriginForm("/foo"));
        request.setHeader("Content-Type", "application/unix-diff");
        request.setBody("patch");
        Response response = controller.patch(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NOT_FOUND));
    }

    @Test(expected=PreconditionFailedHttpException.class)
    public void itShould412WhenTryingToPatchAndEtagsDontMatch() throws DataStoreException {
        DataStore dataStore = new MapDataStore(HashMap.ofEntries(Tuple.of("/foo", "default content")));
        Controller controller = new Controller(dataStore);
        controller.addPatcher("application/unix-diff", (is, diff) -> new PatchResult(true, ""));

        Request request = new Request(Method.PATCH, new OriginForm("/foo"));
        request.setHeader("Content-Type", "application/unix-diff");
        request.setHeader("If-Match", "xxx");
        request.setBody("patch");
        Response response = controller.patch(request);
    }

    @Test
    public void itShouldPatchADocument() throws DataStoreException, IOException {
        DataStore dataStore = new MapDataStore(HashMap.ofEntries(Tuple.of("/foo", "default content")));
        Controller controller = new Controller(dataStore);
        controller.addPatcher("application/unix-diff", (is, diff) -> new PatchResult(true, "foo content"));

        Request request = new Request(Method.PATCH, new OriginForm("/foo"));
        request.setHeader("Content-Type", "application/unix-diff");
        request.setHeader("If-Match", "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec");
        request.setBody("some patch");
        Response response = controller.patch(request);

        assertThat(response.getStatusCode(), equalTo(StatusCode.NO_CONTENT));
        assertThat(FileSystem.inputStreamToString(dataStore.fetch("/foo")), equalTo("foo content"));
    }

}
