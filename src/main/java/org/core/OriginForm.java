package org.core;

public class OriginForm implements RequestTarget {
    private String path;
    private String query;

    public OriginForm(String path) {
        this.path = path;
    }

    public OriginForm(String path, String query) {
        this.path = path;
        this.query = query;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public String toString() {
        String requestTarget = path;
        if (query != null) {
            requestTarget += "?" + query;
        }

        return requestTarget;
    }
}
