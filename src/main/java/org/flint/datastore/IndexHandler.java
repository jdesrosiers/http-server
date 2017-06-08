package org.flint.datastore;

import java.io.InputStream;
import java.nio.file.Path;

public interface IndexHandler {
    public InputStream handle(final Path indexPath) throws DataStoreException;
}
