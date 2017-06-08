package org.flint.datastore;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;

public class FileSystemDataStore implements DataStore {
    private Path rootPath;
    private IndexHandler indexHandler;

    public FileSystemDataStore(final Path rootPath, final IndexHandler indexHandler) {
        this.rootPath = rootPath;
        this.indexHandler = indexHandler;
    }

    public FileSystemDataStore(final Path rootPath) {
        this.rootPath = rootPath;
    }

    public InputStream fetch(final String identifier) throws DataStoreException {
        Path path = getPath(identifier);

        try {
            return Files.isDirectory(path) && indexHandler != null ? indexHandler.handle(path) : Files.newInputStream(path);
        } catch (final IOException ioe) {
            throw new DataStoreException(ioe);
        }
    }

    public boolean contains(final String identifier) {
        return Files.exists(getPath(identifier));
    }

    public void save(final String identifier, final String data) throws DataStoreException {
        Path path = getPath(identifier);

        try {
            Files.createDirectories(path.getParent());
            Files.copy(new ByteArrayInputStream(data.getBytes()), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException ioe) {
            throw new DataStoreException(ioe);
        }
    }

    public void delete(final String identifier) throws DataStoreException {
        try {
            Files.delete(getPath(identifier));
        } catch (final IOException ioe) {
            throw new DataStoreException(ioe);
        }
    }

    public String hashOf(final String identifier) throws DataStoreException {
        Path path = getPath(identifier);
        try {
            byte[] contents = Files.readAllBytes(path);
            byte[] hash = MessageDigest.getInstance("SHA1").digest(contents);
            return DatatypeConverter.printHexBinary(hash);
        } catch (Exception e) {
            throw new DataStoreException(e);
        }
    }

    private Path getPath(final String identifier) {
        return Paths.get(rootPath + identifier);
    }
}
