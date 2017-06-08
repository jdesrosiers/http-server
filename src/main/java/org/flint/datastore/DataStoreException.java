package org.flint.datastore;

public class DataStoreException extends Exception {
    public DataStoreException() {
        super();
    }

    public DataStoreException(String message) {
        super(message);
    }

    public DataStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataStoreException(String message, Throwable cause, boolean enableSupression, boolean writableStackTrace) {
        super(message, cause, enableSupression, writableStackTrace);
    }

    public DataStoreException(Throwable cause) {
        super(cause);
    }
}
