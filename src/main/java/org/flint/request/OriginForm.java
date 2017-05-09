package org.flint.request;

import javaslang.control.Option;

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
    public Option<String> getQuery() {
        return Option.of(query);
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
