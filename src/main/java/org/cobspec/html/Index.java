package org.cobspec.html;

import javaslang.collection.List;

public class Index {
    private String directory;
    private List<Link> links;

    public Index(String directory, List<Link> links) {
        this.directory = directory;
        this.links = links;
    }

    public String getDirectory() {
        return directory;
    }

    public List<Link> getLinks() {
        return links;
    }
}
