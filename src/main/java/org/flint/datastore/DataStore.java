package org.flint.datastore;

import java.io.InputStream;

public interface DataStore {
    public InputStream fetch(final String identifier) throws DataStoreException;
    public boolean contains(final String identifier);
    public void save(final String identifier, final String data) throws DataStoreException;
    public void delete(final String identifier) throws DataStoreException;
    public String hashOf(final String identifier) throws DataStoreException;
}
