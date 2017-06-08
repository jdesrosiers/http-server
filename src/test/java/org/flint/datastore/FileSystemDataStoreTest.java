package org.flint.datastore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.util.FileSystem;

public class FileSystemDataStoreTest {

    @Test
    public void itShouldFetchAFileFromTheDataStore() throws DataStoreException, IOException {
        Path rootPath = Paths.get("src/test/resources");
        DataStore dataStore = new FileSystemDataStore(rootPath);

        InputStream actual = dataStore.fetch("/file1.txt");
        InputStream expected = Files.newInputStream(Paths.get("src/test/resources/file1.txt"));
        assertThat(FileSystem.inputStreamToString(actual), equalTo(FileSystem.inputStreamToString(expected)));
    }

    @Test
    public void itShouldKnowIfAResourceIsInTheDataStore() throws DataStoreException, IOException {
        Path rootPath = Paths.get("src/test/resources");
        DataStore dataStore = new FileSystemDataStore(rootPath);

        assertThat(dataStore.contains("/file1.txt"), equalTo(true));
    }

    @Test
    public void itShouldGetADirectoryListingFromTheDataStore() throws DataStoreException, IOException {
        Path rootPath = Paths.get("src/test/resources");
        DataStore dataStore = new FileSystemDataStore(rootPath, indexPath -> new ByteArrayInputStream("foo".getBytes()));

        String body = FileSystem.inputStreamToString(dataStore.fetch("/"));

        assertThat(body, equalTo("foo"));
    }

    @Test
    public void itShouldSaveAFileToTheDataStore() throws DataStoreException, IOException {
        Path rootPath = Files.createTempDirectory(null);
        DataStore dataStore = new FileSystemDataStore(rootPath);

        dataStore.save("/file1.txt", "file1 contents");

        InputStream result = Files.newInputStream(Paths.get(rootPath + "/file1.txt"));
        assertThat(FileSystem.inputStreamToString(result), equalTo("file1 contents"));
    }

    @Test
    public void itShouldDeleteAFileFromTheDataStore() throws DataStoreException, IOException {
        Path rootPath = Files.createTempDirectory(null);
        Files.copy(Paths.get("src/test/resources/file1.txt"), Paths.get(rootPath + "/file1.txt"));
        assertThat(Files.exists(Paths.get(rootPath + "/file1.txt")), equalTo(true));

        DataStore dataStore = new FileSystemDataStore(rootPath);
        dataStore.delete("/file1.txt");

        assertThat(Files.exists(Paths.get(rootPath + "/file1.txt")), equalTo(false));
    }

    @Test
    public void itShouldGetTheHashOfAFileInTheDataStore() throws DataStoreException {
        Path rootPath = Paths.get("src/test/resources");
        DataStore dataStore = new FileSystemDataStore(rootPath);

        assertThat(dataStore.hashOf("/file1.txt"), equalTo("A379624177ABC4679CAFAFA8EAE1D73E1478AAA6"));
    }

}
