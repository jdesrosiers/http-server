package org.flint;

import javaslang.control.Option;

public interface RequestTarget {
    public String getPath();
    public Option<String> getQuery();
}