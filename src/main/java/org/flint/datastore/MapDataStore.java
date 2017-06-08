package org.flint.datastore;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;

import javaslang.collection.Map;

public class MapDataStore implements DataStore {
    private Map<String, String> store;

    public MapDataStore(Map<String, String> store) {
        this.store = store;
    }

    public InputStream fetch(final String identifier) throws DataStoreException {
        return store.get(identifier)
            .map(String::getBytes)
            .map(ByteArrayInputStream::new)
            .getOrElseThrow(() -> new DataStoreException());
    }

    public boolean contains(final String identifier) {
        return store.get(identifier).isDefined();
    }

    public void save(final String identifier, final String data) throws DataStoreException {
        this.store = store.put(identifier, data);
    }

    public void delete(final String identifier) throws DataStoreException {
        this.store = store.remove(identifier);
    }

    public String hashOf(final String identifier) throws DataStoreException {
        try {
            byte[] contents = store.get(identifier).get().getBytes();
            byte[] hash = MessageDigest.getInstance("SHA1").digest(contents);
            return DatatypeConverter.printHexBinary(hash);
        } catch (Exception e) {
            throw new DataStoreException(e);
        }
    }
}
