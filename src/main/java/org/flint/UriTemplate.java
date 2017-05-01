package org.flint;

import javaslang.control.Option;
import javaslang.collection.HashMap;
import javaslang.collection.Map;

public class UriTemplate {
    private String uriTemplate;

    public UriTemplate(String uriTemplate) {
        this.uriTemplate = uriTemplate;
    }

    public Option<Map<String, String>> match(String uri) {
        if (uri.equals(uriTemplate)) {
            Map<String, String> matches = HashMap.of();
            return Option.of(matches);
        } else {
            return Option.none();
        }
    }
}
